package space.tscg;

import static io.javalin.apibuilder.ApiBuilder.before;

import org.tinylog.Logger;

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
        public static void log(String format)
        {
            Logger.info(format);
        }
        
        public static void log(String format, Object... args)
        {
            Logger.info(format, args);
        }
    }

    private static class Before
    {

        private static void logIncomingRequest(Context ctx)
        {
            Logger.info("IpAddr: %s | Path: %s | Method: %s".formatted(ctx.host(), ctx.path(), ctx.method().name()));
        }
    }
}
