package space.tscg.operation;

import elite.dangerous.capi.FleetCarrierData;
import elite.dangerous.capi.meta.Finance.ServiceTaxation;
import elite.dangerous.capi.meta.ServicesCrew;
import space.tscg.database.entity.CarrierServices;
import space.tscg.database.entity.CarrierServices.Service;
import space.tscg.database.entity.CarrierServices.TaxableService;
import space.tscg.database.entity.FleetCarrier;

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
                .callsign(data.getName().getCallsign())
                .name(data.getName().getVanityName())
                .system(data.getCurrentStarSystem())
                .fuel(Integer.valueOf(data.getFuel()))
                .services(CarrierServices.
                    Builder()
                        .refuel(TaxableService.Creator().enabled(services.isRefuelEnabled()).tax(taxation.refuel).build())
                        .repair(TaxableService.Creator().enabled(services.isRepairEnabled()).tax(taxation.repair).build())
                        .armoury(TaxableService.Creator().enabled(services.isRearmEnabled()).tax(taxation.rearm).build())
                        .redemptionOffice(Service.Creator().enabled(services.isRedemptionOfficeEnabled()).build())
                        .shipyard(TaxableService.Creator().enabled(services.isShipyardEnabled()).tax(taxation.shipyard).build())
                        .outfitting(TaxableService.Creator().enabled(services.isOutfittingEnabled()).tax(taxation.outfitting).build())
                        .secureWarehouse(Service.Creator().enabled(services.isBlackmarketEnabled()).build())
                        .universalCartographics(Service.Creator().enabled(services.isUniversalCartographicsEnabled()).build())
                        .concourseBar(Service.Creator().enabled(services.isConcourseEnabled()).build())
                        .vistaGenomics(Service.Creator().enabled(services.isVistaGenomicsEnabled()).build())
                        .pioneerSupplies(TaxableService.Creator().enabled(services.isPioneerSuppliesEnabled()).tax(taxation.pioneersupplies).build())
                        .build())
                .build();
    }
}
