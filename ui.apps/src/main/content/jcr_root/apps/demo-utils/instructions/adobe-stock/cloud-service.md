<div class="unsupported">
    The Adobe Stock integration's Cloud Service is removed every time a deployment (updating the AEM version, or deploying update code) is made to the AEM as a Cloud Service environment.
</div>

## Cloud Set up

This set up uses the AEM Technical Marketing team's Enterprise Adobe Stock account. Please be aware of this accounts licensing access on Adobe Stock:

* Images obtained through this account may only be used for demonstration purposes (NOT for use in demo PPT, etc.)
* ONLY images can be accessed through this account (there is NO access to video).
* Not ALL images are accessible. Images that cannot be downloaded will prevent you from licensing and saving them to AEM.

### Add the Private Key to the Service User

A private key that matches the provide Enterprise Adobe Stock account must be added to the `stock-imsconfig-service` user.

1. Navigate to [AEM > Tools > Security > Users > stock-imsconfig-service](/libs/granite/security/content/v2/usereditor.html/home/users/system/stock-imsconfig-service)
2. Tap the **Keystore** tab.
3. Create a **new Keystore** if needed: `demo`
4. Tap **Add Private Key from KeyStore file** and add the following:
    * New Alias: `aem-demo-utils-adobe-stock`
    * KeyStore File: [keystore.p12](/apps/demo-utils/resources/adobe-stock/keystore.p12)
    * KeyStore File Password: `demo`
    * Private Key Alias: `aem-demo-utils-adobe-stock`
    * Private Key Password: `demo`
5. Tap **Submit**
6. Tap **Save & Close** in the top right.

	![Adobe Stock - Service User Keystore](./adobe-stock/images/service-user__keystore.png)


### IMS technical account set up

After the private key has been added, an IMS Technical Account must be set up in AEM to allow AEM to communicate via Adobe I/O with Adobe Stock.

1. Navigate to [AEM > Tools > Security > Adobe IMS Configurations](/libs/cq/adobeims-configuration/content/configurations.html)
2. Click the **Create** button.
3. On the **Certificate** wizard section
    * Cloud Solution: `Adobe Stock`
    * Certificate: `aem-demo-utils-adobe-stock`
   
    ![Adobe Stock -  IMS 1](./adobe-stock/images/ims-technical__certificate.png)

4. Click **Next**
5.  On the **Account** wizard section
    * Title: `Adobe Stock (Enterprise)`
        * *The title can be anything, but it's used in the Cloud Service configuration below*
    * Authorization Server: `https://ims-na1.adobelogin.com`
    * API Key: `132ea62d135e43569da194cf4a6e728e`
    * Client Secret: `c42f2f31-6bdd-43eb-80eb-c077b311894e`
    * Payload: 

    ```
    {
        "exp": 1539275398,
        "iss": "7E71F90859081A410A495D55@AdobeOrg",
        "sub": "E01D67805BBC4E4A0A495C49@techacct.adobe.com",
        "https://ims-na1.adobelogin.com/s/ent_stocksearch_sdk": true,
        "aud": "https://ims-na1.adobelogin.com/c/132ea62d135e43569da194cf4a6e728e"
    }
	```
	
	![Adobe Stock - IMS 2](./adobe-stock/images/ims-technical__account.png)

5. Click **Create**

### Adobe Stock cloud service set up

one or more Adobe Stock Cloud Services must be created in AEM, to enable Adobe Stock.

Note that multiple Adobe Stock Cloud Services can be created and assign various locales and/or allowed user groups.

1. Navigate to **[AEM > Tools > Cloud Services](/libs/cq/adobeims-configuration/content/configurations.html)**
2. Tap the **Adobe Stock** card.
3. Tap the **Create** button in the top right.
4. Create a **new Cloud Service** configuration:
    * Title: `This can be anything`
    * Associated Adobe IMS Configuration: `Adobe Stock (Enterprise)`
        * *This must be the IMS Configuration created in Step 5 above.*
    * Locale: `Pick a locale of your choosing`
5. Tap **Save & Close** in the top right.

![Adobe Stock - Cloud Service](./adobe-stock/images/cloud-service.png)
