package space.tscg.restserver;

import java.io.IOException;
import java.net.URI;

import org.tinylog.Logger;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.pkce.CodeChallengeMethod;
import com.nimbusds.oauth2.sdk.pkce.CodeVerifier;
import com.nimbusds.oauth2.sdk.token.Tokens;

import panda.std.Blank;
import panda.std.Result;
import space.tscg.common.db.prefab.Member;
import space.tscg.common.db.prefab.TSCGDatabase;
import space.tscg.common.http.Data;
import space.tscg.common.http.HttpError;
import space.tscg.database.FrontierAuth;
import space.tscg.operation.Builders;
import space.tscg.restserver.http.QueryValidator;
import space.tscg.util.Constants;
import space.tscg.util.Tri;

public class CAPIAuthService
{
    private static Tri<String, State, CodeVerifier> triState = new Tri<>();
    
    public static Result<String, HttpError> authorizationLink(QueryValidator validator)
    {
        if(validator.isErr())
            return validator.error();
        
        var ctx = validator.getCtx();
        String discordId = ctx.queryParam("discordid");
        
        return Result.when(
            !discordId.matches("\\d{17,19}"), 
            buildAuthorizationLink(discordId), 
            HttpError.badRequest(Data.map().with("error", "Invalid Discord ID: " + discordId)).getError());
    }
    
    private static String buildAuthorizationLink(String discordId)
    {
        if (Member.get(discordId).isEmpty())
        {
            Member member = Member.Builder().id(discordId).build();
            TSCGDatabase.instance().create(member);
        }
        
        URI callbackUri = URI.create(Constants.CALLBACK_URL);
        CodeVerifier codeVerifier = new CodeVerifier();
        State        sessionId    = new State(8);

        triState.put(discordId, sessionId, codeVerifier);
        //!f
        return new AuthorizationRequest.Builder(ResponseType.CODE, Constants.CLIENT_ID)
            .customParameter("audience", "frontier,steam,epic")
            .scope(Constants.SCOPE)
            .state(sessionId)
            .codeChallenge(codeVerifier, CodeChallengeMethod.S256)
            .endpointURI(Constants.AUTH_URI)
            .redirectionURI(callbackUri)
            .build().toURI().toString();
    }
    
    public static Result<Blank, HttpError> callback(QueryValidator validator)
    {
        if(validator.isErr())
            return validator.error();
        
        var ctx = validator.getCtx();
        
        AuthorizationCode code = new AuthorizationCode(ctx.queryParam("code"));
        State state = new State(ctx.queryParam("state"));
        
        CodeVerifier           verifier = triState.get_C_From_B(state).orElseThrow();
        AuthorizationCodeGrant grant    = new AuthorizationCodeGrant(code, URI.create(Constants.CALLBACK_URL), verifier);
        TokenRequest           request  = new TokenRequest(Constants.TOKEN_URI, Constants.CLIENT_ID, grant);

        var diOpt = triState.get_A_From_B(state);
        if (diOpt.isPresent())
        {
            var di = diOpt.get();

            try
            {
                HTTPResponse response = request.toHTTPRequest().send();
                try
                {
                    TokenResponse r = TokenResponse.parse(response);
                    if (r instanceof AccessTokenResponse)
                    {
                        AccessTokenResponse tokenResponse = r.toSuccessResponse();
                        Tokens              tokens        = tokenResponse.getTokens();
                        //!f
                        FrontierAuth frontier = Builders.FrontierAuthBuilder()
                            .refreshToken(tokens.getRefreshToken())
                            .accessToken(tokens.getBearerAccessToken())
                            .expiresIn(tokens.getAccessToken().getLifetime())
                            .build();
                        
                        TSCGDatabase.instance().create(frontier).after(s -> {
                            Member m = Member.get(di).orElseThrow();
                            m.setAuthenticationId(frontier.getId());
                            TSCGDatabase.instance().update(m);
                        });
                        return Result.ok();
                    } else {
                        TokenErrorResponse errorResponse = r.toErrorResponse();
                        return HttpError.badRequest(Data.map().with(errorResponse.getErrorObject().toParameters()));
                        
                    }
                } catch (ParseException e)
                {
                    Logger.error(e, "Error while parsing HTTP Response");
                    return HttpError.internalServerError(Data.map()
                        .with("error", "Error while parsing HTTP Response")
                        .with("type", "ParseException")
                        .with("exception", e));
                }

            } catch (IOException e)
            {
                Logger.error("Error while sending HTTP Request", e);
                return HttpError.internalServerError(Data.map()
                    .with("error", "Error while sending HTTP Request")
                    .with("type", "IOException")
                    .with("exception", e));
            }
        }
        return HttpError.notFound(Data.map().with("error", "Member was not found, this should not be possible at this point, but here we are"));
    }
}
