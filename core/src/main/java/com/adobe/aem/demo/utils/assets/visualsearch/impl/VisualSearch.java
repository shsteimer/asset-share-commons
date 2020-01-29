package com.adobe.aem.demo.utils.assets.visualsearch.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.assets.smarttranslationsearch.impl.SmartTranslationSearch;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.aem.demo.utils.impl.Constants;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/visual-search",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
public class VisualSearch extends AbstractExecutable {

    private static final Logger log = LoggerFactory.getLogger(SmartTranslationSearch.class);

    private static final String LUCENE_INDEX_PATH = "/oak:index/lucene";
    private static final String DAM_ASSET_LUCENE_INDEX_PATH = "/oak:index/damAssetLucene";

    @Override
    public String getName() {
        return "visual-search";
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            execute(request, response);
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not automatically config for Visual Search", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    @Override
    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();

        // Automates this setup: https://helpx.adobe.com/experience-manager/6-5/assets/using/search-assets.html#configvisualsearch
        updateDamAssetLuceneOakIndex(resourceResolver);
        updateLuceneOakIndex(resourceResolver);
        syncAssetsSearchForm(resourceResolver);

        if (resourceResolver.hasChanges()) {
            resourceResolver.commit();
        }
    }

    private final void updateLuceneOakIndex(ResourceResolver resourceResolver) throws Exception {
        final Resource resource = resourceResolver.getResource(LUCENE_INDEX_PATH);

        if (resource == null) {
            throw new Exception("Unable to get resource at " + LUCENE_INDEX_PATH);
        }

        final ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);

        if (properties == null) {
            throw new Exception("Unable to get a modifiable value map for " + LUCENE_INDEX_PATH);
        }

        properties.put("costPerEntry", 10D);
        properties.put("costPerExecution", 2D);
        properties.put("refresh", true);

        log.info("Updated {} for AEM Assets Visual Similarity Search", LUCENE_INDEX_PATH);
    }

    private final void updateDamAssetLuceneOakIndex(ResourceResolver resourceResolver) throws Exception {

        /** Create new "imageFeatures" index entry **/
        final Resource indexPropertiesResource = resourceResolver.getResource(DAM_ASSET_LUCENE_INDEX_PATH + "/indexRules/dam:Asset/properties");

        if (indexPropertiesResource == null) {
            throw new Exception("Unable to get resource at " + DAM_ASSET_LUCENE_INDEX_PATH + "/indexRules/dam:Asset/properties");
        }

        final Map<String, Object> properties = new HashMap<>();
        properties.put("jcr:primaryType", JcrConstants.NT_UNSTRUCTURED);
        properties.put("name", "jcr:content/metadata/imageFeatures/haystack0");
        properties.put("nodeScopeIndex", true);
        properties.put("propertyIndex", true);
        properties.put("useInSimilarity", true);

        Resource imageFeaturesResource = indexPropertiesResource.getChild("imageFeatures");
        if (imageFeaturesResource == null) {
            resourceResolver.create(indexPropertiesResource, "imageFeatures", properties);
        } else {
            ModifiableValueMap mvm = imageFeaturesResource.adaptTo(ModifiableValueMap.class);
            mvm.putAll(properties);
        }

        /** Updated predicatedTags index **/
        final Resource predictedTagsResource = resourceResolver.getResource(DAM_ASSET_LUCENE_INDEX_PATH + "/indexRules/dam:Asset/properties/predictedTags");
        if (predictedTagsResource == null) {
            throw new Exception("Unable to get resource at " + DAM_ASSET_LUCENE_INDEX_PATH + "/indexRules/dam:Asset/properties/predictedTags");
        }
        ModifiableValueMap mvm = predictedTagsResource.adaptTo(ModifiableValueMap.class);
        if (mvm == null) {
            throw new Exception("Unable to get a modifiable value map for " + predictedTagsResource.getPath());
        }
        mvm.put("similarityTags", true);


        /** reindex damAssetLucene index **/
        final Resource damAssetLuceneIndexResource = resourceResolver.getResource(DAM_ASSET_LUCENE_INDEX_PATH);
        if (damAssetLuceneIndexResource == null) {
            throw new Exception("Unable to get resource at " + DAM_ASSET_LUCENE_INDEX_PATH);
        }
        mvm = damAssetLuceneIndexResource.adaptTo(ModifiableValueMap.class);
        if (mvm == null) {
            throw new Exception("Unable to get a modifiable value map for " + damAssetLuceneIndexResource.getPath());
        }
        mvm.put("reindex", true);

        log.info("Updated {} for AEM Assets Visual Similarity Search", DAM_ASSET_LUCENE_INDEX_PATH);
    }

    private void syncAssetsSearchForm(ResourceResolver resourceResolver) throws PersistenceException {
        if (resourceResolver.getResource("/conf/global/settings/dam/search/facets/assets/jcr:content/items") == null) {
            return;
        }
        if (resourceResolver.getResource("/conf/global/settings/dam/search/facets/assets/jcr:content/items/similaritysearch") != null) {
            return;
        }

        resourceResolver.copy("/libs/settings/dam/search/facets/assets/jcr:content/items/similaritysearch",
                "/conf/global/settings/dam/search/facets/assets/jcr:content/items");

        log.info("Updated AEM Assets Search for with Similarity Search");
    }
}
