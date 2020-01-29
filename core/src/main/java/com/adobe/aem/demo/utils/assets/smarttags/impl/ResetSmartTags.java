package com.adobe.aem.demo.utils.assets.smarttags.impl;

import com.adobe.aem.demo.utils.impl.Constants;
import com.adobe.aem.demo.utils.impl.QueryUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.management.DynamicMBean;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = {Servlet.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/smart-tags",
                "sling.servlet.selectors=reset",
                "sling.servlet.extensions=html"
        }
)
public class ResetSmartTags extends SlingSafeMethodsServlet {
    private static Logger log = LoggerFactory.getLogger(ResetSmartTags.class);

    @Reference(target = "(jmx.objectname=com.day.cq.dam.similaritysearch.internal.impl:type=similaritysearch)")
    private DynamicMBean mbean;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            if (request.getParameter("all") != null) {
                resetTraining();
                resetLastTransferredForTraining(request.getResourceResolver());
                resetLastTransferredForTagging(request.getResourceResolver());
                if (request.getResourceResolver().hasChanges()) {
                    request.getResourceResolver().commit();
                }
                response.sendRedirect(Constants.SUCCESS_URL);
            } else if (request.getParameter("training") != null) {
                resetLastTransferredForTraining(request.getResourceResolver());
                if (request.getResourceResolver().hasChanges()) {
                    request.getResourceResolver().commit();
                }
                response.sendRedirect(Constants.SUCCESS_URL);
            } else if (request.getParameter("tagging") != null) {
                resetLastTransferredForTagging(request.getResourceResolver());
                if (request.getResourceResolver().hasChanges()) {
                    request.getResourceResolver().commit();
                }
                response.sendRedirect(Constants.SUCCESS_URL);
            } else {
                log.error("Invalid command for resetting Smart Tags");

                response.sendRedirect(Constants.FAILURE_URL);
            }
        } catch (Exception e) {
            log.error("Error occurred while resetting Smart Tags", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    private void resetTraining() throws MBeanException, ReflectionException {
        try {
            mbean.invoke("resetTraining", new Object[]{}, new String[]{});
            log.debug("Issued training reset request for this Smart Tags tenant");
        } catch (MBeanException e) {
            log.error("Could not invoke Smart Tag Training reset", e);
            throw e;
        } catch (ReflectionException e) {
            log.error("Could not invoke Smart Tag Training reset", e);
            throw e;
        }
    }

    private void resetLastTransferredForTraining(ResourceResolver resourceResolver) throws RepositoryException, PersistenceException {

        final Map<String, String> map = new HashMap<String, String>();

        map.put("type", "dam:Asset");
        map.put("path", "/content/dam");
        map.put("property", "jcr:content/metadata/lastTransferredForTraining");
        map.put("property.operation", "exists");
        map.put("p.offset", "0");
        map.put("p.limit", "-1");

        QueryUtil.findResources(resourceResolver, map).stream().forEach(r -> {
            log.debug("Clearing [ jcr:content/metadata/lastTransferredForTraining ] on  [ {} ]", r.getPath());
            // This must exist beause our findResources mandates it does...
            Resource metadata = r.getChild("jcr:content/metadata");
            ModifiableValueMap properties = metadata.adaptTo(ModifiableValueMap.class);

            properties.remove("lastTransferredForTraining");
            log.debug("Removed property [ jcr:content/metadata/lastTransferredForTraining ] from asset [ {} ]", r.getPath());

        });
    }

    private void resetLastTransferredForTagging(ResourceResolver resourceResolver) throws RepositoryException, PersistenceException {
        final Map<String, String> map = new HashMap<String, String>();

        map.put("type", "dam:Asset");
        map.put("path", "/content/dam");
        map.put("property", "jcr:content/metadata/lastTransferredForTagging");
        map.put("property.operation", "exists");
        map.put("p.offset", "0");
        map.put("p.limit", "-1");

        QueryUtil.findResources(resourceResolver, map).stream().forEach(r -> {
            log.debug("Clearing [ jcr:content/metadata/lastTransferredForTagging ] on  [ {} ]", r.getPath());

            Resource metadata = r.getChild("jcr:content/metadata");
            ModifiableValueMap properties = metadata.adaptTo(ModifiableValueMap.class);

            properties.remove("lastTransferredForTagging");
            log.debug("Removed property [ jcr:content/metadata/lastTransferredForTagging ] from asset [ {} ]", r.getPath());
        });
    }
}