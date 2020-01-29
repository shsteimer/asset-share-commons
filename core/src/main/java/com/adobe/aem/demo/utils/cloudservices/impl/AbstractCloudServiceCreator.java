package com.adobe.aem.demo.utils.cloudservices.impl;

import com.adobe.aem.demo.utils.cloudservices.CloudServiceCreator;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

public abstract class AbstractCloudServiceCreator extends AbstractExecutable implements CloudServiceCreator {
    private static final Logger log = LoggerFactory.getLogger(AbstractCloudServiceCreator.class);

    public void execute(ResourceResolver resourceResolver,
                        CryptoSupport cryptoSupport,
                        String parentPagePath,
                        String pageName,
                        String template,
                        String title,
                        Map<String, Object> properties) throws WCMException, ServletException, IOException, CryptoException {

        final Page page = createPage(resourceResolver,
                parentPagePath,
                pageName,
                template,
                title);

        final ModifiableValueMap mvm = page.getContentResource().adaptTo(ModifiableValueMap.class);

        mvm.putAll(getEncrypted(cryptoSupport, new ValueMapDecorator(properties)));

        resourceResolver.commit();
    }

    public Page createPage(ResourceResolver resourceResolver, String parentPath, String pageName, String template, String title) throws WCMException, PersistenceException {

        final Resource pageResource = resourceResolver.getResource(parentPath + "/" + pageName);
        if (pageResource != null) {
            resourceResolver.delete(pageResource);
            resourceResolver.commit();
        }

        final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        final Page page = pageManager.create(parentPath, pageName, template, title, true);

        log.info("Created cloud services page at [ {} ]", page.getPath());

        return page;
    }


    private ValueMap getEncrypted(CryptoSupport cryptoSupport, ValueMap properties) throws CryptoException {
        List<String> encryptedProperties = new ArrayList<>();

        for (String key : properties.keySet()) {
            if (StringUtils.endsWith(key, "@Encrypted")) {
                encryptedProperties.add(StringUtils.substringBefore(key, "@"));
            }
        }

        for (String key : properties.keySet()) {
            if (encryptedProperties.contains(key) && StringUtils.isNotBlank(properties.get(key, String.class))) {
                if (!cryptoSupport.isProtected(properties.get(key, ""))) {
                    properties.put(key, cryptoSupport.protect(properties.get(key, "")));

                }
            }
        }

        // Copy the set so we can remove things safel
        final Set<String> keys = new HashSet<>(properties.keySet());
        for (String key : keys) {
            if (StringUtils.contains(key, "@")) {
                properties.remove(key);
            }
        }

        return properties;
    }
}
