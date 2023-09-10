package space.tscg.restserver;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import space.tscg.common.domain.URLEndpoint;
import space.tscg.restserver.http.QueryValidator;

public class CAPIController
{
    public CAPIController(Javalin javalin)
    {
        javalin.routes(() ->
        {
            path(URLEndpoint.API_CAPI, () -> {
                get("{discordid}/carrier", this::carrierEndpoint);
                get("{discordid}/profile", this::profileEndpoint);
            });
        });
    }

    public void carrierEndpoint(Context ctx)
    {
        CAPIService.callFleetCarrierEndpoint(QueryValidator.create(ctx, "discordid"))
            .peek(fcd -> ctx.status(HttpStatus.OK).json(fcd))
            .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
    }

    public void profileEndpoint(Context ctx)
    {
//        if(!TSCGServer.TESTING)
//            CAPIAuthService.callback(new AuthorizationCode(ctx.queryParam("code")), new State(ctx.queryParam("state")));
        ctx.redirect("https://tscg.network/authenticated", HttpStatus.PERMANENT_REDIRECT);
    }
}
