package com.adobe.aem.demo.utils.impl;

import com.adobe.aem.demo.utils.Executable;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

public abstract class AbstractExecutable extends SlingAllMethodsServlet implements Executable {
    public abstract String getName();

    public abstract void execute(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws Exception;

    public boolean accepts(ResourceResolver resourceResolver, ValueMap params) {
        return params.get(getName(), String.class) != null;
    }
}
