package space.tscg.restserver;

import java.util.List;

import elite.dangerous.capi.FleetCarrierData;
import io.javalin.http.Context;
import panda.std.Result;
import space.tscg.ServerLogger;
import space.tscg.common.TypePair;
import space.tscg.common.db.prefab.TSCGDatabase;
import space.tscg.common.http.HttpState;
import space.tscg.database.FleetCarrier;
import space.tscg.operation.Builders;
import space.tscg.operation.DatabaseUtil;
import space.tscg.operation.UpdatedOperation;
import space.tscg.restserver.http.FleetCarrierError;

public class FleetCarrierService
{
    ServerLogger         logger;
    private TSCGDatabase database;

    public FleetCarrierService(ServerLogger logger)
    {
        this.logger = logger;
        this.database = TSCGDatabase.instance();
    }

    public Result<String, HttpState> create(Context ctx)
    {
        FleetCarrierData fcd = ctx.bodyStreamAsClass(FleetCarrierData.class);
        var createOperation = FleetCarrier.create(fcd);
        if (createOperation.getInserted() == 0)
        {
            if ((createOperation.getFirstError() != null) && createOperation.getFirstError().startsWith("Duplicate primary key"))
                return Result.error(FleetCarrierError.ALREADY_EXISTS.getState(fcd.getCarrierId()));
            else
                return Result.error(HttpState.INTERNAL_SERVER_ERROR);
        }
        return Result.ok(fcd.getCarrierId());
    }

    public Result<FleetCarrier, HttpState> get(String id)
    {
        var fc = FleetCarrier.get(id);
        if (fc.isEmpty())
            return Result.error(FleetCarrierError.NOT_FOUND.getState(id));
        return Result.ok(fc.get());
    }

    public Result<UpdatedOperation, HttpState> update(Context ctx, String id)
    {
        TypePair.Builder<FleetCarrier> builder = TypePair.Builder();
        //
        var fco = FleetCarrier.get(id);

        if (fco.isPresent())
        {
            FleetCarrierData fcd = ctx.bodyStreamAsClass(FleetCarrierData.class);
            if(fcd != null)
            {
                var carrier = fco.get();
                builder.addType(carrier);
                carrier = Builders.fleetCarrier(fcd);
                var operation = database.update(carrier);
                return Result.when(operation.operationSucceded(), completeOperation(builder, fcd), FleetCarrierError.DATABASE_ERROR.getState());
            }
        }
        
        return Result.error(FleetCarrierError.NOT_FOUND.getState(id));
    }
    
    private UpdatedOperation completeOperation(TypePair.Builder<FleetCarrier> builder, FleetCarrierData fcd)
    {
        builder.addType(FleetCarrier.get(fcd.getCarrierId()).get());
        return new UpdatedOperation(fcd.getCarrierId(), builder.build().getDiff());
    }

    public Result<String, HttpState> delete(String id)
    {
        var operation = database.delete("carriers", id);
        return Result.when(operation.operationSucceded(), id, FleetCarrierError.NOT_FOUND.getState(id));
    }

    public Result<List<FleetCarrier>, HttpState> getAll()
    {
        var list = DatabaseUtil.getAllCarriers();
        return Result.when(!list.isEmpty(), list, FleetCarrierError.DATABASE_ERROR.getState());
    }
}
