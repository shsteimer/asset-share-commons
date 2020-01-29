package com.adobe.aem.demo.utils.assets.smarttags.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.cloudservices.impl.AbstractCloudServiceCreator;
import com.adobe.aem.demo.utils.impl.CleanerUtil;
import com.adobe.aem.demo.utils.impl.Constants;
import com.adobe.granite.crypto.CryptoSupport;
import com.adobe.granite.keystore.KeyStoreNotInitialisedException;
import com.adobe.granite.keystore.KeyStoreService;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.model.WorkflowModel;
import com.adobe.granite.workflow.model.WorkflowNode;
import com.adobe.granite.workflow.model.WorkflowTransition;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.management.DynamicMBean;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularDataSupport;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/smart-tags",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
@SuppressWarnings("squid:S2068")
public class InstallSmartTags extends AbstractCloudServiceCreator implements Executable {
    public static final String CLOUDSERVICES_FOLDER = "/etc/cloudservices/smarttagging";
    public static final String CLOUDSERVICES_PAGE_NAME = "demo-utils";
    public static final String TEMPLATE = "/libs/dam/templates/smarttagging";
    public static final String TITLE = "Smart Tags";
    public static final String SERVICE_USER_ID = "dam-update-service";
    private static final String PATH_CERTS = "/apps/demo-utils/resources/smart-tags/";
    private static final String SMART_TAGS_WORKFLOW_PROCESS = "com.day.cq.dam.similaritysearch.internal.workflow.process.AutoTagAssetProcess";
    private static Logger log = LoggerFactory.getLogger(InstallSmartTags.class);
    @Reference
    private KeyStoreService keyStoreService;

    @Reference
    private CryptoSupport cryptoSupport;

    @Reference(target = "(jmx.objectname=com.day.cq.dam.similaritysearch.internal.impl:type=similaritysearch)")
    private DynamicMBean mbean;

    @Override
    public String getName() {
        return "smart-tags";
    }

