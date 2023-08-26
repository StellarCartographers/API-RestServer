package space.tscg;

import java.lang.reflect.Type;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;

import elite.dangerous.EliteAPI;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import space.tscg.common.dotenv.Dotenv;
import space.tscg.database.CarriersDatabase;
import space.tscg.restserver.FleetCarrierController;
import space.tscg.restserver.FleetCarrierService;

public class TSCGServer
{
    interface DotFunction<E> extends Function<String, E> {
        
        default E applyDefault(String string, Object defaultVal) {
            return null;
        }
    }

    enum Variables {
        HTTP_PORT(Dotenv::getInt),
        TESTING(Dotenv::getBoolean);

        DotFunction<?> func;
        
        Variables(DotFunction<?> func)
        {
            this.func = func;
        }
        
        @SuppressWarnings("unchecked")
        public <E> E get()
        {
            return (E) func.apply(this.toString());
        }
        
        public <E> E get(E defaultVal)
        {   
            return get() != null ? get() : defaultVal;
        }
    }
    
    private Javalin javalin;

    private final ServerLogger serverLogger;
    
    public static final boolean TESTING = Variables.TESTING.get(false);
    public static final int SERVER_PORT = Variables.HTTP_PORT.get(9050);

    public TSCGServer()
    {
        Logger.info("Is Testing: " + TESTING);
        this.serverLogger = new ServerLogger();
        this.javalin = this.createJavalin();
        this.addFleetCarrierEndpoints();
        this.serverLogger.setupLoggers(this.javalin);
    }

    private void addFleetCarrierEndpoints()
    {
        var fleetCarrierService = new FleetCarrierService(CarriersDatabase.get(), this.serverLogger);
        new FleetCarrierController(this.javalin, fleetCarrierService);
    }

    public void start()
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
        });
    }
}
