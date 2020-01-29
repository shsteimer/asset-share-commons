package com.adobe.aem.demo.utils.assets.adobestock.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.*;
import com.adobe.granite.crypto.CryptoSupport;
import com.adobe.granite.keystore.KeyStoreNotInitialisedException;
import com.adobe.granite.keystore.KeyStoreService;
import com.adobe.granite.security.user.UserProperties;
import com.adobe.granite.security.user.UserPropertiesManager;
import com.adobe.granite.security.user.UserPropertiesService;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.vault.fs.io.ImportOptions;
import org.apache.jackrabbit.vault.packaging.JcrPackage;
import org.apache.jackrabbit.vault.packaging.JcrPackageManager;
import org.apache.jackrabbit.vault.packaging.PackageException;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(service = {Servlet.class, Executable.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/adobe-stock",
                "sling.servlet.selectors=install",
                "sling.servlet.extensions=html"
        }
)
@SuppressWarnings("squid:S2068")
public class InstallAdobeStock extends AbstractExecutable implements Executable {
    public static final String SERVICE_USER_ID = "stock-imsconfig-service";
    private static final String PATH_CERTS = "/apps/demo-utils/resources/adobe-stock/";
    private static Logger log = LoggerFactory.getLogger(InstallAdobeStock.class);
    @Reference
    private transient KeyStoreService keyStoreService;

    @Reference
    private transient CryptoSupport cryptoSupport;

    @Reference
    private transient Packaging packaging;


    @Reference(target = "(sling.servlet.resourceTypes=cq/adobeims-configuration/components/admin/datasources/imsconfigurations/update)")
    private transient Servlet createImsConfigurationServlet;

    @Reference(target = "(sling.servlet.resourceTypes=cq/adobeims-configuration/components/admin/datasources/imsconfigurations/delete)")
    private transient Servlet deleteImsConfigurationsServlet;

    @Reference(target = "(sling.servlet.resourceTypes=/apps/stock/configurations)")
    private transient Servlet createCloudServiceServlet;

    @Reference(target = "(sling.servlet.resourceTypes=cq/stock-integration/components/admin/configurations/delete)")
    private transient Servlet deleteCloudServiceServlet;


    @Override
    public String getName() {
        return "adobe-stock";
    }

