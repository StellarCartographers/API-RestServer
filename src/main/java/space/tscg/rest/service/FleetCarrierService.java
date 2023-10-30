/**
 * Copyright (c) 2023 The Stellar Cartographers' Guild.
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.rest.service;

import java.util.List;

import elite.dangerous.Elite4J.CAPI;
import elite.dangerous.capi.FleetCarrierData;
import io.javalin.http.Context;
import panda.std.Result;
import space.tscg.database.defined.TSCGDatabase;
import space.tscg.database.entity.FleetCarrier;
import space.tscg.internal.error.FleetCarrierError;
import space.tscg.misc.TypePair;
import space.tscg.operation.DatabaseUtil;
import space.tscg.operation.UpdatedOperation;
import space.tscg.web.HttpState;
import space.tscg.web.States;

public class FleetCarrierService
{
    private static final TSCGDatabase database = TSCGDatabase.instance();

    public static Result<String, HttpState> create(Context ctx)
    {
        FleetCarrierData fcd = CAPI.parse(ctx.body(), FleetCarrierData.class).get();
        FleetCarrier fleetCarrier = FleetCarrier.buildCarrier(fcd);
        var createOperation = TSCGDatabase.instance().create(fleetCarrier);
        if (createOperation.getInserted() == 0) {
            if ((createOperation.getFirstError() != null) && createOperation.getFirstError().startsWith("Duplicate primary key"))
                return Result.error(FleetCarrierError.ALREADY_EXISTS.getState(fcd.getCarrierId()));
            else
                return Result.error(States.INTERNAL_SERVER_ERROR);
        }
        return Result.ok(fcd.getCarrierId());
    }

    public static Result<FleetCarrier, HttpState> get(String id)
    {
        var fc = FleetCarrier.get(id);
        return fc.<Result<FleetCarrier, HttpState>> map(Result::ok).orElseGet(() -> Result.error(FleetCarrierError.NOT_FOUND.getState(id)));
    }

    public static Result<UpdatedOperation, HttpState> update(Context ctx, String id)
    {
        TypePair.Builder<FleetCarrier> builder = TypePair.Builder();
        //
        var fco = FleetCarrier.get(id);

        if (fco.isPresent()) {
            FleetCarrierData fcd = ctx.bodyAsClass(FleetCarrierData.class);
            if (fcd != null) {
                var carrier = fco.get();
                builder.addType(carrier);
                carrier = FleetCarrier.buildCarrier(fcd);
                var operation = database.update(carrier);
                return Result.when(operation.operationSucceded(), completeOperation(builder, fcd), FleetCarrierError.DATABASE_ERROR.getState());
            }
        }

        return Result.error(FleetCarrierError.NOT_FOUND.getState(id));
    }

    private static UpdatedOperation completeOperation(TypePair.Builder<FleetCarrier> builder, FleetCarrierData fcd)
    {
        var updated = FleetCarrier.get(fcd.getCarrierId()).orElseThrow();
        builder.addType(updated);
        return new UpdatedOperation(fcd.getCarrierId(), builder.build().getDiffMap());
    }

    public static Result<String, HttpState> delete(String id)
    {
        var operation = database.delete("carriers", id);
        return Result.when(operation.operationSucceded(), id, FleetCarrierError.NOT_FOUND.getState(id));
    }

    public static Result<List<FleetCarrier>, HttpState> getAll()
    {
        var list = DatabaseUtil.getAllCarriers();
        return Result.when(!list.isEmpty(), list, FleetCarrierError.DATABASE_ERROR.getState());
    }
}
