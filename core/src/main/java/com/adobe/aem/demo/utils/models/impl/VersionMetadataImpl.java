package com.adobe.aem.demo.utils.models.impl;

import com.adobe.aem.demo.utils.models.VersionMetadata;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = {SlingHttpServletRequest.class}, adapters = {VersionMetadata.class}, resourceType = {
        VersionMetadataImpl.RESOURCE_TYPE}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VersionMetadataImpl implements VersionMetadata {

    protected static final String RESOURCE_TYPE = "demo-utils/components/base";
    private static final Logger log = LoggerFactory.getLogger(VersionMetadataImpl.class);

    @Override
    public String getUtilsVersion() {
        Bundle demoUtilBundle = FrameworkUtil.getBundle(VersionMetadata.class);
        if (demoUtilBundle != null) {
            return demoUtilBundle.getVersion().toString();
        }
        return "ERROR";

    }

}