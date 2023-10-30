/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest.service;

import java.net.URI;

import org.tinylog.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;

import okhttp3.HttpUrl;
import panda.std.Blank;
import panda.std.Result;
import space.tscg.TSCGServer;
import space.tscg.collections.Data;
import space.tscg.database.FrontierAuth;
import space.tscg.database.defined.TSCGDatabase;
import space.tscg.database.entity.TSCGMember;
import space.tscg.internal.CAPIRequest;
import space.tscg.operation.Builders;
import space.tscg.util.Constants;
import space.tscg.util.PathValidator;
import space.tscg.util.QueryValidator;
import space.tscg.util.Tri;
import space.tscg.util.Util;
import space.tscg.web.Body;
import space.tscg.web.HttpError;
import space.tscg.web.Web;
import space.tscg.web.domain.Domain;
import space.tscg.web.domain.Endpoint;

public class CAPIAuthService
{
    private static final ObjectMapper               mapper   = new ObjectMapper();
    private static final Tri<String, State, CodeVerifier> triState = new Tri<>();

    public static Result<String, HttpError> authorizationLink(PathValidator validator)
    {
        if (validator.isErr())
            return validator.error();
        String discordId = validator.pathSegement();
        //!fr
        return Result.when(
            discordId.matches("\\d{17,19}"), 
            buildAuthorizationLink(discordId), 
            HttpError.badRequest(Data.asLinkedHashMap().add("error", "Invalid Discord ID: " + discordId)).getError()
        );
        //@f
    }

    private static String buildAuthorizationLink(String discordId)
    {
        if (TSCGMember.get(discordId).isEmpty())
        {
            TSCGMember member = TSCGMember.Builder().id(discordId).build();
            TSCGDatabase.instance().create(member);
        }

        Endpoint callback = Endpoint.OAUTH_CALLBACK;

        if (TSCGServer.TESTING)
            callback = Domain.of("localhost:9050").toEndpoint(Endpoint.OAUTH_CALLBACK);

        URI          callbackUri  = callback.toUri();
        CodeVerifier codeVerifier = new CodeVerifier();
        State        sessionId    = new State(16);
        triState.put(discordId, sessionId, codeVerifier);
        //!fr
        return new AuthorizationRequest.Builder(ResponseType.CODE, Constants.CLIENT_ID)
            .customParameter("audience", "frontier,steam,epic")
            .scope(Constants.SCOPE)
            .state(sessionId)
            .codeChallenge(codeVerifier, CodeChallengeMethod.S256)
            .endpointURI(Constants.AUTH_URI)
            .redirectionURI(callbackUri)
            .build().toURI().toString();
    }
    
    public static Result<Blank, HttpError> callbackTest(QueryValidator validator)
    {
        
        if(validator.isErr())
            return validator.error();
        
        var ctx = validator.getCtx();
        String discordId = ctx.queryParam("id");
        
        if (TSCGMember.get(discordId).isEmpty())
        {
            TSCGMember member = TSCGMember.Builder().id(discordId).build();
            TSCGDatabase.instance().create(member);
        }
        
        var authorized = mapper.createObjectNode().put("discordId", discordId).toString();
        HttpUrl authorizedUrl = HttpUrl.parse("https://tscg.network/bot/authorized");
        
        if(TSCGServer.TESTING)
            authorizedUrl = HttpUrl.parse("http://localhost:9055/bot/authorized");
        
        Web.POST.call(authorizedUrl, Body.json(authorized));
        
        return Result.ok();
    }
    
    public static Result<Blank, HttpError> callback(QueryValidator validator)
    {
        if(validator.isErr())
            return validator.error();
        
        var ctx = validator.getCtx();
        
        AuthorizationCode code = new AuthorizationCode(ctx.queryParam("code"));
        State state = new State(ctx.queryParam("state"));
        
        Endpoint callback = Endpoint.OAUTH_CALLBACK;
        
        if(TSCGServer.TESTING)
            callback = Domain.of("localhost:9050").toEndpoint(Endpoint.OAUTH_CALLBACK);
        
        CodeVerifier           verifier = triState.get_C_From_B(state).orElseThrow();
        AuthorizationCodeGrant grant    = new AuthorizationCodeGrant(code, callback.toUri(), verifier);
        CAPIRequest.Authorization auth = new CAPIRequest.Authorization(grant);

        var diOpt = triState.get_A_From_B(state);
        if (diOpt.isPresent())
        {
            var di = diOpt.get();
            var request = auth.processRequest();
            if(request.isErr())
                return Result.error(request.getError());
            
            var tokens = request.get();
            
            var expiresAt = Util.getExpiresAtEpoch(tokens.getAccessToken().getLifetime());
            Logger.info("getAccessToken: " + tokens.getAccessToken().getValue());
            Logger.info("getBearerAccessToken: " + tokens.getBearerAccessToken().getValue());
            
            FrontierAuth frontier = Builders.FrontierAuthBuilder()
                .refreshToken(tokens.getRefreshToken().toString())
                .accessToken(tokens.getAccessToken().toString())
                .expiresAt(expiresAt)
                .build();
            
            TSCGDatabase.instance().create(frontier).after(s -> {
                TSCGMember m = TSCGMember.get(di).orElseThrow();
                m.setAuthenticationId(frontier.getId());
                TSCGDatabase.instance().update(m);
            });
            
            var authorized = mapper.createObjectNode().put("discordId", di).toString();
            
            HttpUrl authorizedUrl = HttpUrl.parse("https://tscg.network/bot/authorized");
            
            if(TSCGServer.TESTING)
                authorizedUrl = HttpUrl.parse("Web://localhost:9055/bot/authorized");
            
            Web.POST.call(authorizedUrl, Body.json(authorized));
            triState.deleteByReference(state);
            return Result.ok();
        }
        return HttpError.notFound(Data.asLinkedHashMap().add("error", "Member was not found, this should not be possible at this point, but here we are"));
    }
}
