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

import com.fasterxml.jackson.annotation.JsonIgnore;

import elite.dangerous.capi.FleetCarrierData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import space.tscg.common.database.ManagedObject;
import space.tscg.common.util.Diffable;
import space.tscg.common.util.UpdatedValues;
import space.tscg.operation.Transformer;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder(builderMethodName = "Builder", toBuilder = true)
@Jacksonized
public class FleetCarrier implements ManagedObject, Diffable<FleetCarrier> {
    
    static final String TABLE_NAME = "registered";
    
    private final String id;
    
    private String carrierId;

    private String callsign;

    private String vanityName;

    private String currentStarSystem;

    private int fuel;

    private Services services;

    @JsonIgnore
    FleetCarrier(String id)
    {
        this.id = id;
    }
    
    @JsonIgnore
    public static FleetCarrier create(FleetCarrierData data)
    {
        System.out.println("CREATE = " + data.getUUID().toString());
        return Transformer.transform(new FleetCarrier(data.getUUID().toString()), data);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValues diff(FleetCarrier other) {
        return UpdatedValues
            .Builder()
                .append("vanityName", this.vanityName, other.vanityName)
                .append("fuel", this.fuel, other.fuel)
                .append("currentSolarSystem", this.currentStarSystem, other.currentStarSystem)
                .appendDiff("services", this.services.diff(other.services))
                .buildUpdate();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
