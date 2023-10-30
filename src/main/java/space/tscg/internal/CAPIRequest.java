/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.internal;

import java.io.IOException;
import java.net.URI;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.Tokens;

import panda.std.Result;
import space.tscg.collections.Data;
import space.tscg.util.Constants;
import space.tscg.web.HttpError;

public class CAPIRequest
{
    TokenRequest request;

    CAPIRequest(URI uri, ClientID clientID, AuthorizationGrant authzGrant)
    {
        this.request = new TokenRequest(uri, clientID, authzGrant);
    }

    protected Result<HTTPResponse, HttpError> sendRequest()
    {
        try
        {
            return Result.ok(request.toHTTPRequest().send());
        } catch (IOException e)
        {
            return HttpError.internalServerError(e);
        }
    }

    public static class Authorization extends CAPIRequest
    {
        public Authorization(AuthorizationCodeGrant grant)
        {
            super(Constants.TOKEN_URI, Constants.CLIENT_ID, grant);
        }

        public Result<Tokens, HttpError> processRequest()
        {
            var resp = this.sendRequest();
            if (resp.isErr())
                return resp.projectToError();
            
            TokenResponse response;
            try
            {
                response = TokenResponse.parse(resp.get());
            } catch (ParseException e)
            {
                return HttpError.internalServerError(e);
            }
            if (response instanceof AccessTokenResponse)
            {
                var tokenResponse = response.toSuccessResponse();
                return Result.ok(tokenResponse.getTokens());
            }
            return HttpError.badRequest(Data.of(response.toErrorResponse().getErrorObject().toParameters()));
        }
    }

    public static class Refresh extends CAPIRequest
    {
        public Refresh(RefreshTokenGrant grant)
        {
            super(Constants.TOKEN_URI, Constants.CLIENT_ID, grant);
        }

        public Result<Tokens, HttpError> processRequest()
        {
            var resp = this.sendRequest();
            if (resp.isErr())
                return resp.projectToError();
            
            TokenResponse response;
            try
            {
                response = TokenResponse.parse(resp.get());
            } catch (ParseException e)
            {
                return HttpError.internalServerError(e);
            }
            if (response instanceof AccessTokenResponse)
            {
                var tokenResponse = response.toSuccessResponse();
                return Result.ok(tokenResponse.getTokens());
            }
            return HttpError.badRequest(Data.of(response.toErrorResponse().getErrorObject().toParameters()));
        }
    }
}
