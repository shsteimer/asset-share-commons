## Local Set up

### Automatic set up instructions

<a href="/apps/demo-utils/instructions/adobe-asset-link.install.html" class="button">Configure Adobe Asset Link</a>

*This setup installs 1 AEM packages that include the required code and configuration to setup Adobe Asset Link against a shared, demo IMS account. See the Manual setup instructions below for details.*

> Note: Currently there is an issue logging into AEM with enterprise ID. Continue to login to AEM with the local admin:admin account.

![AEM Local Login](./adobe-asset-link/images/aem-login-locally.png)

### Troubleshooting the Authorization Error

The first time logging in to AEM via the Asset Link panel you may see an *Authorization Error*.

![Authorization Error](./adobe-asset-link/images/auth-error.png)

To resolve this:

1. Log in to AEM as `admin` using the local account
2. Navigate to <a href="/libs/granite/security/content/useradmin.html" target="_blank">Tools > Security > User Management</a>
3. Find your user and click to __Edit__
4. Select the **Group** tab
5. Add yourself as a member of the __Administrators__ group
	![Administrator group](./adobe-asset-link/images/administrator-group.png)
6. Save your changes
7. Re-start the Creative suite product and you should be authorized

### Manual setup instructions

<ol>
<li>Download <a
        href="/apps/demo-utils/resources/adobe-asset-link/aem-demo-utils.asset-link-config-1.1.0.zip" x-cq-linkchecker="skip" target="_blank">aem-demo-utils.asset-link-config-1.1.0</a> AEM content package.
</li>
<li>Install the downloaded AEM Package via <a x-cq-linkchecker="skip" href="/crx/packmgr/index.jsp" target="_blank">AEM Package Manager</a>.
     <br/>
     When this package is installed, the AEM login screen will appear as:
     <img src="./adobe-asset-link/images/aem-login.png"/>
</ol>

## Creative Cloud App set up

1. You must be logged into the [Creative Cloud app](https://helpx.adobe.com/creative-cloud/help/manage-apps-services-desktop.html#InstallCreativeClouddesktopapp) with your **Adobe Enterprise ID (aka Federated ID)** for this integration to work.
