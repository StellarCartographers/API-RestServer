package space.tscg.operation;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.oauth2.sdk.token.RefreshToken;

import elite.dangerous.capi.FleetCarrierData;
import elite.dangerous.capi.meta.Finance.ServiceTaxation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import space.tscg.database.CarrierServices;
import space.tscg.database.FleetCarrier;
import space.tscg.database.FrontierAuth;
import space.tscg.database.CarrierServices.Service;
import space.tscg.database.CarrierServices.TaxableService;
import space.tscg.operation.encryption.EncryptedKey;
import elite.dangerous.capi.meta.ServicesCrew;

public class Builders {
    /**
     * Creates a FleetCarrier instance from the data returned from Fontiers CAPI /fleetcarrier endpoint
     *
     * @param carrier the FleetCarrierResult
     * @return a new FleetCarrier instance
     */
    public static FleetCarrier fleetCarrier(FleetCarrierData data) {
        ServicesCrew    services = data.getServicesCrew();
        ServiceTaxation taxation = data.getFinance().getServiceTaxation();
        return FleetCarrier.Builder()
                .carrierId(data.getCarrierId())
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
    
    public static FrontierAuthentication FrontierAuthBuilder()
    {
        return new FrontierAuthentication();
    }
    
    public static FrontierAuth frontierAuth(BearerAccessToken accessToken, RefreshToken refreshToken, long expiresIn)
    {
        return new FrontierAuthentication()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .expiresIn(expiresIn)
            .build();
    }
    
    @ToString
    @Getter
    @NoArgsConstructor(access = AccessLevel.PACKAGE)
    @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
    public static class FrontierAuthentication {
        private EncryptedKey<BearerAccessToken> accessToken;
        private EncryptedKey<RefreshToken> refreshToken;
        private long expiresIn;

        public FrontierAuthentication accessToken(final BearerAccessToken accessToken) {
            this.accessToken = EncryptedKey.of(accessToken);
            return this;
        }
        
        public FrontierAuthentication refreshToken(final RefreshToken refreshToken) {
            this.refreshToken = EncryptedKey.of(refreshToken);
            return this;
        }

        public FrontierAuthentication expiresIn(final long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public FrontierAuth build() {
            return new FrontierAuth(this);
        }
    }
}
