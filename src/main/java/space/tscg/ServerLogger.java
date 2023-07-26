package space.tscg;

import static io.javalin.apibuilder.ApiBuilder.before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.Javalin;
import io.javalin.http.Context;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServerLogger
{
    public void setupLoggers(Javalin javalin)
    {
        javalin.routes(() ->
        {
            before(Before::logIncomingRequest);
        });
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
