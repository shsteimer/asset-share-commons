package com.adobe.aem.demo.utils.assets.assetlinkimpl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.aem.demo.utils.impl.Constants;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.adobe.granite.workflow.model.WorkflowNode;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.vault.fs.io.ImportOptions;
import org.apache.jackrabbit.vault.packaging.JcrPackage;
import org.apache.jackrabbit.vault.packaging.JcrPackageManager;
import org.apache.jackrabbit.vault.packaging.PackageException;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/adobe-asset-link",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
public class AdobeAssetLink extends AbstractExecutable {
    public static final String CONFIG_PACKAGE_PATH = "/apps/demo-utils/resources/adobe-asset-link/aem-demo-utils.asset-link-config-1.1.0.zip";
    private static Logger log = LoggerFactory.getLogger(AdobeAssetLink.class);

    @Reference
    private transient Packaging packaging;

    @Override
    public String getName() {
        return "adobe-asset-link";
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            execute(request, response);
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not create cloud service config for Adobe Asset Link", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();

        addToAdministrators(request.adaptTo(Session.class));
        
        installPackage(resourceResolver, CONFIG_PACKAGE_PATH);

        updateWorkflow(resourceResolver);

        if (resourceResolver.hasChanges()) {
            resourceResolver.commit();
        }
    }

    private void installPackage(ResourceResolver resourceResolver, String packagePath) throws RepositoryException, IOException, PackageException {
        JcrPackageManager jcrPackageManager = packaging.getPackageManager(resourceResolver.adaptTo(Session.class));
        InputStream is = resourceResolver.getResource(packagePath).adaptTo(InputStream.class);
        JcrPackage jcrPackage = jcrPackageManager.upload(is, true, false);
        jcrPackage.install(new ImportOptions());
    }


    private static void addToAdministrators(Session session) throws RepositoryException {
        if (!(session instanceof JackrabbitSession)) {
            throw new RepositoryException("The repository does not support dynamic_media_replication user");
        }
        JackrabbitSession jackrabbitSession = (JackrabbitSession) session;

        UserManager userManager = jackrabbitSession.getUserManager();
        Authorizable me = userManager.getAuthorizable(session.getUserID());
        Authorizable administrators = userManager.getAuthorizable("administrators");

        if (administrators != null && administrators.isGroup()) {
            ((Group) administrators).addMember(me);
        } else {
            log.warn("Could not find [ administrators ] user group to add user [ {} ]", session.getUserID());
        }

        session.save();
    }

    /* ************************************************************************************************************
        UPDATE WORKFLOW
    * ************************************************************************************************************/

    private void updateWorkflow(ResourceResolver resourceResolver) throws WorkflowException, PersistenceException, RepositoryException {
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);

        if (resourceResolver.getResource("/conf/global/settings/workflow/models/dam/update_asset") == null) {
            // Copy it down from libs first...
            JcrUtils.getOrCreateByPath("/conf/global/settings/workflow/models/dam",
                    JcrResourceConstants.NT_SLING_FOLDER,
                    resourceResolver.adaptTo(Session.class));

            resourceResolver.copy("/libs/settings/workflow/models/dam/update_asset",
                    "/conf/global/settings/workflow/models/dam");
        }

        if (workflowModelRequiresUpdating(resourceResolver.getResource("/var/workflow/models/dam/update_asset"))) {
            // update the model in /conf
            updateWorkflowDesignModel(resourceResolver.getResource("/conf/global/settings/workflow/models/dam/update_asset"));
            // update the model in /var
            updateWorkflowRuntimeModel(resourceResolver.getResource("/var/workflow/models/dam/update_asset"));
        }

        workflowSession.deployModel(workflowSession.getModel("/var/workflow/models/dam/update_asset"));
    }

    private boolean workflowModelRequiresUpdating(Resource workflowResource) throws WorkflowException {
        ResourceResolver resourceResolver = workflowResource.getResourceResolver();
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
        WorkflowModel model = workflowSession.getModel(workflowResource.getPath());

        for (WorkflowNode node : model.getNodes()) {
            if ("com.day.cq.dam.core.impl.process.ThumbnailProcess".equalsIgnoreCase(node.getMetaDataMap().get("PROCESS", "")) &&
                    node.getMetaDataMap().get("FPO_CREATION_ENABLED", false) == true) {
                // Already has FPO for Thumbnail Process configured... skipping
                return false;
            }
        }

        log.debug("DAM Update Asset runtime WF model requires updating FPO configuration");
        return true;
    }

    private void updateWorkflowDesignModel(Resource workflowResource) throws PersistenceException, RepositoryException {
        final Resource flow = workflowResource.getChild("jcr:content/flow");

        for (Resource step : flow.getChildren()) {
            if (step.isResourceType("dam/components/workflow/thumbnailprocess")) {
                step.getChild("metaData").adaptTo(ModifiableValueMap.class).put("FPO_CREATION_ENABLED", true);
                return;
            }
        }
    }

    private void updateWorkflowRuntimeModel(Resource workflowResource) throws WorkflowException, PersistenceException, RepositoryException {
        ResourceResolver resourceResolver = workflowResource.getResourceResolver();
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
        WorkflowModel model = workflowSession.getModel(workflowResource.getPath());

        for (WorkflowNode workflowNode : model.getNodes()) {
            if ("com.day.cq.dam.core.impl.process.ThumbnailProcess".equals(workflowNode.getMetaDataMap().get("PROCESS", String.class))) {
                workflowNode.getMetaDataMap().put("FPO_CREATION_ENABLED", "true");

                model.getMetaDataMap().put("cq:lastModified", Long.valueOf(Calendar.getInstance().getTimeInMillis()));
                model.getMetaDataMap().put("cq:lastModifiedBy", "aem-demo-utils");
                model.getMetaDataMap().put("cq:generatingPage", "/conf/global/settings/workflow/models/dam/update_asset");

                workflowSession.deployModel(model);

                log.debug("Deployed updated WF Model [ {} ] for Adobe Asset Link FPO Support", model.getId());
                return;
            }
        }

        log.warn("Could not find Thumbnail Process to update for Adobe Asset Link FPO in WF Model [ {} ]", model.getId());
    }
}
