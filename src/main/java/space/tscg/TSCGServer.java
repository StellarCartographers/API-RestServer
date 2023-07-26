package space.tscg;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import elite.dangerous.EliteAPI;
import io.javalin.Javalin;
import io.javalin.json.JsonMapper;
import io.javalin.openapi.JsonSchemaLoader;
import io.javalin.openapi.OpenApiContact;
import io.javalin.openapi.OpenApiInfo;
import io.javalin.openapi.OpenApiLicense;
import io.javalin.openapi.OpenApiServer;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import space.tscg.common.dotenv.Dotenv;
import space.tscg.database.CarriersDatabase;
import space.tscg.restserver.FleetCarrierController;
import space.tscg.restserver.FleetCarrierService;

public class TSCGServer {
    private Javalin javalin;

    private final ServerLogger serverLogger;

    public TSCGServer() {
        this.serverLogger = new ServerLogger();
        this.javalin      = this.createJavalin();
        this.addFleetCarrierEndpoints();
        this.serverLogger.setupLoggers(this.javalin);
    }

    private void addFleetCarrierEndpoints() {
        var fleetCarrierService = new FleetCarrierService(CarriersDatabase.get(), this.serverLogger);
        new FleetCarrierController(this.javalin, fleetCarrierService);
    }

    public void start() {
        this.javalin.start(Dotenv.ofInt("HTTP_PORT").orElse(8080));
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

    private Javalin createJavalin() {
        return Javalin.create(config -> {
            config.jsonMapper(this.gson());
            config.plugins.register(new OpenApiPlugin(getApiPluginConfig()));
            var swaggerConfiguration = new SwaggerConfiguration();
            config.plugins.register(new SwaggerPlugin(swaggerConfiguration));
            new JsonSchemaLoader().loadGeneratedSchemes();
        });
    }

    private OpenApiPluginConfiguration getApiPluginConfig() {
        var pluginConfig = new OpenApiPluginConfiguration();
        pluginConfig
            .withDefinitionConfiguration((v, d) -> d
                .withOpenApiInfo(i -> getApiInfo())
                    .withServer(s -> getServer(v)));
        return pluginConfig;
    }

    private OpenApiServer getServer(String version) {
        var server = new OpenApiServer();
        server.setUrl(Dotenv.ofString("API_SERVER_URL").orElse("http://localhost{port}/{basePath}/" + version + "/"));
        server.addVariable("port", Dotenv.ofString("HTTP_PORT").orElse(":8080"), new String[] {Dotenv.ofString("HTTP_PORT").orElse(":8080")}, "Port of the server");
        server.addVariable("basePath", "v1", new String[] {"v1"}, "Base path of the server");
        return server;
    }

    private OpenApiInfo getApiInfo() {
        var info = new OpenApiInfo();
        info.setTitle(Dotenv.ofString("API_INFO_TITLE").orElse("StellarCartographers API"));
        info.setTermsOfService(Dotenv.ofString("API_INFO_TOS_URL").orElse("https://stellarcartographers.github.io/API-RestServer/tos.html"));
        info.setVersion(Dotenv.ofString("API_INFO_VERSION").orElse("1.0.0"));
        info.setContact(this.getApiContact());
        info.setLicense(this.getApiLicense());
        return info;
    }

    private OpenApiLicense getApiLicense() {
        var license = new OpenApiLicense();
        license.setIdentifier(Dotenv.ofString("API_LICENSE_ID").orElse("AGPL-3.0-or-later"));
        license.setName(Dotenv.ofString("API_LICENSE_NAME").orElse("GNU Affero General Public License v3.0"));
        license.setUrl(Dotenv.ofString("API_LICENSE_URL").orElse("https://github.com/StellarCartographers/API-RestServer/blob/master/LICENSE"));
        return license;
    }

    private OpenApiContact getApiContact() {
        var contact = new OpenApiContact();
        contact.setName(Dotenv.ofString("API_CONTACT_NAME").orElse("TSCG WebAdmin"));
        contact.setUrl(Dotenv.ofString("API_CONTACT_URL").orElse("https://tscg.space/api-support"));
        contact.setEmail(Dotenv.ofString("API_CONTACT_EMAIL").orElse("webadmin@tscg.space"));
        return contact;
    }
}
