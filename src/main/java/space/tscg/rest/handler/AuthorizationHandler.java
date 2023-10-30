/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest.handler;

import static io.javalin.apibuilder.ApiBuilder.*;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import space.tscg.rest.service.CAPIAuthService;
import space.tscg.util.PathValidator;
import space.tscg.util.QueryValidator;

public class AuthorizationHandler implements EndpointGroup
{
    @Override
    public void addEndpoints()
    {
        path("v1/oauth", () -> {
            get("callback", this::callback);
            get("authorizationlink/{id}", this::authorizationLink);
        });
    }
    
    public void authorizationLink(Context ctx)
    {
        CAPIAuthService.authorizationLink(PathValidator.create(ctx, "id"))
            .peek(link -> ctx.status(HttpStatus.OK).result(link))
            .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
    }
    
    public void callback(Context ctx)
    {
        if(ctx.queryParamMap().containsKey("testing"))
        {
            CAPIAuthService.callbackTest(QueryValidator.create(ctx, "id"))
                .peek(v -> ctx.status(200))
                .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
        } else {
            CAPIAuthService.callback(QueryValidator.create(ctx, "code", "state"))
                .peek(v -> ctx.redirect("https://tscg.network/authenticated", HttpStatus.PERMANENT_REDIRECT))
                .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
        }
    }
}
