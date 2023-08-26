package space.tscg.restserver;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import elite.dangerous.capi.FleetCarrierData;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class FleetCarrierController
{
    public static final String  ENDPOINT = "/carriers";
    private FleetCarrierService service;

    public FleetCarrierController(Javalin javalin, FleetCarrierService service)
    {
        this.service = service;
        javalin.routes(() ->
        {
            path("carriers", () ->
            {
                post(this::createCarrier);
                patch(this::updateCarrier);
                get(this::getAllCarriers);
                path("{carrierId}", () ->
                {
                    get(this::getCarrier);
                    delete(this::deleteCarrier);
                });
            });
        });
    }

    public void createCarrier(Context ctx)
    {
        var http = service.create(ctx.bodyAsClass(FleetCarrierData.class));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.extras()[0]);
    }

    public void getCarrier(Context ctx)
    {
        var http = service.get(ctx.queryParam("carrierId"));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.type());
    }

    public void getAllCarriers(Context ctx)
    {
        var http = service.getAll();
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.type());
    }

    public void updateCarrier(Context ctx)
    {
        var http = service.update(ctx.bodyAsClass(FleetCarrierData.class));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.type().getUpdatedValues());
    }

    public void deleteCarrier(Context ctx)
    {
        var http = service.delete(ctx.queryParam("id"));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status());
    }
}
