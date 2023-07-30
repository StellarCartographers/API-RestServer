package space.tscg.restserver;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;
import space.tscg.restserver.http.HttpResponse;

public class CAPIAuthController
{
    public static final String ENDPOINT = "oauth/callback";

    public CAPIAuthController(Javalin javalin)
    {
        javalin.routes(() ->
        {
            path("oauth/callback", () ->
            {
                get(this::callback);
            });
        });
    }

    //!f
    @OpenApi(
        path = ENDPOINT,
        methods = {HttpMethod.GET},
        queryParams = {
            @OpenApiParam(
                name = "code",
                type = String.class, 
                required = true
            ),
            @OpenApiParam(
                name = "state",
                type = String.class, 
                required = true
            )
        },
        responses = {
            @OpenApiResponse(
                status = "200", 
                description = "The Fleet Carrier", 
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
    public void callback(Context ctx)
    {
        var http = CAPIService.callback(ctx.queryParam("code"), ctx.queryParam("state"));
        ctx.status(200).json(http);
    }
}
