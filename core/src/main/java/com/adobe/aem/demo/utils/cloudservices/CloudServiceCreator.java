package com.adobe.aem.demo.utils.cloudservices;

import com.adobe.granite.crypto.CryptoSupport;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Map;

public interface CloudServiceCreator {
    void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception;

    void execute(ResourceResolver resourceResolver,
                 CryptoSupport cryptoSupport,
                 String parentPagePath,
                 String pageName,
                 String template,
                 String title,
                 Map<String, Object> requestParams) throws Exception;
}
