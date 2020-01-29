package com.adobe.aem.demo.utils.impl;

import com.adobe.aem.demo.utils.Versioned;
import com.day.cq.commons.Version;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class VersionUtil {

    private static final String PATH_TRACKER_RESOURCE = "/var/demo-utils";

    public static Resource getVersionTracker(ResourceResolver resourceResolver) throws RepositoryException {
        JcrUtils.getOrCreateByPath(PATH_TRACKER_RESOURCE, JcrConstants.NT_UNSTRUCTURED, resourceResolver.adaptTo(Session.class));
        return resourceResolver.getResource(PATH_TRACKER_RESOURCE);
    }

    public static final Version getVersion(Resource resource, String key) {
        return getVersion(resource.getValueMap(), key);
    }

    public static final Version getVersion(ValueMap properties, String key) {
        String versionStr = properties.get(key, "");
        Version version;

        try {
            version = Version.create(versionStr);
        } catch (Exception ex) {
            version = Version.EMPTY;
        }

        return version;
    }

    public static boolean needsUpdate(Versioned current, ValueMap applied) {
        return needsUpdate(current.getVersion(), Version.create(applied.get(current.getName(), "")));
    }

    public static boolean needsUpdate(Version current, Version applied) {

        if (applied == null || Version.EMPTY.equals(applied)) {
            // Means applied is not applied, so the current is always newer
            return true;
        }

        // if current is greater than the applied version
        return current.compareTo(applied) > 0;
    }

    public static void trackVersion(ModifiableValueMap properties, Versioned versioned) {
        properties.put(versioned.getName(), versioned.getVersion().toString());
    }
}
