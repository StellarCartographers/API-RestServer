///**
// * Copyright (C) 2023  The Stellar Cartographers' Guild
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <https://www.gnu.org/licenses/>.
// */
package space.tscg.database;

import java.util.Optional;

import elite.dangerous.capi.CAPIFleetCarrier;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import space.tscg.api.Diffable;
import space.tscg.api.carrier.IFleetCarrier;
import space.tscg.common.UpdatedValues;
import space.tscg.common.db.modal.DbEntity;
import space.tscg.common.db.op.InsertOperation;
import space.tscg.common.db.prefab.TSCGDatabase;
import space.tscg.operation.Builders;

@Data
@Builder(builderMethodName = "Builder", toBuilder = true)
@Jacksonized
public class FleetCarrier implements DbEntity, IFleetCarrier, Diffable<FleetCarrier> {
    
    public static final String TABLE_NAME = "registered";
    
    private String carrierId;

    private String callsign;

    private String name;

    private String system;

    private int fuel;

    private CarrierServices services;

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValues diff(FleetCarrier other) {
        return UpdatedValues
            .Builder()
                .append("name", this.name, other.name)
                .append("fuel", this.fuel, other.fuel)
                .append("system", this.system, other.system)
                .appendDiff("services", this.services.diff(other.services))
                .buildUpdate();
    }
    
    public static Optional<FleetCarrier> get(String carrierId)
    {
        return Optional.ofNullable(TSCGDatabase.instance().get(TABLE_NAME, carrierId, FleetCarrier.class));
    }
    
    public static InsertOperation create(CAPIFleetCarrier fcd) {
        return TSCGDatabase.instance().create(Builders.fleetCarrier(fcd));
    }
    
    @Override
    public String getId() {
        return carrierId;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
