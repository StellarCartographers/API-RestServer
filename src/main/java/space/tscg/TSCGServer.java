/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg;

import org.tinylog.Logger;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.plugin.bundled.DevLoggingPlugin;
import io.javalin.util.JavalinLogger;
import space.tscg.database.Metrics;
import space.tscg.internal.local.LocalTesting;
import space.tscg.properties.dot.Dotenv;
import space.tscg.rest.ServerEndpoints;


public class TSCGServer
{
    private final Javalin javalin;

    public static boolean TESTING;
    int SERVER_PORT =  Dotenv.getInt("javalin_port" ,9050);

    TSCGServer()
    {
        if(Dotenv.get("db_host").equals("localhost"))
        {
            Logger.tag("LocalTesting").info("Detected Local Testing Parameters");
            Logger.tag("LocalTesting").info("Setting up Database DefinedTable if needed");
            LocalTesting.setupDefinedTableIfNeeded();
            TESTING = true;
            Logger.tag("LocalTesting").info("TESTING: " + TESTING);
        }
        Metrics.createIfNeeded();
        this.javalin = this.createJavalin();
        new ServerEndpoints(javalin);
        this.start();
    }

    void start()
    {
        Logger.tag("TSCGServer").info("Server Port: " + SERVER_PORT);
        this.javalin.start(SERVER_PORT);
    }

    private Javalin createJavalin()
    {
        JavalinLogger.useTinyLogger("Javalin");
        return Javalin.create(config ->
        {
            config.showJavalinBanner = false;
            config.jsonMapper(new JavalinJackson().updateMapper(mapper -> mapper.registerModule(new JavaTimeModule())));
            config.plugins.enableRouteOverview("/overview");
            config.plugins.register(new DevLoggingPlugin());
        });
    }
}
