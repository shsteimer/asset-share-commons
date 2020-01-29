package com.adobe.aem.demo.utils.assets.smarttranslationsearch.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.aem.demo.utils.impl.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/smart-translation-search",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
public class SmartTranslationSearch extends AbstractExecutable {
    private static final Logger log = LoggerFactory.getLogger(SmartTranslationSearch.class);

    private static final String BUNDLE_SYMBOLIC_NAME = "org.apache.jackrabbit.oak-search-mt";
    private static final String INDEX_PATH = "/oak:index/damAssetLucene";

    private BundleContext bundleContext;

    @Override
    public String getName() {
        return "smart-translation-search";
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            execute(request, response);
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not automatically config for Smart Translation Search", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    @Override
    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();

        updateOakIndex(resourceResolver);
        restartBundle();
    }

    private final void updateOakIndex(ResourceResolver resourceResolver) throws Exception {
        final Resource resource = resourceResolver.getResource(INDEX_PATH);

        if (resource == null) {
            throw new Exception("Unable to get resource at " + INDEX_PATH);
        }

        final ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);

        if (properties == null) {
            throw new Exception("Unable to get a modifiable value map for " + INDEX_PATH);
        }

        properties.put("reindex", true);

        resourceResolver.commit();

        log.info("Updated {}/predicateTags for Smart Translation Search", INDEX_PATH);
    }


    private final void restartBundle() throws BundleException {
        for (Bundle bundle : bundleContext.getBundles()) {
            if (bundle != null && BUNDLE_SYMBOLIC_NAME.equals(bundle.getSymbolicName())) {
                if (Bundle.UNINSTALLED != bundle.getState()) {
                    bundle.stop();
                }
                bundle.start();
                break;
            }
        }
    }

    @Activate
    public void activate(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

}
