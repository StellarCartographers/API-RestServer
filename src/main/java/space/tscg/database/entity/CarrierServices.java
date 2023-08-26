package space.tscg.database.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import space.tscg.api.Diffable;
import space.tscg.api.carrier.ICarrierServices;
import space.tscg.api.carrier.IService;
import space.tscg.api.carrier.ITaxableService;
import space.tscg.common.UpdatedValues;

@Getter
@Builder(builderMethodName = "Builder")
@Jacksonized
public class CarrierServices implements ICarrierServices, Diffable<CarrierServices>
{
    private TaxableService refuel;

    private TaxableService repair;

    private TaxableService armoury;

    private Service redemptionOffice;

    private TaxableService shipyard;

    private TaxableService outfitting;

    private Service secureWarehouse;

    private Service universalCartographics;

    private Service concourseBar;

    private Service vistaGenomics;

    private TaxableService pioneerSupplies;

    @Override
    public UpdatedValues diff(CarrierServices other)
    {
        return UpdatedValues
            .Builder()
                .appendDiff("refuel", this.refuel.diff(other.getRefuel()))
                .appendDiff("repair", this.repair.diff(other.getRepair()))
                .appendDiff("armoury", this.armoury.diff(other.getArmoury()))
                .appendDiff("redemptionOffice", this.redemptionOffice.diff(other.getRedemptionOffice()))
                .appendDiff("shipyard", this.shipyard.diff(other.getShipyard()))
                .appendDiff("outfitting", this.outfitting.diff(other.getOutfitting()))
                .appendDiff("secureWarehouse", this.secureWarehouse.diff(other.getSecureWarehouse()))
                .appendDiff("universalCartographics", this.universalCartographics.diff(other.getUniversalCartographics()))
                .appendDiff("concourseBar", this.concourseBar.diff(other.getConcourseBar()))
                .appendDiff("vistaGenomics", this.vistaGenomics.diff(other.getVistaGenomics()))
                .appendDiff("pioneerSupplies", this.pioneerSupplies.diff(other.getPioneerSupplies()))
                .buildUpdate();
    }
    
    @Getter
    @Builder(builderMethodName = "Creator")
    @Jacksonized
    public static class Service implements IService, Diffable<Service> {
        private boolean enabled;
        
        @Override
        public UpdatedValues diff(Service other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.isEnabled(), other.isEnabled())
                    .buildUpdate();
        }
    }
    
    @Getter
    @Builder(builderMethodName = "Creator")
    @Jacksonized
    public static class TaxableService implements ITaxableService, Diffable<TaxableService> {
        private boolean enabled;
        private int tax;
        
        @Override
        public UpdatedValues diff(TaxableService other) {
            return UpdatedValues
                .Builder()
                    .append("enabled", this.isEnabled(), other.isEnabled())
                    .append("tax", this.getTax(), other.getTax())
                    .buildUpdate();
        }
    }
}