    private Map<String, Object> getParams(String apiKey, String techAcctId, String orgId, String clientSecret) {
        final Map<String, Object> params = new HashMap<>();

        params.put("serviceUrl", "https://mc.adobe.io/marketingcloud/smartcontent");
        params.put("authServerUrl", "https://ims-na1.adobelogin.com");
        params.put("apiKey", apiKey);
        params.put("techAcctId", techAcctId);
        params.put("orgId", orgId);
        params.put("clientSecret", clientSecret);
        params.put("clientSecret@Encrypted", "");

        return params;
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            execute(request, response);
            validate();
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not create cloud service config for Smart Tags", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();
        final ValueMap params = new ValueMapDecorator(request.getRequestParameterMap().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> (Object) entry.getValue())));

        //setupTenant(resourceResolver);
        removeExisting(resourceResolver);

        Map config;
        String region = params.get("region", String.class);

        if (Constants.APAC.equalsIgnoreCase(region)) {
            region = Constants.APAC;
            config = getParams(
                    "b6a5e3672e8749ce992b1a369cdacf6d",
                    "43FF19775A6997270A495C95@techacct.adobe.com",
                    "7B493FE759719AAB0A495EEC@AdobeOrg",
                    "a86f6617-80e1-4a3b-8e5d-6fac8e1bb121"
            );
        } else if (Constants.EMEA.equalsIgnoreCase(region)) {
            region = Constants.EMEA;
            config = getParams(
                    "142648eb1a874352837882f038a13b2a",
                    "B24ED851592BBDB00A495D25@techacct.adobe.com",
                    "CBAFF3C959280C4B0A495E7B@AdobeOrg",
                    "8bd0f8de-bca9-4998-ae98-04b922c73bbc"
            );
        } else {
            region = Constants.NA;
            config = getParams(
                    "691f65f597434342be3ec16052380b04",
                    "CA092896592BBD490A495D28@techacct.adobe.com",
                    "218BFF5659280AF50A495D22@AdobeOrg",
                    "5cd48ae9-a51d-42c8-9aa7-ceb8b3bd7094"
            );
        }

        try {
            createKeyStore(resourceResolver, region);
            updateWorkflow(resourceResolver);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        execute(resourceResolver,
                cryptoSupport,
                CLOUDSERVICES_FOLDER,
                CLOUDSERVICES_PAGE_NAME,
                TEMPLATE,
                TITLE,
                config
        );

        if (resourceResolver.hasChanges()) {
            resourceResolver.commit();
        }
    }

    /* ************************************************************************************************************
       ISOLATE TENANT
    * ************************************************************************************************************/
    @Deprecated
    // TODO Cannot write to /apps at runtime; this is for Training anyhow so doesnt hurt
    private void setupTenant(ResourceResolver resourceResolver) throws PersistenceException, InterruptedException {
        Resource resource = resourceResolver.getResource("/apps/demo-utils/config.author/com.day.cq.dam.similaritysearch.internal.impl.SimilaritySearchServiceImpl");

        ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
        properties.put("training.namespace", System.currentTimeMillis() + "-");

        resourceResolver.commit();
    }


    /* ************************************************************************************************************
        CLEAN-UP AND PREP
    * ************************************************************************************************************/

    private void removeExisting(ResourceResolver resourceResolver) throws PersistenceException {
        CleanerUtil.removeChildren(resourceResolver, CLOUDSERVICES_FOLDER);
        CleanerUtil.remove(resourceResolver, "/home/users/system/dam/dam-update-service/oauth");
    }

    /* ************************************************************************************************************
        UPDATE KEYSTORE
    * ************************************************************************************************************/


    private void createKeyStore(ResourceResolver resourceResolver, String region) throws Exception {
        String alias = "similaritysearch";
        String password = "test123";
        String keystorePassword = "demo";

        if (!keyStoreService.keyStoreExists(resourceResolver, SERVICE_USER_ID)) {
            keyStoreService.createKeyStore(resourceResolver, SERVICE_USER_ID, keystorePassword.toCharArray());
            log.info("Created keystore for [ {} ]", SERVICE_USER_ID);
        } else {
            try {
                keyStoreService.changeKeyStorePassword(resourceResolver, SERVICE_USER_ID, keystorePassword.toCharArray(), keystorePassword.toCharArray());
            } catch (SecurityException e) {
                CleanerUtil.remove(resourceResolver, "/home/users/system/dam/dam-update-service/keystore");
                keyStoreService.createKeyStore(resourceResolver, SERVICE_USER_ID, keystorePassword.toCharArray());
            }
        }

        final String certName = StringUtils.lowerCase("smart-tags-" + region + "-adobe-io.p12");
        final InputStream ksStream = resourceResolver.getResource(PATH_CERTS + certName).adaptTo(InputStream.class);
        final String userId = getUserId(resourceResolver);

        final KeyStore inputKeyStore = KeyStore.getInstance("pkcs12");
        inputKeyStore.load(ksStream, password.toCharArray());
        final KeyStore.Entry entry = inputKeyStore.getEntry(alias, new KeyStore.PasswordProtection(password.toCharArray()));
        final KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) entry;

        removeKeyStoreEntry(resourceResolver, alias, userId);

        keyStoreService.addKeyStoreKeyEntry(resourceResolver, userId, alias, pkEntry.getPrivateKey(), pkEntry.getCertificateChain());

        log.info("Updated keystore for [ {} ]", SERVICE_USER_ID);
    }

    private void removeKeyStoreEntry(ResourceResolver resourceResolver, String alias, String userId) throws IOException {
        KeyStore store = null;
        try {
            store = keyStoreService.getKeyStore(resourceResolver, userId);
        } catch (KeyStoreNotInitialisedException e) {
            log.error("Unable to perform remove alias operation because the store was not initialised.");
        }
        if (store != null) {
            try {
                if (store.containsAlias(alias)) {
                    store.deleteEntry(alias);
                }
            } catch (KeyStoreException e) {
                throw new IOException(e);
            }
        }
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
            resourceResolver.commit();
        }

        if (workflowModelRequiresUpdating(resourceResolver.getResource("/var/workflow/models/dam/update_asset"))) {
            // update the model in /conf
            updateWorkflowDesignModel(resourceResolver.getResource("/conf/global/settings/workflow/models/dam/update_asset"));
            // update the model in /var
            updateWorkflowRuntimeModel(resourceResolver.getResource("/var/workflow/models/dam/update_asset"));
        }

        workflowSession.deployModel(workflowSession.getModel("/var/workflow/models/dam/update_asset"));
    }

    private void updateWorkflowDesignModel(Resource workflowResource) throws PersistenceException, RepositoryException {
        ResourceResolver resourceResolver = workflowResource.getResourceResolver();

        final Resource flow = workflowResource.getChild("jcr:content/flow");

        for (Resource step : flow.getChildren()) {
            // Already has Smart Tags added... skipping
            if (step.isResourceType("dam/components/workflow/autotagassetprocess")) {
                return;
            }
        }

        final Resource step = resourceResolver.create(flow, "autotagassetprocess",
                ImmutableMap.<String, Object>builder().put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED).build());
        step.adaptTo(ModifiableValueMap.class).put("jcr:title", "Smart Tags");
        step.adaptTo(ModifiableValueMap.class).put("jcr:description", "Smart Tags workflow step automatically created by AEM Demo Utils.");
        step.adaptTo(ModifiableValueMap.class).put("sling:resourceType", "dam/components/workflow/autotagassetprocess");

        final Resource stepMetadata = resourceResolver.create(step, "metaData",
                ImmutableMap.<String, Object>builder().put(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.NT_UNSTRUCTURED).build());
        stepMetadata.adaptTo(ModifiableValueMap.class).put("PROCESS", SMART_TAGS_WORKFLOW_PROCESS);
        stepMetadata.adaptTo(ModifiableValueMap.class).put("PROCESS_AUTO_ADVANCE", true);
        stepMetadata.adaptTo(ModifiableValueMap.class).put("IGNORE_SMART_TAG_FLAG", true);
        stepMetadata.adaptTo(ModifiableValueMap.class).put("IGNORE_ERRORS", true);

        final Node flowNode = flow.adaptTo(Node.class);
        flowNode.orderBefore("autotagassetprocess", "damupdateassetworkflowcompletedprocess");
    }

    private void updateWorkflowRuntimeModel(Resource workflowResource) throws WorkflowException, PersistenceException, RepositoryException {
        ResourceResolver resourceResolver = workflowResource.getResourceResolver();
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
        WorkflowModel model = workflowSession.getModel(workflowResource.getPath());

        WorkflowNode smartTagsNode = model.createNode("Smart Tags", "PROCESS", "Smart Tags workflow step automatically created by AEM Demo Utils.");

        smartTagsNode.getMetaDataMap().put("PROCESS", "com.day.cq.dam.similaritysearch.internal.workflow.process.AutoTagAssetProcess");
        smartTagsNode.getMetaDataMap().put("PROCESS_AUTO_ADVANCE", true);
        smartTagsNode.getMetaDataMap().put("IGNORE_SMART_TAG_FLAG", true);
        smartTagsNode.getMetaDataMap().put("IGNORE_ERRORS", true);

        WorkflowNode afterNode = model.getEndNode().getIncomingTransitions().get(0).getFrom();
        WorkflowNode beforeNode = afterNode.getIncomingTransitions().get(0).getFrom();

        WorkflowTransition beforeToSmartTagsTransition = beforeNode.getTransitions().get(0);

        beforeToSmartTagsTransition.setTo(smartTagsNode);
        model.createTransition(smartTagsNode, afterNode, null);


        long now = Calendar.getInstance().getTimeInMillis();
        model.getMetaDataMap().put("cq:lastModified", Long.valueOf(now));
        model.getMetaDataMap().put("cq:lastModifiedBy", "aem-demo-utils");
        model.getMetaDataMap().put("cq:generatingPage", "/conf/global/settings/workflow/models/dam/update_asset");

        workflowSession.deployModel(model);
        log.debug("Deployed updated WF Model [ {} ]", model.getId());
    }

    private boolean workflowModelRequiresUpdating(Resource workflowResource) throws WorkflowException {
        ResourceResolver resourceResolver = workflowResource.getResourceResolver();
        WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);
        WorkflowModel model = workflowSession.getModel(workflowResource.getPath());

        for (WorkflowNode node : model.getNodes()) {
            if ("com.day.cq.dam.similaritysearch.internal.workflow.process.AutoTagAssetProcess".equalsIgnoreCase(node.getMetaDataMap().get("PROCESS", ""))) {
                // Already has Smart Tags added... skipping
                return false;
            }
        }

        log.debug("DAM Update Asset runtime WF model requires Smart Tags step");
        return true;
    }

    /* ************************************************************************************************************
        Validate Set up
    * ************************************************************************************************************/
    private void validate() throws Exception {

        boolean valid = true;
        int count = 20;
        while (count-- > 0) {
            Thread.sleep(1000);

            Object result = mbean.invoke("validateConfigs", new Object[]{}, new String[]{});
            TabularDataSupport table = (TabularDataSupport) result;
            for (Object value : table.values()) {
                CompositeData compositeData = (CompositeData) value;
                String status = (String) compositeData.get("Status");
                if (!"passed".equalsIgnoreCase(status)) {
                    valid = false;
                }
            }

            if (valid) {
                break;
            }
        }

        if (!valid) {
            throw new Exception("Could not validate the configuration via JMX; try again.");
        }
    }

    private String getUserId(ResourceResolver resourceResolver) throws RepositoryException {
        return ((JackrabbitSession) resourceResolver.adaptTo(Session.class)).getUserManager().getAuthorizable(SERVICE_USER_ID).getID();
    }
}