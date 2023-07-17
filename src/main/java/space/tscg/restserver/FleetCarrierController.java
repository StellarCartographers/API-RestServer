package space.tscg.restserver;

import elite.dangerous.capi.FleetCarrierData;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;
import space.tscg.common.entity.FleetCarrier;
import space.tscg.common.util.UpdatedValues;

public class FleetCarrierController
{

    public static final String ENDPOINT = "/api/v1/carrier";
    private FleetCarrierService service;

    public FleetCarrierController(Javalin javalin, FleetCarrierService service)
    {
        this.service = service;
        javalin.post(ENDPOINT, this::create);
        javalin.get(ENDPOINT, this::get);
        javalin.patch(ENDPOINT, this::update);
        javalin.delete(ENDPOINT, this::delete);
    }

    //@noformat
    @OpenApi(
        path = ENDPOINT,
        methods = { HttpMethod.POST },
        summary = "Create FleetCarrier",
        description = "Registers a new FleetCarrier and returns it",
        tags = {"Carrier"},
        requestBody = @OpenApiRequestBody(
            content = @OpenApiContent(from = FleetCarrierData.class)
            ),
        responses = {
            @OpenApiResponse(
                status = "201",
                description = "Registered Fleet Carrier",
                content = {@OpenApiContent(from = HttpResponse.class)}
                ),
            @OpenApiResponse(
                status = "409",
                description = "The Fleet Carrier is already registered",
                content = {@OpenApiContent(from = HttpResponse.class)}
                ),
            @OpenApiResponse(
                status = "500",
                description = "Server encountered an error during operation",
                content = {@OpenApiContent(from = HttpResponse.class)}
                )
        }
        )
    //@format
    public void create(Context ctx)
    {
        var http = service.create(ctx.bodyAsClass(FleetCarrierData.class));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.extras()[0]);
    }

    //@noformat
    @OpenApi(
        path = ENDPOINT,
        methods = { HttpMethod.GET },
        summary = "Get FleetCarrier",
        description = "Get an exisiting FleetCarrier",
        tags = {"Carrier"},
        queryParams = {
            @OpenApiParam(name = "callsign", type = String.class, description = "The Fleet Carrier callsign"),
            @OpenApiParam(name = "name", type = String.class, description = "The Fleet Carrier name")
        },
        responses = {
            @OpenApiResponse(
                status = "200",
                description = "The Fleet Carrier",
                content = {@OpenApiContent(from = FleetCarrier.class)}
                ),
            @OpenApiResponse(
                status = "400",
                description = "Fleet Carrier was not found",
                content = {@OpenApiContent(from = HttpResponse.class)}
                ),
            @OpenApiResponse(
                status = "500",
                description = "Server encountered an error during operation",
                content = {@OpenApiContent(from = HttpResponse.class)}
                )
        }
        )
    //@format
    public void get(Context ctx)
    {
        var http = service.get(ctx.queryParam("callsign"), ctx.queryParam("name"));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.type());
    }

    //@noformat
    @OpenApi(
        path = ENDPOINT,
        methods = { HttpMethod.PATCH },
        summary = "Update FleetCarrier",
        description = "Updates exisiting FleetCarrier and returns only the property values that changed",
        tags = {"Carrier"},
        requestBody = @OpenApiRequestBody(
            content = @OpenApiContent(from = FleetCarrierData.class)
            ),
        responses = {
            @OpenApiResponse(
                status = "200",
                description = "Values that were updated",
                content = {@OpenApiContent(from = UpdatedValues.class)}
                ),
            @OpenApiResponse(
                status = "400",
                description = "Fleet Carrier was not found",
                content = {@OpenApiContent(from = HttpResponse.class)}
                ),
            @OpenApiResponse(
                status = "500",
                description = "Server encountered an error during operation",
                content = {@OpenApiContent(from = HttpResponse.class)}
                )
        }
        )
    //@format
    public void update(Context ctx)
    {
        var http = service.update(ctx.bodyAsClass(FleetCarrierData.class));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status()).json(http.type().getUpdatedValues());
    }

    //@noformat
    @OpenApi(
        path = ENDPOINT,
        methods = { HttpMethod.DELETE },
        summary = "Delete Fleet Carrier",
        description = "Delete Fleet Carrier by Name and Callsign",
        tags = {"Carrier"},
        queryParams = {
            @OpenApiParam(name = "callsign", type = String.class, description = "The Fleet Carrier callsign"),
            @OpenApiParam(name = "carrierId", type = String.class, description = "The Fleet Carrier Id")
        },
        responses = {
            @OpenApiResponse(
                status = "200",
                description = "Fleet Carrier deleted"
                ),
            @OpenApiResponse(
                status = "400",
                description = "Fleet Carrier was not found",
                content = {@OpenApiContent(from = HttpResponse.class)}
                ),
            @OpenApiResponse(
                status = "500",
                description = "Server encountered an error during operation",
                content = {@OpenApiContent(from = HttpResponse.class)}
                )
        }
        )
    //@format
    public void delete(Context ctx)
    {
        var http = service.delete(ctx.queryParam("callsign"), ctx.queryParam("carrierId"));
        if (http.isError())
        {
            ctx.status(http.status()).json(http.customMessage());
            return;
        }
        ctx.status(http.status());
    }
}
