package space.tscg.internal;

import java.io.IOException;
import java.net.URI;

import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.AuthorizationGrant;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.RefreshTokenGrant;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.Tokens;

import panda.std.Result;
import space.tscg.common.http.Data;
import space.tscg.common.http.HttpError;
import space.tscg.util.Constants;

public class CAPIRequest extends TokenRequest
{
    
    CAPIRequest(URI uri, ClientID clientID, AuthorizationGrant authzGrant)
    {
        super(uri, clientID, authzGrant);
    }
    
    protected Result<HTTPResponse, HttpError> sendRequest()
    {
        try
        {
            return Result.ok(super.toHTTPRequest().send());
        } catch (IOException e)
        {
            return HttpError.internalServerError(e);
        }
    }

    public static class Authorization extends CAPIRequest
    {
        public Authorization(AuthorizationCodeGrant grant)
        {
            super(Constants.AUTH_URI, Constants.CLIENT_ID, grant);
        }
        
        public Result<Tokens, HttpError> processRequest()
        {
            var resp = this.sendRequest();
            if(resp.isErr())
                return resp.projectToError();
            
            var tr = Result.supplyThrowing(ParseException.class, () -> TokenResponse.parse(resp.get()));
            
            if(tr.isErr())
                return HttpError.internalServerError(tr.getError());
            
            var response = tr.get();
            
            if(response instanceof TokenErrorResponse)
                return HttpError.badRequest(Data.of(response.toErrorResponse().getErrorObject().toParameters()));
            
            return Result.ok(response.toSuccessResponse().getTokens());
        }
    }
    
    public static class Refresh extends CAPIRequest
    {

        Refresh(RefreshTokenGrant grant)
        {
            super(Constants.TOKEN_URI, Constants.CLIENT_ID, grant);
        }
        
        public Result<Tokens, HttpError> processRequest()
        {
            var resp = this.sendRequest();
            if(resp.isErr())
                return resp.projectToError();
            
            var tr = Result.supplyThrowing(ParseException.class, () -> TokenResponse.parse(resp.get()));
            
            if(tr.isErr())
                return HttpError.internalServerError(tr.getError());
            
            var response = tr.get();
            
            if(response instanceof TokenErrorResponse)
                return HttpError.badRequest(Data.of(response.toErrorResponse().getErrorObject().toParameters()));
            
            return Result.ok(response.toSuccessResponse().getTokens());
        }
    }
}
