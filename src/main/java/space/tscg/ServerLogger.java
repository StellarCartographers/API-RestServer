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

    public static class AuthService
    {
        private static Logger authLogger = LoggerFactory.getLogger("FC-API/Auth");
        
        public static void log(String format)
        {
            authLogger.info(format);
        }
        
        public static void log(String format, Object... args)
        {
            authLogger.info(format, args);
        }
    }

    private static class Before
    {

        private static Logger requestLogger = LoggerFactory.getLogger("FC-API/Req");

        private static void logIncomingRequest(Context ctx)
        {
            requestLogger.info("IpAddr: %s | Path: %s | Method: %s".formatted(ctx.host(), ctx.path(), ctx.method().name()));
        }
    }
}
