package com.adobe.aem.demo.utils.assets.remotedam.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.aem.demo.utils.impl.CleanerUtil;
import com.adobe.aem.demo.utils.impl.Constants;
import com.adobe.granite.crypto.CryptoSupport;
import com.adobe.granite.keystore.KeyStoreService;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/remote-dam",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
public class InstallRemoteDAM extends AbstractExecutable implements Executable {
    private static final String CONF_REMOTE_DAM_PATH = "/conf/global/settings/dam/remotedam";
    private static final String RESOURCE_REMOTE_DAM_PATH = "/apps/demo-utils/resources/remote-dam/configuration";
    private static final String RESOURCE_LAUNCHER_PATH = "/apps/demo-utils/resources/remote-dam/launcher/config";
    private static final String LAUNCHER_CFG_PATH = "/conf/global/settings/workflow/launcher/config";
    private static final String[] REMOTE_DAM_LAUNCHERS = {"dam_xmp_writeback", "update_asset_create", "update_asset_create_without_DM", "update_asset_mod", "update_asset_mod_reupload", "update_asset_mod_without_DM", "update_asset_mod_without_DM_reupload"};
    private static Logger log = LoggerFactory.getLogger(InstallRemoteDAM.class);
    @Reference
    private KeyStoreService keyStoreService;
    @Reference
    private CryptoSupport cryptoSupport;
    @Reference
    private Packaging packaging;
    @Reference(
            target = "(sling.servlet.resourceTypes=dam/remoteassets/connectiontest)",
            cardinality = ReferenceCardinality.OPTIONAL)
    private Servlet remotedamConnectionTestServlet;

    @Override
    public String getName() {
        return "remote-dam";
    }

    @Override
    public void execute(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws Exception {
        doGet(request, response);
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        if (remotedamConnectionTestServlet == null) {
            throw new ServletException("Remote DAM requires AEM 6.5 at a MINIMUM!");
        }

        final ResourceResolver resourceResolver = request.getResourceResolver();

        try {
            log.info("Configuring Remote DAM");
            executeRemoteDamSetup(resourceResolver);
            response.sendRedirect(Constants.SUCCESS_URL);

        } catch (Exception e) {
            log.error("Could not configure Remote DAM", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    public void executeRemoteDamSetup(ResourceResolver resourceResolver) throws Exception {
        // Clean existing Remote DAM configuration
        CleanerUtil.remove(resourceResolver, CONF_REMOTE_DAM_PATH);

        // Ensure remote dam config exists
        JcrUtils.getOrCreateByPath(CONF_REMOTE_DAM_PATH, JcrResourceConstants.NT_SLING_FOLDER, JcrResourceConstants.NT_SLING_FOLDER, resourceResolver.adaptTo(Session.class), true);
        //copy remote DAM config
        resourceResolver.copy(RESOURCE_REMOTE_DAM_PATH, CONF_REMOTE_DAM_PATH);

        //update workflow launchers
        for (String wfLauncher : REMOTE_DAM_LAUNCHERS) {
            CleanerUtil.remove(resourceResolver, LAUNCHER_CFG_PATH + "/" + wfLauncher);
            resourceResolver.copy(RESOURCE_LAUNCHER_PATH + "/" + wfLauncher, LAUNCHER_CFG_PATH);
        }

        resourceResolver.commit();
    }
}