package com.adobe.aem.demo.utils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

public interface Executable {
    String ACTION_INSTALL = "install";

    void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception;

    boolean accepts(ResourceResolver resourceResolver, ValueMap params);

    String getName();
}

