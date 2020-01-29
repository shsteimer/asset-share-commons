package com.adobe.aem.demo.utils.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanerUtil {
    private static final Logger log = LoggerFactory.getLogger(CleanerUtil.class);

    public static void remove(ResourceResolver resourceResolver, String path) throws PersistenceException {
        Resource resource = resourceResolver.getResource(path);
        if (resource != null) {
            resourceResolver.delete(resource);
            resourceResolver.commit();
            resourceResolver.refresh();
        }
    }

    public static void removeChildren(final ResourceResolver resourceResolver, String path) throws PersistenceException {
        Resource resource = resourceResolver.getResource(path);

        if (resource != null) {
            resource.getChildren().forEach((child) -> {
                // configuration is the var tasks bucket
                if (!ArrayUtils.contains(new String[]{"configuration", "rep:policy", "jcr:content"}, child.getName())) {
                    try {
                        resourceResolver.delete(child);
                    } catch (PersistenceException e) {
                        log.error("Could not remove resource [ {} ] as part of clean-up.", child.getPath(), e);
                    }
                }
            });
        }

        if (resourceResolver.hasChanges()) {
            resourceResolver.commit();
            resourceResolver.refresh();
        }
    }
}
