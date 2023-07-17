package space.tscg;

import static io.javalin.apibuilder.ApiBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import lombok.NoArgsConstructor;
import space.tscg.restserver.HttpResponse;

@NoArgsConstructor
public class ServerLogger
{
    private static Logger errLogger = LoggerFactory.getLogger("FC-API/Error");

    public void setupLoggers(Javalin javalin)
    {
        javalin.routes(() ->
        {
            before(Before::logIncomingRequest);
        });
    }

    public static void error(Context ctx, HttpResponse<?> response, String message, Object... args)
    {
        var start = "[%s] [%s]".formatted(response.status().getCode(), ctx.method().toString());
        errLogger.error("%s %s", start, String.format(message, args));
    }

    private static class Before
    {

        private static Logger requestLogger = LoggerFactory.getLogger("FC-API/Req/Info");

        private static void logIncomingRequest(Context ctx)
        {
            requestLogger.info("IpAddr: %s | Path: %s | Method: %s".formatted(ctx.ip(), ctx.path(), ctx.method().name()));
        }
    }
}
