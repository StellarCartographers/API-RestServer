package space.tscg.database;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import space.tscg.common.util.Diffable;
import space.tscg.common.util.UpdatedValues;

//
@Data
@Builder(builderMethodName = "Creator", toBuilder = true)
@Jacksonized
public class Services implements Diffable<Services> {
    private Services.Refuel refuel;

    private Services.Repair repair;

    private Services.Armoury armoury;

    private Services.RedemptionOffice redemptionOffice;

    private Services.Shipyard shipyard;

    private Services.Outfitting outfitting;

    private Services.SecureWarehouse secureWarehouse;

    private Services.UniversalCartographics universalCartographics;

    private Services.ConcourseBar concourseBar;

    private Services.VistaGenomics vistaGenomics;

    private Services.PioneerSupplies pioneerSupplies;

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class Refuel implements Diffable<Services.Refuel> {
        private boolean active;

        private int taxRate;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.Refuel other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .append("taxRate", this.taxRate, other.taxRate)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class Repair implements Diffable<Services.Repair> {
        private boolean active;

        private int taxRate;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.Repair other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .append("taxRate", this.taxRate, other.taxRate)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class Armoury implements Diffable<Services.Armoury> {
        private boolean active;

        private int taxRate;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.Armoury other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .append("taxRate", this.taxRate, other.taxRate)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class RedemptionOffice implements Diffable<Services.RedemptionOffice> {
        private boolean active;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.RedemptionOffice other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class Shipyard implements Diffable<Services.Shipyard> {
        private boolean active;

        private int taxRate;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.Shipyard other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .append("taxRate", this.taxRate, other.taxRate)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class Outfitting implements Diffable<Services.Outfitting> {
        private boolean active;

        private int taxRate;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.Outfitting other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .append("taxRate", this.taxRate, other.taxRate)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class SecureWarehouse implements Diffable<Services.SecureWarehouse> {
        private boolean active;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.SecureWarehouse other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class UniversalCartographics implements Diffable<Services.UniversalCartographics> {
        private boolean active;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.UniversalCartographics other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class ConcourseBar implements Diffable<Services.ConcourseBar> {
        private boolean active;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.ConcourseBar other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class VistaGenomics implements Diffable<Services.VistaGenomics> {
        private boolean active;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.VistaGenomics other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .buildUpdate();
        }
    }

    @Data
    @Builder(builderMethodName = "Creator", toBuilder = true)
    @Jacksonized
    public static class PioneerSupplies implements Diffable<Services.PioneerSupplies> {
        private boolean active;

        private int taxRate;

        /**
         * {@inheritDoc}
         */
        @Override
        public UpdatedValues diff(Services.PioneerSupplies other) {
            return UpdatedValues
                .Builder()
                    .append("active", this.active, other.active)
                    .append("taxRate", this.taxRate, other.taxRate)
                    .buildUpdate();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UpdatedValues diff(Services other) {
        return UpdatedValues
            .Builder()
                .appendDiff("refuel", this.refuel.diff(other.refuel))
                .appendDiff("repair", this.repair.diff(other.repair))
                .appendDiff("armoury", this.armoury.diff(other.armoury))
                .appendDiff("redemptionOffice", this.redemptionOffice.diff(redemptionOffice))
                .appendDiff("shipyard", this.shipyard.diff(other.shipyard))
                .appendDiff("outfitting", this.outfitting.diff(other.outfitting))
                .appendDiff("secureWarehouse", this.secureWarehouse.diff(other.secureWarehouse))
                .appendDiff("universalCartographics", this.universalCartographics.diff(other.universalCartographics))
                .appendDiff("concourseBar", this.concourseBar.diff(other.concourseBar))
                .appendDiff("vistaGenomics", this.vistaGenomics.diff(other.vistaGenomics))
                .appendDiff("pioneerSupplies", this.pioneerSupplies.diff(other.pioneerSupplies))
                .buildUpdate();
    }
}