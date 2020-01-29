
<div class="unsupported">
There is plan to have auto-provisioning for Smart Tags in AEM as a Cloud Service but not ready yet.
</div>

## Automatic Cloud Set up

Click the buttons below to automatically set up the Smart Tags cloud service:

* The **Smart Tags cloud service**
* The **dam-update-service** key store
* Enables **Smart Tags** on all asset folders.
* **Automatically** Smart Tags on upload to AEM Assets.

<a href="/apps/demo-utils/instructions/smart-tags.install.html?region=na" class="button">Configure for North America</a>
<a href="/apps/demo-utils/instructions/smart-tags.install.html?region=emea" class="button">Configure for EMEA</a>
<a href="/apps/demo-utils/instructions/smart-tags.install.html?region=apac" class="button">Configure for APAC</a>

## Smart Tags existing assets

* Once Smart Tags has been configured, Smart Tag any existing assets by selecting either Assets or Assets Folders, and tapping the `Reprocess Assets` button in the top action bar.
    ![Reprocess assets](./smart-tags/images/reprocess-assets.png)

## Manually set up Smart Tags (all configurations support Training)

1. Open AEM Author and click <a href="/etc/cloudservices.html" x-cq-linkchecker="skip" target="_blank">Tools > Cloud Services > Legacy Cloud Services</a>
2. Click **Configure now** under Assets Smart Tags
3. Enter a Title and click **Create**
4. Enter the following configuration based on your geographical preference

    ### Configuration for North America

    ```plain
    Service URL : https://mc.adobe.io/marketingcloud/smartcontent
    Authorization Server : https://ims-na1.adobelogin.com
    Api Key : 691f65f597434342be3ec16052380b04
    Technical Account ID : CA092896592BBD490A495D28@techacct.adobe.com
    Organization ID : 218BFF5659280AF50A495D22@AdobeOrg
    Client Secret : 5cd48ae9-a51d-42c8-9aa7-ceb8b3bd7094
    ```

    #### Configuration for EMEA

    ```plain
    Service URL : https://mc.adobe.io/marketingcloud/smartcontent
    Authorization Server : https://ims-na1.adobelogin.com
    Api Key : 142648eb1a874352837882f038a13b2a
    Technical Account ID : B24ED851592BBDB00A495D25@techacct.adobe.com
    Organization ID : CBAFF3C959280C4B0A495E7B@AdobeOrg
    Client Secret : 8bd0f8de-bca9-4998-ae98-04b922c73bbc
    ```

    #### Configuration for APAC

    ```plain
    Service URL : https://mc.adobe.io/marketingcloud/smartcontent
    Authorization Server : https://ims-na1.adobelogin.com
    Api Key : b6a5e3672e8749ce992b1a369cdacf6d
    Technical Account ID : 43FF19775A6997270A495C95@techacct.adobe.com
    Organization ID : 7B493FE759719AAB0A495EEC@AdobeOrg
    Client Secret : a86f6617-80e1-4a3b-8e5d-6fac8e1bb121
    ```

5. Click **OK** to save Assets Smart Tags configuration.
6. Go to <a href="/libs/granite/security/content/useradmin.html" target="_blank">Tools > Security > Users</a> and click on user: **dam-update-service**
7. Click on **Create Key Store** and create one (any password is fine)
8. Once created, click **Manage Key Store > Add Private Key from Key Store File**
  * Base on which geographic service you chose to integrate with above...
      * North America
         * New Alias: **similaritysearch**
        * Key file: <a href="/apps/demo-utils/resources/smart-tags/smart-tags-na-adobe-io.p12" x-cq-linkchecker="skip" target="_blank">smart-tags-na-adobe-io.p12</a>
         * Keystore password: **test123**
         * Private key alias: **similaritysearch**
         * Set the private key password: **test123**
      * EMEA
        * New Alias: **similaritysearch**
        * Key file: <a href="/apps/demo-utils/resources/smart-tags/smart-tags-emea-adobe-io.p12" x-cq-linkchecker="skip" target="_blank">smart-tags-emea-adobe-io.p12</a>
        * Keystore password: **test123**
        * Private key alias: **similaritysearch**
        * Set the private key password: **test123**
      * APAC
        * New Alias: **similaritysearch**
        * Key file: <a href="/apps/demo-utils/resources/smart-tags/smart-tags-apac-adobe-io.p12" x-cq-linkchecker="skip" target="_blank">smart-tags-apac-adobe-io.p12</a>
        * Keystore password: **test123**
        * Private key alias: **similaritysearch**
        * Set the private key password: **test123**
9. Click **Submit**
10. Click **Save** in the top right

