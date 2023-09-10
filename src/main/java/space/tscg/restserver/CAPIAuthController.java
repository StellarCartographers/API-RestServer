package space.tscg.restserver;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import space.tscg.common.domain.URLEndpoint;
import space.tscg.restserver.http.QueryValidator;

public class CAPIAuthController
{
    public CAPIAuthController(Javalin javalin)
    {
        javalin.routes(() ->
        {
            path(URLEndpoint.API_OAUTH, () -> {
                get("callback", this::callback);
                get("authorizationlink", this::authorizationLink);
            });
        });
    }

    public void authorizationLink(Context ctx)
    {
        CAPIAuthService.authorizationLink(QueryValidator.create(ctx, "discordid"))
            .peek(link -> ctx.status(HttpStatus.OK).json(link))
            .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
    }

    public void callback(Context ctx)
    {
        CAPIAuthService.callback(QueryValidator.create(ctx, "code", "state"))
            .peek(v -> ctx.redirect("https://tscg.network/authenticated", HttpStatus.PERMANENT_REDIRECT))
            .onError(err -> ctx.status(err.state().toHttpStatus()).json(err));
    }
}
