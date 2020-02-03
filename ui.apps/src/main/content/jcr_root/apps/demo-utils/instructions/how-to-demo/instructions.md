AEM Demo Utils can be used to expedite the demoing of AEM as a Cloud Service by providing:

+ List of common features to demo
+ Instructions on how to set up feature and how to demo them
+ Some features have automatic set-up using shared Demo credentials provided by Demo Utils project

## What can I demo on AEM as a Cloud Service vs Quickstart Jar?

This list is current as of: February 3, 2020.

For more complete lists including bug-fix and roadmap status please review the following KT Wikis:

+ [AEM Assets - Un/supported Capabilities at GA](https://wiki.corp.adobe.com/display/DMSArchitecture/KT+-+AEM+Assets+Cloud+Service+-+Supported+capabilities+by+GA#KT-AEMAssetsCloudService-SupportedcapabilitiesbyGA-Deprecated/UnsupportedCapabilities(atGA))
+ [AEM Sites - Supported Capabilities at GA](https://wiki.corp.adobe.com/display/~msiegel/Adobe+Experience+Manager+as+a+Cloud+Service+-++Sites+Features)
+ [Notables changes in AEM as a Cloud Service](https://wiki.corp.adobe.com/display/DMSArchitecture/KT+-+AEM+Sites+Cloud+Service+-+Notable+changes)

### Supported features

The following are features that are intended to work on AEM as a Cloud Service (note, that some may be broken on both AEM as a Cloud Service and Quickstart Jar at this time, but these are unplanned bugs).

|      | Feature | AEM as a Cloud Service | Quickstart Jar |
|------|---------|------------------------|----------------|
| Assets | Adobe Asset Link | ✘ | ✘ |
| Assets | Asset Insights | ✔ | ✔ |
| Assets | Assets microservices (OOTB workers) | ✔ | ✘ |
| Assets | Asset Share Commons | ✔ | ✔ |
| Assets | Adobe Stock integration | ✘ | ✘ |
| Assets | Connected Assets | ✘ | ✘ |
| Assets | Desktop App | ✔ | ✔ |
| Assets | Smart Tags | ✔ | ✔ |
| Assets | Visual Search | ✘ | ✘ |
| Assets - Dynamic Media | Basic functionality | ✘ | ✘ |
| Assets - Dynamic Media | Image Smart Crop | ✔ | ✔ |
| Assets - Dynamic Media | Video Smart Crop | ✘ | ✘ |
| Assets - Dynamic Media | 360/VR video | ✘ | ✘ |
| Sites | Core Components Showcase | ✔ | ✔ |
| Sites | Hybrid CMS Demo | ✘ | ✘ |
| Sites | WKND Site | ✔ | ✔ |
| Sites | WKND App (SPA Editor) | ✔ | ✔ |
| Content Services | Content Fragment HTTP API (read-only) | ✔ | ✔ |
| Content Services | WKND Mobile (AEM Headless) | ✔ | ✔ |
| All  | Ability to send e-mails from AEM | ✘ | ✔ |

### Unsupported and Roadmap Features

The following features are currently not supported by AEM as a Cloud Service. Items in this list may eventually be supported by AEM as a Cloud Service.

The ETA column in the table below gives a rough indicator when features might be made available in AEM as a Cloud Service. Note that the timing may change as priorities evolve.


|        | ETA  | Feature |
|--------|------|---------|
| All    | TBD    | User generated content on AEM Publish (no writes to AEM Publish) |
| Assets | 2020 | Assets Catalogs (Commerce & InDesign Server integration)) |
| Assets | 2020 | Assets microservices (Custom workers) |
| Assets | 2020 | Assets Templates (InDesign Server integration) |
| Assets | 2020 | Brand Portal |
| Assets | 2020 | InDesign Server integration |
| Assets | 2020 | Photoshoot Project (AEM Projects) |
| Assets | 2020 | Imagemagick, FFMPEG, and all other command-line Workflow steps |
| Assets | 2020 | Smart Tags Training (aka Enhanced Smart Tags) |
| Assets | TBD  | Smart Translation Search |
| Assets | TBD  | Assets HTTP API *(for write operations)* |
| Assets | TBD  | YouTube publishing |
| Assets | TBD  | Zip file extraction |
| Sites | Never | Design / Landing Page Importer |
| Commerce | 2020 | Currently not supported |
| Communities | TBD | Currently not supported |
| Screens | 2020 | Not supported |
| Forms | ~2020 | Not Supported; [See details](https://wiki.corp.adobe.com/display/WEM/AEM+Forms+Services+and+Skyline) |

## FAQs

### What's the difference between "AEM as a Cloud Service" and "Skyline"?

Nothing. Skyline was the **ADOBE INTERNAL** code name for the AEM as a Cloud Service product.

Never use the term "Skyline" (especially outside of Adobe), instead use "Adobe Experience Manager as a Cloud Service".

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

### How do I install demos on my AEM as a Cloud Service sandbox environment?

AEM as a Cloud Service (running in the Adobe Cloud) can only have Code deployed to it via Adobe Cloud Manager, which requires knowledge of AEM Maven Projects and Git.

To review how demo applications can be installed, please review this [deployment overview video](https://docs.adobe.com/content/help/en/experience-manager-cloud-service/implementing/deploying/overview.html#Introduction) that uses the WKND site as the "example AEM application" to deploy.

---

### How do I run AEM as a Cloud Service locally?

Download and run the AEM as a Cloud Service Quickstart Jar:

1. In a browser, navigate to [https://downloads.experiencecloud.adobe.com](https://downloads.experiencecloud.adobe.com/content/software-distribution/en/aemcloud.html) and login with your Adobe ID.
2. Navigate to the AEM as a Cloud Service tab and download the latest AEM SDK zip file.
    + If the AEM as a Cloud Service tab is not present, your Adobe ID account is  not part of an Adobe IMS Organization that has access to the AEM as a Cloud Service product.
3. Unzip the `aem-sdk-xxx.zip`
4. Rename the Jar file to `aem-author-p4502.jar` or `aem-publish-p4503.jar`, ensure your `properties.license` file is in the same folder, and double-click the Jar to start it.

---

### How do I install demos onto my local Quickstart Jar.

Demo packages can be installed on the local AEM as a Cloud Service Quickstart Jar in the usual way, using AEM Package Manager.

+ [http://localhost:4502/crx/packmgr/index.jsp](http://localhost:4502/crx/packmgr/index.jsp)

----

Please report any issues to <a href="mailto:Grp-AEMTechMarketing@adobe.com">Grp-AEMTechMarketing@adobe.com</a>



