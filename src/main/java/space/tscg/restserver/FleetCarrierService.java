package space.tscg.restserver;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import com.rethinkdb.net.Result;

import elite.dangerous.capi.FleetCarrierData;
import panda.std.Blank;
import space.tscg.ServerLogger;
import space.tscg.common.util.TypePair;
import space.tscg.database.CarriersDatabase;
import space.tscg.database.FleetCarrier;
import space.tscg.operation.Transformer;
import space.tscg.operation.UpdatedOperation;

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
                return HttpResponse.conflict(FleetCarrierError.ALREADY_EXISTS, fcr.getUUID().toString());
            else
                return HttpResponse.internalServerError("internal server error has occured");
        }
        return HttpResponse.created("Fleet Carrier Registered", fco.getId());
    }

    public HttpResponse<FleetCarrier> get(String callsign, String name) {
        return get(this.getUUIDFromString(callsign + name).toString());
    }

    public HttpResponse<FleetCarrier> get(String uuid) {
        var FleetCarrier = database.getCarrierObject(uuid);
        if (FleetCarrier.isEmpty())
            return HttpResponse.notFound(FleetCarrierError.NOT_FOUND, uuid);
        return HttpResponse.ok(FleetCarrier.get());
    }

    public HttpResponse<UpdatedOperation> update(FleetCarrierData fcr) {
        TypePair.Builder<FleetCarrier> builder = TypePair.Builder();
        System.out.println(fcr.getUUID().toString());
        var                            fco     = database.getCarrierObject(fcr.getUUID().toString());
        if (fco.isPresent()) {
            var carrier = fco.get();
            builder.addType(carrier);
            carrier = Transformer.transform(carrier, fcr);
            var operation = database.update(carrier);
            if (operation.getReplaced() > 0) {
                builder.addType(database.getCarrierObject(fcr.getUUID().toString()).get());
                var updatedOperation = new UpdatedOperation(fcr.getUUID().toString(), builder.build().getDiff());
                return HttpResponse.ok(updatedOperation);
            }
            return HttpResponse.internalServerError(FleetCarrierError.DATABASE_ERROR);
        }
        return HttpResponse.notFound(FleetCarrierError.NOT_FOUND, fcr.getUUID().toString());
    }

    public HttpResponse<Blank> delete(String callsign, String carrierId) {
        return delete(this.getUUIDFromString(callsign + carrierId).toString());
    }

    public HttpResponse<Blank> delete(String uuid) {
        var operation = database.delete(uuid);
        if (operation.getDeleted() > 0) {
            return HttpResponse.accepted(uuid);
        }
        return HttpResponse.notFound(FleetCarrierError.NOT_FOUND, uuid);
    }

    public HttpResponse<List<FleetCarrier>> getAll() {
        Result<FleetCarrier> result = database.getAll("registered", FleetCarrier.class);
        if (result.bufferedCount() > 0) {
            return HttpResponse.ok(result.stream().toList());
        }
        return HttpResponse.internalServerError(FleetCarrierError.DATABASE_ERROR);
    }

    private UUID getUUIDFromString(String namespace) {
        return UUID.nameUUIDFromBytes(namespace.getBytes(Charset.forName("UTF-8")));
    }
}
