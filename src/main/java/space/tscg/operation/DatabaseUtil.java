package space.tscg.operation;

import java.util.List;

import space.tscg.common.db.prefab.TSCGDatabase;
import space.tscg.database.FleetCarrier;

public final class DatabaseUtil
{
    public static List<FleetCarrier> getAllCarriers()
    {
        var result = TSCGDatabase.instance().getAll("registered", FleetCarrier.class);
        return (result.bufferedCount() > 0) ? result.stream().toList() : List.of();
    }
}
