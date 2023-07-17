package space.tscg.operation;

import elite.dangerous.capi.FleetCarrierData;
import elite.dangerous.capi.meta.Finance.ServiceTaxation;
import elite.dangerous.capi.meta.ServicesCrew;
import space.tscg.database.FleetCarrier;
import space.tscg.database.Services;
import space.tscg.database.Services.Armoury;
import space.tscg.database.Services.ConcourseBar;
import space.tscg.database.Services.Outfitting;
import space.tscg.database.Services.PioneerSupplies;
import space.tscg.database.Services.RedemptionOffice;
import space.tscg.database.Services.Refuel;
import space.tscg.database.Services.Repair;
import space.tscg.database.Services.SecureWarehouse;
import space.tscg.database.Services.Shipyard;
import space.tscg.database.Services.UniversalCartographics;
import space.tscg.database.Services.VistaGenomics;

public class Transformer {
    /**
     * Creates a FleetCarrier instance from the data returned from Fontiers CAPI /fleetcarrier endpoint
     *
     * @param carrier the FleetCarrierResult
     * @return a new FleetCarrier instance
     */
    public static FleetCarrier transform(FleetCarrier fc, FleetCarrierData data) {
        ServicesCrew    services = data.getServicesCrew();
        ServiceTaxation taxation = data.getFinance().getServiceTaxation();
        return fc
            .toBuilder()
                .carrierId(data.getMarket().getId())
                .callsign(data.getName().getCallsign())
                .vanityName(data.getName().getVanityName())
                .currentStarSystem(data.getCurrentStarSystem())
                .fuel(Integer.valueOf(data.getFuel()))
                .services(Services
                    .Creator()
                        .refuel(Refuel.Creator().active(services.isRefuelEnabled()).taxRate(taxation.refuel).build())
                        .repair(Repair.Creator().active(services.isRepairEnabled()).taxRate(taxation.repair).build())
                        .armoury(Armoury.Creator().active(services.isRearmEnabled()).taxRate(taxation.rearm).build())
                        .redemptionOffice(RedemptionOffice.Creator().active(services.isRedemptionOfficeEnabled()).build())
                        .shipyard(Shipyard.Creator().active(services.isShipyardEnabled()).taxRate(taxation.shipyard).build())
                        .outfitting(Outfitting.Creator().active(services.isOutfittingEnabled()).taxRate(taxation.outfitting).build())
                        .secureWarehouse(SecureWarehouse.Creator().active(services.isBlackmarketEnabled()).build())
                        .universalCartographics(UniversalCartographics.Creator().active(services.isUniversalCartographicsEnabled()).build())
                        .concourseBar(ConcourseBar.Creator().active(services.isConcourseEnabled()).build())
                        .vistaGenomics(VistaGenomics.Creator().active(services.isVistaGenomicsEnabled()).build())
                        .pioneerSupplies(PioneerSupplies.Creator().active(services.isPioneerSuppliesEnabled()).taxRate(taxation.pioneersupplies).build())
                        .build())
                .build();
    }
}
