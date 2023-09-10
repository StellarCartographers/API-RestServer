package space.tscg;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import elite.dangerous.EliteAPI;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import space.tscg.common.dotenv.Dotenv;
import space.tscg.restserver.CAPIAuthController;
import space.tscg.restserver.CAPIController;
import space.tscg.restserver.FleetCarrierController;
import space.tscg.restserver.FleetCarrierService;


public class TSCGServer
{
    private Javalin javalin;

    private ServerLogger serverLogger;
    
    public static boolean TESTING = Dotenv.getBoolean("testing", false);
    int SERVER_PORT =  Dotenv.getInt("javalin_port" ,9050);

    TSCGServer()
    {
        Logger.info("Is Testing: " + TESTING);
        this.serverLogger = new ServerLogger();
        this.javalin = this.createJavalin();
        this.addEndpoints();
        this.serverLogger.setupLoggers(this.javalin);
    }

    private void addEndpoints()
    {
        var fleetCarrierService = new FleetCarrierService(this.serverLogger);
        new FleetCarrierController(this.javalin, fleetCarrierService);
        new CAPIAuthController(this.javalin);
        new CAPIController(this.javalin);
    }

    void start()
    {
        Logger.info("Server Port: " + SERVER_PORT);
        this.javalin.start(SERVER_PORT);
    }

    private JsonMapper gson()
    {
        return new JsonMapper()
        {
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type)
            {
                return EliteAPI.toJson(obj, type);
            }

            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType)
            {
                return EliteAPI.fromJson(json, targetType);
            }
        };
    }

    private Javalin createJavalin()
    {
        return Javalin.create(config ->
        {
            config.jsonMapper(this.gson());
            config.plugins.enableRouteOverview("/overview");
            config.plugins.enableDevLogging();
        });
    }
}
