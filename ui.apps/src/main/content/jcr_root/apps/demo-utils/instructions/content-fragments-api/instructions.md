<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/26390/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

*This video is old and show more operations (Created, Update, Delete) that are supported by AEM as a Cloud Service. This video should be  used to help you gain a basic understanding of how Postman works, but make sure to use the Postman Environment and Collections outlined below.*

## Set up

1. Download the free Postman application from: <a href="https://www.getpostman.com/downloads/" target="_blank">https://www.getpostman.com/downloads/</a>
2. Download the [Postman environment file](https://link.enablementadobe.com/demo-content-fragment-http-api-postman-environment)
2. Import the downloaded Postman environment file into Postman
    + Open Postman > Import in top left > Select the downloaded environment file
    + Ensure the **Adobe Experience Manager as a Cloud Service** is active in the Environment switcher dropdown in the top right
2. Download the [Postman Content Fragment HTTP API collection file](https://link.enablementadobe.com/demo-content-fragment-http-api-postman-collection)
    + Open Postman > Import in top left > Select the downloaded collection file

	![import collection](./content-fragments-api/images/import-json-collection.png)

4. The demo video explains some of the different features that can be demoed.
    + Note that in AEM as a Cloud Service **ONLY** read operation are available. No writes can be effected via the Content Fragments HTTP API.

### Updating the AEM Host, User and Password in Postman

Note that the collection is split into 2 sets of call, AEM Author and AEM Publish.

+ Calls to AEM Author require a valid username and password to be set (default to admin/admin -- the default works on Quickstart Jar, but NOT AEM as a Cloud Service as the admin credentials are not shared there).
+ Calls to AEM Publish do NOT require authentication.

#### Updating AEM Host

By default the Postman Environment sets the `{{aem.host}}` Postman variable to `http://localhost:4502`. This can be updated by editing the Adobe Experience Manager as a Cloud Service Environment in Postman.

#### Updating AEM User and Password

This is ONLY required if connecting to an AEM Author service.

By default the Postman Environment sets the `{{aem.user}}` Postman variable to `admin`, and the `{{aem.password]}}` Postman variable to `admin`.

These can be updated by editing the Adobe Experience Manager as a Cloud Service Environment in Postman. It is likely you will have to create a local user (that is part of the DAM Users group) in AEM Author service in order to set a password you can provide to Postman.

## Other materials

* <a href="https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/reference-materials/assets-api-content-fragments/index.html" target="_blank">Swagger API Reference (internal)</a>

https://link.enablementadobe.com/demo-content-fragment-http-api-postman-environment

