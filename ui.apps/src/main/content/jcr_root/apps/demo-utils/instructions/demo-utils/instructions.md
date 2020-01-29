AEM Demo Utils is a tool to help expedite the demoing of AEM as a Cloud Service. It contains:

+ List of common features to demo
+ Instructions on how to set up feature and how to demo them
+ Some features have automatic set-up using shared Demo credentials provided by Demo Utils project

## FAQs

### What is this term "Skyline" I see internally?

Skyline is the **INTERNAL** code name for the AEM as a Cloud Service product. Skyline *is* AEM as a Cloud Service, and AEM as a Cloud Service *is* Skyline.

A good rule of thumb is to *never* use the term "Skyline" (especially outside of Adobe), *always* use "Adobe Experience Manager as a Cloud Service".

---

### How do I provision my Adobe IMS Organization with the AEM as a Cloud Service product?

Please review the [AEM as a Cloud Service Sandbox FAQ](https://wiki.corp.adobe.com/display/WEM/Skyline+Sandbox+FAQ) on the Adobe Wiki for the latest instructions on requesting

---

### How do I get an AEM as a Cloud Service sandbox?

Once an Adobe IMS Org youre part of is provisioned for AEM as a Cloud Service:

1. Navigate to [https://experience.adobe.com/](https://experience.adobe.com/)
2. Ensure the Adobe IMS Org that has been provisioned with AEM as a Cloud Service is active in the Adobe IMS Org switcher.
3. Tap on **Experience Manager** from the Solution Switcher or Quick Access product list.
4. Tap the **AEM Managed Services** card.
5. Tap the **Add Program** button in the top right.
6. Give your program a meaningful, and identifiable name (you CANNOT change this name later) and tap **Create**
7. Once your program has been created, you can access it via the [Adobe Cloud Manager Program list console](https://my.cloudmanager.adobe.com/)

---

### How do I install demos onto my AEM as a Cloud Service sandbox environment?

AEM as a Cloud Service (running in the Adobe Cloud) can only have Code deployed to it via Adobe Cloud Manager, which requires knowledge of AEM Maven Projects and Git.

To review how demo applications can be installed, please review this [deployment overview video](https://docs.adobe.com/content/help/en/experience-manager-cloud-service/implementing/deploying/overview.html#Introduction) that uses the WKND site as the "example AEM application" to deploy.

---

### How do I run AEM as a Cloud Service locally?

To get download the AEM as a Cloud Service Quickstart Jar:

1. In a browser, navigate to [https://downloads.experiencecloud.adobe.com](https://downloads.experiencecloud.adobe.com/content/software-distribution/en/aemcloud.html) and login with your Adobe ID.
2. Navigate to the AEM as a Cloud Service tab and download the latest AEM SDK zip file.
    + If the AEM as a Cloud Service tab is not present, your Adobe ID account is  not part of an Adobe IMS Organization that has access to the AEM as a Cloud Service product.
3. Unzip the `aem-sdk-xxx.zip`
4. Rename the Jar file to `aem-author-p4502.jar` or `aem-publish-p4503.jar`, ensure your `properties.license` file is in the same folder, and double-click the Jar to start it.

---

### How do I install demos onto my local AEM as Cloud Service instance?

Demo packages can be installed in the usual way, using AEM Package Manager.

+ [http://localhost:4502/crx/packmgr/index.jsp](http://localhost:4502/crx/packmgr/index.jsp)

----

Please report any issues to <a href="mailto:Grp-AEMTechMarketing@adobe.com">Grp-AEMTechMarketing@adobe.com</a>



