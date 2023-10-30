/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest.handler;

import static io.javalin.apibuilder.ApiBuilder.*;

import org.jetbrains.annotations.NotNull;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import space.tscg.collections.Data;
import space.tscg.rest.service.FleetCarrierService;
import space.tscg.web.States;

public class FleetcarrierHandler implements CrudHandler, EndpointGroup
{
    @Override
    public void addEndpoints()
    {
        crud("v1/carrier" + "/{carrierId}", this);
    }

    @Override
    public void create(@NotNull Context ctx)
    {
        var state = States.UNAUTHORIZED.withData(Data.of("Fleet Carriers cannot be CREATED via API"));
        ctx.status(state.getCode()).json(state.toJson());
    }

    @Override
    public void delete(@NotNull Context ctx, @NotNull String id)
    {
        FleetCarrierService.delete(id)
            .peek(del -> ctx.status(States.OK.toHttpStatus()).result(del))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void getAll(@NotNull Context ctx)
    {
        FleetCarrierService.getAll()
            .peek(fcl -> ctx.status(States.OK.toHttpStatus()).json(fcl))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void getOne(@NotNull Context ctx, @NotNull String id)
    {
        FleetCarrierService.get(id)
            .peek(fc -> ctx.status(States.OK.toHttpStatus()).json(fc))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void update(@NotNull Context ctx, @NotNull String id)
    {
        FleetCarrierService.update(ctx, id)
            .peek(op -> ctx.status(States.OK.toHttpStatus()).json(op))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }
}