    @Override
    public void execute(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws Exception {
        doGet(request, response);
    }

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        if (createCloudServiceServlet == null) {
            throw new ServletException("Adobe Stock create cloud service cannot be found.");
        }

        if (deleteCloudServiceServlet == null) {
            throw new ServletException("Adobe Stock delete cloud service cannot be found.");
        }

        if (createImsConfigurationServlet == null) {
            throw new ServletException("Adobe Stock create IMS ConfigurationServlet cannot be found.");
        }

        if (deleteImsConfigurationsServlet == null) {
            throw new ServletException("Adobe Stock delete IMS ConfigurationServlet cannot be found");
        }

        try {
            //reinstallPackage(request.getResourceResolver());

            //Thread.sleep(1000);

            removeCloudServices(request, response);
            removeImsTechnicalAccountConfigurations(request, response);

            try {
                createKeyStore(request.getResourceResolver(), Constants.NA);
            } catch (Exception e) {
                log.error("Could not create keystore for Adobe Stock integration", e);
                throw new ServletException(e);
            }

            String accessTokenConfigId = createImsConfiguration(request, response);

            createCloudService(request, response, accessTokenConfigId);

            permissionCloudService(request, response, accessTokenConfigId);

            createMissingPreferencesNode(request.getResourceResolver());

            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not create cloud service config for Adobe Stock", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }


    public void execute(ResourceResolver resourceResolver, ValueMap params) throws Exception {

    }

    /* ************************************************************************************************************
       IMS CONFIGURATION
    * ************************************************************************************************************/

    private String createImsConfiguration(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        request.getResourceResolver().refresh();

        final String jwtClaims = new JSONObject() {{
            try {
                put("exp", "1539182446");
                put("iss", "7E71F90859081A410A495D55@AdobeOrg");
                put("sub", "E01D67805BBC4E4A0A495C49@techacct.adobe.com");
                put("https://ims-na1.adobelogin.com/s/ent_stocksearch_sdk", true);
                put("aud", "https://ims-na1.adobelogin.com/c/132ea62d135e43569da194cf4a6e728e");
            } catch (JSONException e) {
                throw new ServletException("Could not create IMS Payload object for Adobe Stock setup", e);
            }
        }}.toString();

        final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                put("cloudServiceName", new String[]{"Adobe Stock"}).
                put("keypairAliases", new String[]{"aem-demo-utils-adobe-stock"}).
                put("keypairAlias", new String[]{"aem-demo-utils-adobe-stock"}).
                put("newKeypairAlias", new String[]{"aem-demo-utils-adobe-stock"}).
                put("title", new String[]{"Adobe Stock (Enterprise)"}).
                put("authServerUrl", new String[]{"https://ims-na1.adobelogin.com"}).
                put("apiKey", new String[]{"132ea62d135e43569da194cf4a6e728e"}).
                put("clientSecret", new String[]{"c42f2f31-6bdd-43eb-80eb-c077b311894e"}).
                put("jwtClaims", new String[]{jwtClaims}).
                build();

        SlingHttpServletRequest wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
        SyntheticResponse wrappedResponse = new SyntheticResponse(response);

        createImsConfigurationServlet.service(wrappedRequest, wrappedResponse);

        if (wrappedResponse.getStatus() != 200 && StringUtils.isNotBlank(wrappedResponse.getString())) {
            throw new ServletException("Could not setup Adobe Stock cloud service");
        }

        log.info("Successfully set up IMS configuration for Adobe Stock for [ {} ]", wrappedResponse.getString());

        return wrappedResponse.getString();
    }

    /* ************************************************************************************************************
        CREATE CLOUD SERVICE
    * ************************************************************************************************************/

    public void createCloudService(SlingHttpServletRequest request, SlingHttpServletResponse response, String accessTokenConfigId) throws Exception {
        request.getResourceResolver().refresh();

        final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                put("name", new String[]{"Adobe Stock (Enterprise)"}).
                put("imsConfig", new String[]{accessTokenConfigId}).
                //put("environment", new String[]{ "PROD" }).
                //put("licensePath", new String[]{ "/content/dam" }).
                        put("locale", new String[]{"en_US"}).
                        build();

        SlingHttpServletRequestWrapper wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
        SyntheticResponse wrappedResponse = new SyntheticResponse(response);

        createCloudServiceServlet.service(wrappedRequest, wrappedResponse);

        if (wrappedResponse.getStatus() != 200) {
            throw new ServletException("Could not setup Adobe Stock cloud service");
        }

        // Sleep to let the OSGi service bind and create the node under /conf/global/settings/stock
        Thread.sleep(1000);
    }


    /* ************************************************************************************************************
        PERMISSION CLOUD SERVICE
    * ************************************************************************************************************/

    public void permissionCloudService(SlingHttpServletRequest request, SlingHttpServletResponse response, String accessTokenConfigId) throws Exception {
        request.getResourceResolver().refresh();

        final String pid = findConfigId(request.getResourceResolver(), accessTokenConfigId);

        final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                put("name", new String[]{"Adobe Stock (Enterprise)"}).
                put("imsConfig", new String[]{accessTokenConfigId}).
                //put("environment", new String[]{ "PROD" }).
                //put("licensePath", new String[]{ "/content/dam" }).
                        put("locale", new String[]{"en_US"}).
                        put("configurationId", new String[]{pid}).
                        put("user", new String[]{"/home/groups/d/dam-users", "/home/groups/a/administrators"}).
                        build();

        SlingHttpServletRequestWrapper wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
        SyntheticResponse wrappedResponse = new SyntheticResponse(response);

        createCloudServiceServlet.service(wrappedRequest, wrappedResponse);

        if (wrappedResponse.getStatus() != 200) {
            throw new ServletException("Could not setup Adobe Stock cloud service");
        }

        log.debug("Updated permissions on Adobe Stock Cloud Service [ {} ]", pid);
    }

    /* ************************************************************************************************************
        UPDATE KEYSTORE
    * ************************************************************************************************************/

    private void createKeyStore(ResourceResolver resourceResolver, String region) throws Exception {
        String alias = "aem-demo-utils-adobe-stock";

        String password = "demo";

        String keystorePassword = "demo";

        if (!keyStoreService.keyStoreExists(resourceResolver, SERVICE_USER_ID)) {
            keyStoreService.createKeyStore(resourceResolver, SERVICE_USER_ID, keystorePassword.toCharArray());
            log.info("Created keystore for [ {} ]", SERVICE_USER_ID);
        } else {
            try {
                keyStoreService.changeKeyStorePassword(resourceResolver, SERVICE_USER_ID, keystorePassword.toCharArray(), keystorePassword.toCharArray());
            } catch (SecurityException e) {
                CleanerUtil.remove(resourceResolver, "/home/users/system/" + SERVICE_USER_ID + "/keystore");
                keyStoreService.createKeyStore(resourceResolver, SERVICE_USER_ID, keystorePassword.toCharArray());
            }
        }

        final String certName = StringUtils.lowerCase("keystore.p12");
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

    private String getUserId(ResourceResolver resourceResolver) throws RepositoryException {
        return ((JackrabbitSession) resourceResolver.adaptTo(Session.class)).getUserManager().getAuthorizable(SERVICE_USER_ID).getID();
    }


    @Deprecated
    // TODO Cannot write to /apps at runtime
    private String findConfigId(ResourceResolver resourceResolver, String accessTokenConfigId) throws IOException {
        final Resource parent = resourceResolver.getResource("/apps/system/config");

        if (parent != null) {
            for (Resource child : parent.getChildren()) {
                if (StringUtils.startsWith(child.getName(), "com.day.cq.dam.stock.integration.impl.configuration.StockConfigurationImpl")) {

                    try {
                        InputStream is = child.adaptTo(InputStream.class);
                        String content = IOUtils.toString(is, StandardCharsets.UTF_8);
                        if (StringUtils.contains(content, accessTokenConfigId)) {
                            return StringUtils.removeEnd(StringUtils.replace(child.getName(), ".StockConfigurationImpl-", ".StockConfigurationImpl."), ".config");
                        }
                    } catch (Exception io) {
                        log.warn("Could not check if [ {} ] is the correct Adobe Stock configuration", child.getPath());
                    }
                }
            }
        } else {
            log.info("The user setting up Adobe Stock doesnt have access to [ {} ] or it does not exist", "/apps/system/config");
        }

        return null;
    }


    @Deprecated
    // TODO Cannot write to /apps at runtime
    private void removeImsTechnicalAccountConfigurations(SlingHttpServletRequest request, SlingHttpServletResponse response) throws PersistenceException {
        request.getResourceResolver().refresh();

        final Resource parent = request.getResourceResolver().getResource("/apps/system/config");

        if (parent != null) {
            for (Resource child : parent.getChildren()) {
                if (child.isResourceType(JcrConstants.NT_FILE) &&
                        StringUtils.startsWith(child.getName(), "com.adobe.granite.auth.oauth.accesstoken.provider")) {

                    try {
                        InputStream is = child.adaptTo(InputStream.class);
                        String content = IOUtils.toString(is, StandardCharsets.UTF_8);
                        if (StringUtils.contains(content, "aem-demo-utils-adobe-stock")) {

                            final String pid = StringUtils.removeEnd(StringUtils.replace(child.getName(), ".provider-", ".provider."), ".config");
                            final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                                    put("configurationIds", new String[]{pid}).
                                    build();

                            final SlingHttpServletRequest wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
                            final SyntheticResponse wrappedResponse = new SyntheticResponse(response);

                            deleteImsConfigurationsServlet.service(wrappedRequest, wrappedResponse);

                            if ((wrappedResponse).getStatus() != 200) {
                                log.warn("Could not remove Adobe Stock IMS Configuration [ {} ]", pid);
                                continue;
                            }

                            request.getResourceResolver().commit();

                            request.getResourceResolver().delete(child);


                            log.debug("Cleaned up Adobe Stock cloud configuration for [ {} ]", pid);

                        }
                    } catch (Exception io) {
                        log.warn("Could not check if [ {} ] is a delete-able Adobe Stock configuration", child.getPath());
                    }
                }
            }
        } else {
            log.info("The user setting up Adobe Stock doesn't have access to [ {} ] or it does not exist", "/apps/system/config");
        }

        if (request.getResourceResolver().hasChanges()) {
            request.getResourceResolver().commit();
        }

    }


    private void removeCloudServices(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws PersistenceException {
        request.getResourceResolver().refresh();

        final Pattern pattern = Pattern.compile(".*imsConfig=\\\"(.+)\\\".*", Pattern.MULTILINE);
        final ResourceResolver resourceResolver = request.getResourceResolver();
        final Resource parent = request.getResourceResolver().getResource("/conf/global/settings/stock");

        if (parent != null) {
            for (Resource child : parent.getChildren()) {
                if (StringUtils.startsWith(child.getName(), "com.day.cq.dam.stock.integration.impl.configuration.StockConfigurationImpl")) {

                    final String stockPid = child.getName();
                    final String stockOsgiConfigPath = "/apps/system/config/" + StringUtils.replace(child.getName(), ".StockConfigurationImpl.", ".StockConfigurationImpl-") + ".config";
                    final Resource stockOsgiConfigResource = request.getResourceResolver().getResource(stockOsgiConfigPath);

                    if (stockOsgiConfigResource == null) {
                        try {
                            resourceResolver.delete(child);
                        } catch (PersistenceException e) {
                            log.warn("Could not remove dangling config node [ {} ]", child.getPath());
                        }
                        continue;
                    }

                    try {
                        String content = IOUtils.toString(stockOsgiConfigResource.adaptTo(InputStream.class), StandardCharsets.UTF_8);
                        Matcher matcher = pattern.matcher(content);

                        if (matcher.find()) {
                            //final String accessTokenPid = matcher.group(1);
                            final Map<String, String[]> requestParams = ImmutableMap.<String, String[]>builder().
                                    put("configurationIds", new String[]{stockPid}).
                                    build();

                            final SlingHttpServletRequest wrappedRequest = new SyntheticRequest(request, "POST", requestParams);
                            final SyntheticResponse wrappedResponse = new SyntheticResponse(response);

                            deleteCloudServiceServlet.service(wrappedRequest, wrappedResponse);

                            if ((wrappedResponse).getStatus() != 200) {
                                log.warn("Could not remove Adobe Stock Cloud Service [ {} ]", stockPid);
                                continue;
                            }

                            resourceResolver.commit();
                            resourceResolver.refresh();

                            if (resourceResolver.getResource(stockOsgiConfigPath) != null) {
                                resourceResolver.delete(stockOsgiConfigResource);
                            }

                            log.debug("Cleaned up Adobe Stock cloud configuration for [ {} ]", stockPid);
                        }

                    } catch (Exception io) {
                        log.warn("Could not check if [ {} ] is a delete-able Adobe Stock cloud configuration", child.getPath());
                    }
                }
            }
        } else {
            log.info("The user setting up Adobe Stock doesnt have access to [ {} ] or it does not exist", "conf/global/settings/stock");
        }

        if (resourceResolver.hasChanges()) {
            resourceResolver.commit();
        }
    }

    private void reinstallPackage(ResourceResolver resourceResolver) throws RepositoryException, IOException, PackageException {
        final JcrPackageManager packageManager = packaging.getPackageManager(resourceResolver.adaptTo(Session.class));

        Optional<JcrPackage> jcrPackage = packageManager.listPackages().stream().filter(p -> {
            try {
                return p.getPackage().getId().getName().contains("cq-dam-stock-integration-content");
            } catch (Exception e) {
                log.error("Could not get a package while searching for the stock integration package.");
            }
            return false;

        }).findFirst();

        if (jcrPackage.isPresent()) {
            JcrPackage pkg = jcrPackage.get();

            ImportOptions importOptions = new ImportOptions();
            importOptions.setAccessControlHandling(pkg.getPackage().getProperties().getACHandling());

            pkg.install(importOptions);

            resourceResolver.commit();
        }
    }

    private void createMissingPreferencesNode(ResourceResolver resourceResolver) throws RepositoryException, PersistenceException {
        Authorizable auth = resourceResolver.adaptTo(Authorizable.class);
        UserPropertiesManager upm = resourceResolver.adaptTo(UserPropertiesManager.class);
        if (upm != null) {
            UserProperties preferences = upm.getUserProperties(auth, "");
            if (preferences != null) {
                String contentPath = preferences.getResource(".").getPath() + "/" + UserPropertiesService.PREFERENCES_PATH;
                JcrUtils.getOrCreateByPath(contentPath, JcrConstants.NT_UNSTRUCTURED, resourceResolver.adaptTo(Session.class));
                resourceResolver.commit();
            }
        }

    }
}