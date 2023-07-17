package space.tscg.database;

import static com.rethinkdb.RethinkDB.r;

import java.util.Optional;

import com.rethinkdb.net.Result;

import space.tscg.common.database.Database;
import space.tscg.common.database.Operation;

public class CarriersDatabase extends Database
{

    private static final CarriersDatabase _instance = new CarriersDatabase();

    public static CarriersDatabase get()
    {
        return _instance;
    }

    public Optional<FleetCarrier> getCarrierObject(String uuid)
    {
        var obj = r.table(FleetCarrier.TABLE_NAME).get(uuid).runAtom(connection(), FleetCarrier.class);
        return (obj == null) ? Optional.empty() : Optional.of(obj);
    }

    public Operation delete(String uuid)
    {
        return r.table(FleetCarrier.TABLE_NAME).get(uuid).delete().run(connection(), Operation.class).single();
    }

    public <T> Result<T> getAll(String tableName, Class<T> target)
    {
        return r.table(tableName).run(connection(), target);
    }
}
