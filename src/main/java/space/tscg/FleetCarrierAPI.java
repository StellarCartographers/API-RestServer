package space.tscg;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import elite.dangerous.EliteAPI;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import io.javalin.openapi.JsonSchemaLoader;
import io.javalin.openapi.JsonSchemaResource;
import io.javalin.openapi.OpenApiContact;
import io.javalin.openapi.OpenApiLicense;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import space.tscg.database.CarriersDatabase;
import space.tscg.restserver.FleetCarrierController;
import space.tscg.restserver.FleetCarrierService;

public class FleetCarrierAPI
{

    private Javalin      javalin;
    private final ServerLogger serverLogger;

    public FleetCarrierAPI()
    {
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
        this.javalin.start(Env.getAsInt("HTTP_PORT"));
    }
    
    private JsonMapper gson() {
        return new JsonMapper() {
            @Override
            public String toJsonString(@NotNull Object obj, @NotNull Type type) {
                return EliteAPI.toJson(obj, type);
            }

            @Override
            public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
                return EliteAPI.fromJson(json, targetType);
            }
        };
    }

    private Javalin createJavalin()
    {
        return Javalin.create(config ->
        {
            config.jsonMapper(this.gson());
            config.plugins.register(new OpenApiPlugin(new OpenApiPluginConfiguration().withDefinitionConfiguration((version, definition) -> definition.withOpenApiInfo(openApiInfo ->
            {
                var openApiContact = new OpenApiContact();
                openApiContact.setName("API Support");
                openApiContact.setUrl("https://www.example.com/support");
                openApiContact.setEmail("support@example.com");
                var openApiLicense = new OpenApiLicense();
                openApiLicense.setName("Apache 2.0");
                openApiLicense.setIdentifier("Apache-2.0");
                openApiInfo.setTitle("Awesome App");
                openApiInfo.setDescription("App description goes right here");
                openApiInfo.setTermsOfService("https://example.com/tos");
                openApiInfo.setContact(openApiContact);
                openApiInfo.setLicense(openApiLicense);
                openApiInfo.setVersion("1.0.0");
            }).withServer(openApiServer ->
            {
                openApiServer.setUrl(("http://localhost:8080/v1/"));
                openApiServer.setDescription("Server description goes here");
                openApiServer.addVariable("port", "8080", new String[] {"8080"}, "Port of the server");
                openApiServer.addVariable("basePath", "v1", new String[] {"v1"}, "Base path of the server");
            }))));
            var swaggerConfiguration = new SwaggerConfiguration();
            config.plugins.register(new SwaggerPlugin(swaggerConfiguration));
            for (JsonSchemaResource generatedJsonSchema : new JsonSchemaLoader().loadGeneratedSchemes())
            {
                System.out.println(generatedJsonSchema.getName());
                System.out.println(generatedJsonSchema.getContentAsString());
            }
        });
    }

    public static void main(String[] args)
    {
        var api = new FleetCarrierAPI();
        api.start();
    }
}
