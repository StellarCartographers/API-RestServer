package space.tscg;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import elite.dangerous.EliteAPI;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import io.javalin.openapi.ApiKeyAuth;
import io.javalin.openapi.JsonSchemaLoader;
import io.javalin.openapi.OpenApiContact;
import io.javalin.openapi.OpenApiLicense;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import space.tscg.common.dotenv.Dotenv;
import space.tscg.database.CarriersDatabase;
import space.tscg.restserver.CAPIAuthController;
import space.tscg.restserver.FleetCarrierController;
import space.tscg.restserver.FleetCarrierService;

public class TSCGServer
{
    private Javalin javalin;

    private final ServerLogger serverLogger;

    public TSCGServer()
    {
        System.out.println(Dotenv.retrieve("USER"));
        
        this.serverLogger = new ServerLogger();
        this.javalin = this.createJavalin();
        this.addFleetCarrierEndpoints();
        this.serverLogger.setupLoggers(this.javalin);
    }

    private void addFleetCarrierEndpoints()
    {
        var fleetCarrierService = new FleetCarrierService(CarriersDatabase.get(), this.serverLogger);
        new FleetCarrierController(this.javalin, fleetCarrierService);
        new CAPIAuthController(this.javalin);
    }

    public void start()
    {
        this.javalin.start(Dotenv.retrieveInt("HTTP_PORT", 9050));
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
            config.plugins.register(new OpenApiPlugin(new OpenApiPluginConfiguration().withDefinitionConfiguration((version, definition) -> definition.withOpenApiInfo(info ->
            {
                info.setTitle(Dotenv.retrieve("API_INFO_TITLE", "StellarCartographers API"));
                info.setTermsOfService(Dotenv.retrieve("API_INFO_TOS_URL", "https://stellarcartographers.github.io/API-RestServer/tos.html"));
                info.setVersion(Dotenv.retrieve("API_INFO_VERSION", "1.0.0"));

                OpenApiContact contact = new OpenApiContact();
                contact.setName(Dotenv.retrieve("API_CONTACT_NAME", "TSCG WebAdmin"));
                contact.setUrl(Dotenv.retrieve("API_CONTACT_URL", "https://tscg.space/api-support"));
                contact.setEmail(Dotenv.retrieve("API_CONTACT_EMAIL", "webadmin@tscg.space"));

                OpenApiLicense license = new OpenApiLicense();
                license.setIdentifier(Dotenv.retrieve("API_LICENSE_ID", "AGPL-3.0-or-later"));
                license.setName(Dotenv.retrieve("API_LICENSE_NAME", "GNU Affero General Public License v3.0"));
                license.setUrl(Dotenv.retrieve("API_LICENSE_URL", "https://github.com/StellarCartographers/API-RestServer/blob/master/LICENSE"));

                info.setContact(contact);
                info.setLicense(license);
            }).withServer(server ->
            {
                server.setUrl("http://localhost:{port}/api/{version}/");
                server.setDescription("Used for local testing only");
                server.addVariable("port", "8080", new String[] {"8080", "9050"}, "Port of the server");
                server.addVariable("version", "v1", new String[] {"v1"}, "Base path of the server");
            }).withServer(server ->
            {
                server.setUrl("https://tscg.network/api/{version}/");
                server.addVariable("version", "v1", new String[] {"v1"}, "The API Version in short form");
            }).withSecurity(security ->
            {
                security.withSecurityScheme("ApiKeyAuth", new ApiKeyAuth());
            }))));

            var swaggerConfiguration = new SwaggerConfiguration();
            swaggerConfiguration.setUiPath("/api/swagger");
            config.plugins.register(new SwaggerPlugin(swaggerConfiguration));
            new JsonSchemaLoader().loadGeneratedSchemes();
        });
    }
}
