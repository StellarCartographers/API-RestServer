package space.tscg.restserver;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.UUID;

import elite.dangerous.capi.FleetCarrierData;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiRequestBody;
import io.javalin.openapi.OpenApiResponse;
import io.javalin.openapi.OpenApiSecurity;
import space.tscg.common.util.UpdatedValues;
import space.tscg.database.entity.FleetCarrier;
import space.tscg.restserver.http.HttpResponse;

public class FleetCarrierController
{
    public static final String ENDPOINT = "/carriers";

    private FleetCarrierService service;

    public FleetCarrierController(Javalin javalin, FleetCarrierService service)
    {
        this.service = service;
        javalin.routes(() -> {
           path("carriers", () -> {
               post(this::createCarrier);
               patch(this::updateCarrier);
               get(this::getAllCarriers);
               path("{carrierId}", () -> {
                   get(this::getCarrier);
                   delete(this::deleteCarrier);
               });
           });
        });
    }

    //!f
    @OpenApi(
        path = ENDPOINT,
        methods = {HttpMethod.POST},
        summary = "Create FleetCarrier",
        description = """
        	Registers a new FleetCarrier to the database
        	
        	Impl Note: The request body is the response provided by Frontier's CAPI /fleetcarrier endpoint.\s\
        	This endpoint will only ever be called by the TSCG Bot after a Discord user invokes the RegisterCarrier\
        	command. This endpoint requires a bi-daily rotating API-Key and is only shown for documentation purposes.""",
        versions = {"v1"},
        tags = {"Carrier"},
        security = { 
            @OpenApiSecurity(name = "ApiKeyAuth") 
        },
        headers = {
            @OpenApiParam(name = "X-API-Key", required = true, type = UUID.class)
        },
        requestBody = @OpenApiRequestBody(
            content = @OpenApiContent(
                from = FleetCarrierData.class
            )
        ),
        responses = {
            @OpenApiResponse(
                status = "201", 
                description = "Registered Fleet Carrier", 
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            ),
            @OpenApiResponse(
                status = "409",
                description = "The Fleet Carrier is already registered",
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            ), 
            @OpenApiResponse(
                status = "500", 
                description = "Server encountered an error during operation", 
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            )
        }
    )
    //@f
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

    //!f
    @OpenApi(
        path = ENDPOINT + "/{carrierId}",
        methods = {HttpMethod.GET},
        summary = "Get FleetCarrier",
        description = "Get an exisiting FleetCarrier",
        tags = {"Carrier"},
        pathParams = {
            @OpenApiParam(
                name = "carrierId", 
                type = String.class, 
                required = true,
                description = "The Fleet Carrier id"
            )
        },
        responses = {
            @OpenApiResponse(
                status = "200", 
                description = "The Fleet Carrier", 
                content = {
                    @OpenApiContent(from = FleetCarrier.class)
                }
            ), 
            @OpenApiResponse(
                status = "400", 
                description = "Fleet Carrier was not found", 
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            ), 
            @OpenApiResponse(
                status = "500",
                description = "Server encountered an error during operation",
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            )
        }
    )
    //@f
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

    //!f
    @OpenApi(
        path = ENDPOINT,
        methods = {HttpMethod.GET},
        summary = "Get All Registered FleetCarriers",
        description = "Get all exisiting FleetCarriers",
        tags = {"Carrier"},
        responses = {
            @OpenApiResponse(
                status = "200", 
                description = "The list of Registered Fleet Carriers", 
                content = {
                    @OpenApiContent(from = FleetCarrier[].class)
                }
            ), 
            @OpenApiResponse(
                status = "400",
                description = "Fleet Carrier was not found",
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            ), 
            @OpenApiResponse(
                status = "500", 
                description = "Server encountered an error during operation", 
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            )
        }
    )
    //@f
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

    //!f
    @OpenApi(
        path = ENDPOINT,
        methods = {HttpMethod.PATCH},
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
                content = {
                    @OpenApiContent(from = UpdatedValues.class)
                }
            ), 
            @OpenApiResponse(
                status = "400",
                description = "Fleet Carrier was not found",
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            ), 
            @OpenApiResponse(
                status = "500", 
                description = "Server encountered an error during operation", 
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            )
        }
    )
    //@f
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

    //!f
    @OpenApi(
        path = ENDPOINT + "/{carrierId}",
        methods = {HttpMethod.DELETE},
        summary = "Delete Fleet Carrier",
        description = "Delete Fleet Carrier by Carrier ID",
        tags = {"Carrier"},
        pathParams = {
            @OpenApiParam(
                name = "carrierId", 
                type = String.class, 
                required = true,
                description = "The Fleet Carrier id"
            )
        },
        responses = {
            @OpenApiResponse(
                status = "200", 
                description = "Fleet Carrier deleted"
            ), 
            @OpenApiResponse(
                status = "400", 
                description = "Fleet Carrier was not found", 
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            ), 
            @OpenApiResponse(
                status = "500",
                description = "Server encountered an error during operation",
                content = {
                    @OpenApiContent(from = HttpResponse.class)
                }
            )
        }
    )
    //@f
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
