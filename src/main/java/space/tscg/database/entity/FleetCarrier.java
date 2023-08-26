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
package space.tscg.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import elite.dangerous.capi.FleetCarrierData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import space.tscg.api.Diffable;
import space.tscg.api.carrier.IFleetCarrier;
import space.tscg.common.UpdatedValues;
import space.tscg.common.database.DbEntity;
import space.tscg.operation.Transformer;

@Getter
@Setter
@AllArgsConstructor
@Builder(builderMethodName = "Builder", toBuilder = true)
@Jacksonized
public class FleetCarrier extends DbEntity implements IFleetCarrier, Diffable<FleetCarrier> {
    
    public static final String TABLE_NAME = "registered";
    
    private final String carrierId;

    private String callsign;

    private String name;

    private String system;

    private int fuel;

    private CarrierServices services;

    @JsonIgnore
    FleetCarrier(String carrierId)
    {
        this.carrierId = carrierId;
    }
    
    @JsonIgnore
    public static FleetCarrier create(FleetCarrierData data)
    {
        return Transformer.transform(new FleetCarrier(data.getCarrierId()), data);
    }
    
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

    @Override
    public String getId() {
        return carrierId;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
