/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.operation;

import java.util.List;

import space.tscg.database.defined.TSCGDatabase;
import space.tscg.database.entity.FleetCarrier;

public final class DatabaseUtil
{
    public static List<FleetCarrier> getAllCarriers()
    {
        var result = TSCGDatabase.instance().getAll("carriers", FleetCarrier.class);
        return (result.bufferedCount() > 0) ? result.stream().toList() : List.of();
    }
}
