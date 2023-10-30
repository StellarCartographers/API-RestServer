/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest.handler;

import static io.javalin.apibuilder.ApiBuilder.*;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import space.tscg.database.Metrics;

public class ServerMetricsHandler implements EndpointGroup
{
    @Override
    public void addEndpoints()
    {
        path("server", () -> {
            get("ping", this::ping);
            get("metrics", this::metrics);
        });
    }

    private void ping(Context ctx)
    {
        ctx.status(HttpStatus.OK);
    }
    
    private void metrics(Context ctx)
    {
        ctx.status(HttpStatus.ACCEPTED).json(new MetricsResponse(Metrics.get().orElseThrow().getRequests()));
    }

    private static record MetricsResponse(int requests){};
}
