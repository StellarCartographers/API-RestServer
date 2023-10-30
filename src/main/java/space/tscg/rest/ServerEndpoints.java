/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest;

import org.tinylog.Logger;

import io.javalin.Javalin;
import io.javalin.http.Context;
import space.tscg.database.Metrics;
import space.tscg.database.defined.TSCGDatabase;
import space.tscg.rest.handler.AuthorizationHandler;
import space.tscg.rest.handler.CAPIProxyHandler;
import space.tscg.rest.handler.FleetcarrierHandler;
import space.tscg.rest.handler.ServerMetricsHandler;

public class ServerEndpoints
{
    public ServerEndpoints(Javalin isntance)
    {
        isntance.before("/api/*", this::metrics);
        isntance.before("/server/*", this::metrics);

        isntance.routes(() ->
        {
            new ServerMetricsHandler().addEndpoints();
            new AuthorizationHandler().addEndpoints();
            new CAPIProxyHandler().addEndpoints();
            new FleetcarrierHandler().addEndpoints();
        });
    }

    private void metrics(Context ctx)
    {
        var metrics = Metrics.get().orElseThrow();
        int before  = metrics.getRequests();
        metrics.increment();
        int after = metrics.getRequests();
        Logger.tag("Metrics").info("increment 'requests' from '{}' -> '{}'", before, after);
        TSCGDatabase.instance().update(metrics);
    }
}
