package space.tscg.restserver;

import java.util.List;

import com.rethinkdb.net.Result;

import elite.dangerous.capi.FleetCarrierData;
import panda.std.Blank;
import space.tscg.ServerLogger;
import space.tscg.common.TypePair;
import space.tscg.database.CarriersDatabase;
import space.tscg.database.entity.FleetCarrier;
import space.tscg.operation.Transformer;
import space.tscg.operation.UpdatedOperation;
import space.tscg.restserver.http.FleetCarrierError;
import space.tscg.restserver.http.HttpResponse;

public class FleetCarrierService {
    ServerLogger logger;

    private CarriersDatabase database;

    public FleetCarrierService(CarriersDatabase database, ServerLogger logger) {
        this.logger   = logger;
        this.database = database;
    }

    public HttpResponse<Blank> create(FleetCarrierData fcr) {
        var fco       = FleetCarrier.create(fcr);
        var operation = database.create(fco);
        System.out.println(operation);
        if (operation.getInserted() == 0) {
            if ((operation.getFirstError() != null) && operation.getFirstError().startsWith("Duplicate primary key"))
                return HttpResponse.conflict(FleetCarrierError.ALREADY_EXISTS, fcr.getCarrierId());
            else
                return HttpResponse.internalServerError("internal server error has occured");
        }
        return HttpResponse.created("Fleet Carrier Registered", fco.getId());
    }

    public HttpResponse<FleetCarrier> get(String id) {
        var FleetCarrier = database.getCarrierObject(id);
        if (FleetCarrier.isEmpty())
            return HttpResponse.notFound(FleetCarrierError.NOT_FOUND, id);
        return HttpResponse.ok(FleetCarrier.get());
    }

    public HttpResponse<UpdatedOperation> update(FleetCarrierData fcr) {
        TypePair.Builder<FleetCarrier> builder = TypePair.Builder();
        System.out.println(fcr.getCarrierId());
        var                            fco     = database.getCarrierObject(fcr.getCarrierId());
        if (fco.isPresent()) {
            var carrier = fco.get();
            builder.addType(carrier);
            carrier = Transformer.transform(carrier, fcr);
            var operation = database.update(carrier);
            if (operation.getReplaced() > 0) {
                builder.addType(database.getCarrierObject(fcr.getCarrierId()).get());
                var updatedOperation = new UpdatedOperation(fcr.getCarrierId(), builder.build().getDiff());
                return HttpResponse.ok(updatedOperation);
            }
            return HttpResponse.internalServerError(FleetCarrierError.DATABASE_ERROR);
        }
        return HttpResponse.notFound(FleetCarrierError.NOT_FOUND, fcr.getCarrierId());
    }

    public HttpResponse<Blank> delete(String id) {
        var operation = database.delete(id);
        if (operation.getDeleted() > 0) {
            return HttpResponse.accepted(id);
        }
        return HttpResponse.notFound(FleetCarrierError.NOT_FOUND, id);
    }

    public HttpResponse<List<FleetCarrier>> getAll() {
        Result<FleetCarrier> result = database.getAll("registered", FleetCarrier.class);
        if (result.bufferedCount() > 0) {
            return HttpResponse.ok(result.stream().toList());
        }
        return HttpResponse.internalServerError(FleetCarrierError.DATABASE_ERROR);
    }
}
