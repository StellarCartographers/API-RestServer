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
import space.tscg.rest.service.CAPIService;
import space.tscg.util.PathValidator;

public class CAPIProxyHandler implements EndpointGroup
{
    @Override
    public void addEndpoints()
    {
        path("v1/capi", () -> {
            get("{id}/carrier", this::carrierEndpoint);
            get("{id}/profile", this::profileEndpoint);
        });
    }

    public void carrierEndpoint(Context ctx)
    {
        if(ctx.queryString().equals("testing"))
        {
            CAPIService.callFleetCarrierTest(PathValidator.create(ctx, "id"))
                .peek(fcd -> ctx.status(HttpStatus.OK).result(fcd))
                .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
        } else {
            CAPIService.callFleetCarrierEndpoint(PathValidator.create(ctx, "id"))
                .peek(fcd -> ctx.status(HttpStatus.OK).result(fcd))
                .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
        }
    }

    public void profileEndpoint(Context ctx)
    {
        CAPIService.callProfileEndpoint(PathValidator.create(ctx, "id"))
            .peek(profile -> ctx.status(HttpStatus.OK).json(profile))
            .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
    }
}
