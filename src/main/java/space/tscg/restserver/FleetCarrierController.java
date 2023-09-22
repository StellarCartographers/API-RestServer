package space.tscg.restserver;

import static io.javalin.apibuilder.ApiBuilder.crud;

import io.javalin.Javalin;
import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import space.tscg.common.domain.Route;
import space.tscg.common.http.HttpState;

public class FleetCarrierController implements CrudHandler
{
    public FleetCarrierController(Javalin javalin)
    {
        javalin.routes(() ->
        {
            crud(Route.CARRIER.getRoute() + "/{carrierId}", this);
        });
    }

    @Override
    public void create(Context ctx)
    {
        FleetCarrierService.create(ctx)
            .peek(id -> ctx.status(HttpState.CREATED.toHttpStatus()).json(id))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void delete(Context ctx, String id)
    {
        FleetCarrierService.delete(id)
            .peek(del -> ctx.status(HttpState.OK.toHttpStatus()).json(del))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void getAll(Context ctx)
    {
        FleetCarrierService.getAll()
            .peek(fcl -> ctx.status(HttpState.OK.toHttpStatus()).json(fcl))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void getOne(Context ctx, String id)
    {
        FleetCarrierService.get(id)
            .peek(fc -> ctx.status(HttpState.OK.toHttpStatus()).json(fc))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }

    @Override
    public void update(Context ctx, String id)
    {
        FleetCarrierService.update(ctx, id)
            .peek(op -> ctx.status(HttpState.OK.toHttpStatus()).json(op))
            .onError(err -> ctx.status(err.toHttpStatus()).json(err));
    }
}
