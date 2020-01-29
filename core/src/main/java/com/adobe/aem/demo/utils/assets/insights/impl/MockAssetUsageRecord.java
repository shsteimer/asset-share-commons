package com.adobe.aem.demo.utils.assets.insights.impl;

import com.day.cq.dam.usage.api.AssetUsageRecord;

import javax.jcr.RepositoryException;
import java.util.Calendar;
import java.util.Date;

public class MockAssetUsageRecord implements AssetUsageRecord {

    private ASSET_USAGE_TYPE type;

    private Date usedAt;

    public MockAssetUsageRecord(ASSET_USAGE_TYPE type, Calendar usedAt) {

        this.type = type;
        this.usedAt = usedAt.getTime();
    }

    /**
     * Instantiates a new AssetUsageRecord with given usage-type and used-date.
     *
     * @param type   the usage-type
     * @param usedAt the used-date
     */
    public MockAssetUsageRecord(ASSET_USAGE_TYPE type, Date usedAt) {

        this.type = type;
        this.usedAt = new Date(usedAt.getTime());
    }

    /**
     * Instantiates a new AssetUsageRecord with given usage-type.
     *
     * @param type the usage-type
     *
     * @throws RepositoryException the repository exception
     */
    public MockAssetUsageRecord(ASSET_USAGE_TYPE type) throws RepositoryException {
        this(type, Calendar.getInstance());

    }

    /*
     * (non-Javadoc)
     * @see com.day.cq.dam.usages.api.AssetUsageRecord#getUsedAt()
     */
    public Date getUsedAt() {
        return new Date(usedAt.getTime());
    }

    /*
     * (non-Javadoc)
     * @see com.day.cq.dam.usages.api.AssetUsageRecord#getUsageType()
     */
    public String getUsageType() {
        return type.toString();
    }

    /**
     * The Enum defines the list of ASSET USAGE TYPES.
     */
    public enum ASSET_USAGE_TYPE {

        CAMPAIGN("campaign"),

        TARGET("target"),

        SOCIAL("social"),

        MEDIA_OPTIMIZER("mediaOptimizer"),

        AEM("aem"),

        ASSET("asset"),

        UNKNOWN("unknown");

        private String type;

        ASSET_USAGE_TYPE(String type) {
            this.type = type;
        }

        private static ASSET_USAGE_TYPE fromString(String str) {
            if (str == null) {
                return ASSET_USAGE_TYPE.UNKNOWN;
            } else if (ASSET_USAGE_TYPE.CAMPAIGN.toString().equals(str)) {
                return ASSET_USAGE_TYPE.CAMPAIGN;
            } else if (ASSET_USAGE_TYPE.TARGET.toString().equals(str)) {
                return ASSET_USAGE_TYPE.TARGET;
            } else if (ASSET_USAGE_TYPE.SOCIAL.toString().equals(str)) {
                return ASSET_USAGE_TYPE.SOCIAL;
            } else if (ASSET_USAGE_TYPE.MEDIA_OPTIMIZER.toString().equals(str)) {
                return ASSET_USAGE_TYPE.MEDIA_OPTIMIZER;
            } else if (ASSET_USAGE_TYPE.AEM.toString().equals(str)) {
                return ASSET_USAGE_TYPE.AEM;
            } else if (ASSET_USAGE_TYPE.ASSET.toString().equals(str)) {
                return ASSET_USAGE_TYPE.ASSET;
            } else if ("collection".equals(str)) {  // we are saving the Collection and CompoundAsset usages under the Asset usage type.
                return ASSET_USAGE_TYPE.ASSET;
            } else if ("compoundAsset".equals(str)) {
                return ASSET_USAGE_TYPE.ASSET;
            } else {
                return ASSET_USAGE_TYPE.UNKNOWN;
            }
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        public String toString() {
            return type;
        }
    }
}
