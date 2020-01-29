package com.adobe.aem.demo.utils.assets.insights.impl;

import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.dam.performance.api.AssetPerformanceConfigProvider;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.webservicesupport.Configuration;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import java.util.Date;
import java.util.Iterator;

@Component(
        property = {
                "service.ranking:Integer=1000000",
        },
        service = AssetPerformanceConfigProvider.class
)
public class ForcedAssetPerformanceConfigProvider implements AssetPerformanceConfigProvider {
    @Override
    public String getSiteCatalystCloudServiceConfigPath(Resource resource) {
        return null;
    }

    @Override
    public String getSiteCatalystCloudServiceConfigPath(ResourceResolver resolver) {
        return "/etc/cloudservices/sitecatalyst/assetinsights";
    }

    @Override
    public String getAssetIdImpressionListVar(ResourceResolver resolver) {
        return "none";
    }

    @Override
    public String getAssetIdClickEVar(ResourceResolver resolver) {
        return "none";
    }

    @Override
    public String getAssetImpressionSuccessEvent(ResourceResolver resolver) {
        return "none";
    }

    @Override
    public String getAssetClickSuccessEvent(ResourceResolver resolver) {
        return "none";
    }

    @Override
    public String getSCConfiguredReportSuite(Configuration scConfiguration) {
        return "/etc/cloudservices/sitecatalyst/assetinsights";
    }

    @Override
    public String getSCConfiguredVisitorNamespace(Configuration scConfiguration) {
        return "none";
    }

    @Override
    public String getSCConfiguredTrackingServer(SlingHttpServletRequest request, Configuration scConfiguration) {
        return "none";
    }

    @Override
    public Configuration getSCConfiguration(ResourceResolver resolver) {
        return new Configuration() {
            private static final String DUMMY_STRING = "AEM DEMO - Force Assets Insights";

            @Override
            public String getTitle() {
                return DUMMY_STRING;
            }

            @Override
            public String getDescription() {
                return DUMMY_STRING;
            }

            @Override
            public String getName() {
                return DUMMY_STRING;
            }

            @Override
            public String getPath() {
                return "/etc/cloudservices/sitecatalyst/assetinsights";
            }

            @Override
            public Long getLastModified() {
                return (new Date()).getTime();
            }

            @Override
            public String getIconPath() {
                return "/dev/null";
            }

            @Override
            public String getThumbnailPath() {
                return "/dev/null";
            }

            @Override
            public Template getTemplate() {
                return null;
            }

            @Override
            public Resource getParent() {
                return null;
            }

            @Override
            public Resource getResource() {
                return null;
            }

            @Override
            public Resource getContentResource() {
                return null;
            }

            @Override
            public InheritanceValueMap getProperties() {
                return null;
            }

            @Override
            public <T> T get(String s, T t) {
                return null;
            }

            @Override
            public <T> T getInherited(String s, T t) {
                return null;
            }

            @Override
            public Iterator<Configuration> listChildren() {
                return null;
            }
        };
    }
}
