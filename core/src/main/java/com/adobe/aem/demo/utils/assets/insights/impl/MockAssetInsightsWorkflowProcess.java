package com.adobe.aem.demo.utils.assets.insights.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.aem.demo.utils.impl.QueryUtil;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.dam.performance.api.AssetPerformanceTracker;
import com.day.cq.dam.usage.api.AssetUsageRecord;
import com.day.cq.dam.usage.api.AssetUsageTracker;
import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Component(service = {WorkflowProcess.class, Executable.class},
        property = {
                "process.label=AEM Demo Utils - Asset Insights - Asset Insights Performance and Usage Generator"
        }
)
@Designate(ocd = MockAssetInsightsWorkflowProcess.Cfg.class)
public class MockAssetInsightsWorkflowProcess extends AbstractExecutable implements WorkflowProcess, Executable {
    private static final Logger log = LoggerFactory.getLogger(MockAssetInsightsWorkflowProcess.class);

    private transient Cfg cfg;

    @Reference
    private transient AssetPerformanceTracker assetPerformanceTracker;

    @Reference
    private transient AssetUsageTracker assetUsageTracker;

    @Override
    public final void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        ResourceResolver resourceResolver = null;

        resourceResolver = workflowSession.adaptTo(ResourceResolver.class);

        final WorkflowData workflowData = workItem.getWorkflowData();
        final String type = workflowData.getPayloadType();

        // Check if the payload is a path in the JCR; The other (less common) type is JCR_UUID
        if (!StringUtils.equals(type, "JCR_PATH")) {
            return;
        }
        // Get the path to the JCR resource from the payload
        final String payload = workflowData.getPayload().toString();

        try {
            processAsset(resourceResolver, metaDataMap.get("PROCESS_ARGS", DamConstants.MOUNTPOINT_ASSETS), payload);
        } catch (PersistenceException e) {
            throw new WorkflowException(e);
        }

    }

    private void processAsset(ResourceResolver resourceResolver, String scopePath, String path) throws PersistenceException {
        final Asset asset = DamUtil.resolveToAsset(resourceResolver.getResource(path));

        if (asset == null) {
            log.debug("Payload path [ {} ] does not resolve to an asset", path);
            return;
        } else if (!StringUtils.startsWith(asset.getPath(), scopePath)) {
            return;
        }  /*else if (!StringUtils.contains(asset.getMimeType(), "image")) {
            // Only images
            log.debug("Skipping [ {} ] as it is not an image [ {} ]", asset.getPath(), asset.getMimeType());
            return;
        }*/

        Resource jcrContent = asset.adaptTo(Resource.class).getChild(JcrConstants.JCR_CONTENT);
        //ModifiableValueMap mvm = jcrContent.adaptTo(ModifiableValueMap.class);
        
        /* commenting out UUID generation
        String uuid = UUID.randomUUID().toString();
        mvm.put("dam:assetID", uuid);
        mvm.put("jcr:uuid", uuid);

        log.info("Set [ {}/@dam:assetID ] to [ {} ]", jcrContent.getPath(), uuid);
        log.info("Set [ {}/@jcr:uuid ] to [ {} ]", jcrContent.getPath(), uuid);
        */
        int usage = randomInRange(cfg.minUsage(), cfg.maxUsage());

        if (usage > 0) {
            log.info("Generating random usage for [ {} ]", jcrContent.getPath());

            generatePerformance(asset);

            usage = randomInRange(cfg.minUsage(), cfg.maxUsage());
            generateUsage(asset, MockAssetUsageRecord.ASSET_USAGE_TYPE.AEM, usage);
            usage = randomInRange(cfg.minUsage(), cfg.maxUsage());
            generateUsage(asset, MockAssetUsageRecord.ASSET_USAGE_TYPE.ASSET, usage);
            usage = randomInRange(cfg.minUsage(), cfg.maxUsage());
            generateUsage(asset, MockAssetUsageRecord.ASSET_USAGE_TYPE.CAMPAIGN, usage);
            usage = randomInRange(cfg.minUsage(), cfg.maxUsage());
            generateUsage(asset, MockAssetUsageRecord.ASSET_USAGE_TYPE.SOCIAL, usage);
        }
    }

    private void generatePerformance(Asset asset) throws PersistenceException {
        int clickCount = randomInRange(cfg.minPerformanceClicks(), cfg.maxPerformanceClicks());
        int impressionCount = randomInRange(cfg.minPerformanceImpressions(), cfg.maxPerformanceImpressions()) + clickCount;
        assetPerformanceTracker.setAssetClick(asset, new Long(clickCount));
        assetPerformanceTracker.setAssetImpression(asset, new Long(impressionCount));
    }

    private void generateUsage(Asset asset, MockAssetUsageRecord.ASSET_USAGE_TYPE type, int count) {
        for (int i = 0; i < count; i++) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1 * randomInRange(0, 10));

            AssetUsageRecord usage = new MockAssetUsageRecord(type, cal);

            try {
                assetUsageTracker.recordUsage(asset, usage);
            } catch (Exception e) {
                log.error("Could not generate mock usage for [ {} ]", asset.getPath(), e);
            }
        }
    }

    private int randomInRange(int min, int max) {
        return min + new SecureRandom().nextInt((max - min) + 1);
    }


    @Activate
    protected void activate(Cfg cfg) {
        this.cfg = cfg;
    }

    @Override
    public String getName() {
        return "asset-insights";
    }


    @Override
    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();

        final Map<String, String> map = new HashMap<String, String>();

        map.put("type", "dam:Asset");
        map.put("path", "/content/dam");
        map.put("property", "jcr:content/metadata/dc:format");
        map.put("property.operation", "exists");
        map.put("orderby", "@jcr:content/jcr:lastModified");
        map.put("orderby.sort", "desc");
        map.put("p.offset", "0");
        map.put("p.limit", "-1");

        QueryUtil.findResources(resourceResolver, map).stream().forEach(r -> {
            try {
                processAsset(resourceResolver, DamConstants.MOUNTPOINT_ASSETS, r.getPath());
            } catch (PersistenceException e) {
                log.error("Could not process {}", r.getPath(), e);
            }
        });
    }


    @ObjectClassDefinition(name = "AEM Demo Utils - Asset Insights - Usage and Summary Performance Generator")
    public @interface Cfg {
        @AttributeDefinition(
                name = "Min Usage",
                description = "Sets the minimum bound for any given Usage score."
        )
        int minUsage() default 0;

        @AttributeDefinition(
                name = "Max Usage",
                description = "Sets the maximum bound for any given Usage score."
        )
        int maxUsage() default 5;

        @AttributeDefinition(
                name = "Min Performance Clicks",
                description = "Sets the minimum bound for Clicks summary score."
        )
        int minPerformanceClicks() default 0;

        @AttributeDefinition(
                name = "Max Performance Clicks",
                description = "Set the maximum bound for Clicks summary score."
        )
        int maxPerformanceClicks() default 60;

        @AttributeDefinition(
                name = "Min Performance Impressions",
                description = "Sets the minimum bound for Impressions summary score."
        )
        int minPerformanceImpressions() default 0;

        @AttributeDefinition(
                name = "Max Performance Impressions",
                description = "Set the maximum bound for Impressions summary score."
        )
        int maxPerformanceImpressions() default 120;
    }

}
