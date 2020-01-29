# AEM Assets Brand Portal

> Watch a quick video on the **latest** features on Brand Portal 6.4.4 (~10 mins)

<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/26354/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen 
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

For an overview video click [here](https://helpx.adobe.com/experience-manager/kt/eseminars/gems/aem-brand-portal.html).

## Shared Brand Portal Accounts

<table>
<thead>
<tr>
<th>Env</th>
<th>Brand Portal Tenant URL</th>
<th>Brand Portal Adobe ID Credentials</th>
</tr>
</thead>
<tbody>
<tr>
<td>NA</td>
<td><a href="https://bpdemona1.brand-portal.adobe.com">https://bpdemona1.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
</tr>
<tr>
<td>NA</td>
<td><a href="https://bpdemona2.brand-portal.adobe.com">https://bpdemona2.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
</tr>
<tr>
<td>EMEA</td>
<td><a href="https://bpdemoemea1.brand-portal.adobe.com">https://bpdemoemea1.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
</tr>
<tr>
<td>EMEA</td>
<td><a href="https://bpdemoemea2.brand-portal.adobe.com">https://bpdemoemea2.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
</tr>
<tr>
<td>APAC</td>
<td><a href="https://bpdemoapac.brand-portal.adobe.com">https://bpdemoapac.brand-portal.adobe.com</a></td>
<td><a href="mailto:demouser@adobe.com">demouser@adobe.com</a> / Brandportal#4</td>
</tr>
</tbody>
</table>

*Credentials last updated: September 10, 2018*. These passwords will expire over time. If the list password does not work, try the next few sequential numbers (ie. Brandportal#5, Brandportal#6...)

If the above credentials do not work, please check the <a href="https://wiki.corp.adobe.com/display/AdobeDAM/Brand+Portal+demo+instances" target="_blank">Internal Adobe Wiki</a> for the latest passwords.

## Set up Brand Portal

> Note **https://marketing.adobe.com/developer/applications** is replaced with **https://legacy-oauth.cloud.adobe.io**

1. Navigate to <a href="https://legacy-oauth.cloud.adobe.io" target="_blank">https://legacy-oauth.cloud.adobe.io</a>
2. If necessary, click on the **Login** with Adobe ID button to authenticate yourself using the Brand Portal Adobe ID Credentials provided in the table above.
4. Tap the **Add Application** button to **create a new application** (the organization drop-down does not matter at this point)
5. In the **Application Name** field, provide the following name: **<YOUR-LDAP-ID>-brand-portal **(eg. dgonzale-brand-portal)
6. In the **Description** field, enter a date in which this configuration can be removed (clean-up); or leave blank to never have it removed.
7. Leave **Public Key** field blank for now (this will be updated later using a value from AEM Author).
8. In the **Organization (Company)** field, select the desired Brand Portal instance org. Ensure this is the organization that matches the Brand Portal environment you will connect to. (eg. Select **bpdemona1** for North America 1)
9. In the **Scope** field, select all options: **cc-share, dam-read, dam-write, and dam-sync**
10. Click the **Add** button.
11. **DO NOT close the browser window**, additional steps are required on this web page
12. Log in to AEM Author
13. In AEM, navigate to **Tools > Cloud Services > Legacy Cloud Services**
14. Under **Assets Brand Portal** tap **Configure now**
15. In the Create Service dialog, provide a name for the new Adobe Marketing Cloud service
16. Tap **Create**
17. Enter the full **Brand Portal Tenant URL** (e.g. https://[tenant-ID].brand-portal.adobe.com) as provided in the table above
18. The **Client ID** is the **Application ID** that was assigned to the application created previously in the **legacy-oauth.cloud.adobe.io** portal (available fom Step 10) . Copy and paste that **Application ID** into the **Client ID **field in AEM
19. Uncheck **Public Folder Publish** ... Or check this is you're OK with anyone using the (shared) Brand Portal instance to be able to see your folders and assets.
20. Tap **OK**.
21. Wait 10 seconds and tap on the **Display Public Key** button
22. **Copy the public key** to your clipboard (including the “-----BEGIN PUBLIC KEY-----” and “-----END PUBLIC KEY-----” parts.
23. Return to the legacy-oauth.cloud.adobe.io web page, and **paste into the Public Key field**
24. Click **Save** on the Marking Cloud Application web page

----

## Demos

* [Brand Portal Demo](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-assets-brand-portal.html)

## Other materials

* [Credentials for Shared Demo Brand Portal servers](https://wiki.corp.adobe.com/display/AdobeDAM/Brand+Portal+demo+instances)

* Videos
    * [AEM Assets Brand Portal Overview Video](https://helpx.adobe.com/experience-manager/kt/eseminars/gems/aem-brand-portal.html)
    * [Set up Brand Portal with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-technical-video-setup.html)
    * [Using Brand Portal with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-feature-video-use.html)
    * [Developing for Brand Portal with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-technical-video-develop.html)
* Adobe Docs
    * [Overview of AEM Assets Brand Portal](https://helpx.adobe.com/experience-manager/brand-portal/using/brand-portal.html)
    * [What's new in AEM Assets Brand Portal](https://helpx.adobe.com/experience-manager/brand-portal/using/whats-new.html)
    * [Understanding Brand Portal in AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/brand-portal-article-understand.html)

