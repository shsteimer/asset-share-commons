*Note that Dynamic Media run mode is always enabled for the local Quickstart Jar, so there is no need to rename the Jar file to enable Dynamic Media.*

## Automatic Local Set up

Click the button below to automatically set up the Dynamic Media Cloud Service.

**AFTER CLICKING, BE PATIENT... you will be re-directed to a success or failure page indicating if it's set up successfully.**

<a href="/apps/demo-utils/instructions/dynamic-media.install.html?id=na" class="button">Configure for North America</a>

## Manual Set up

Alternatively, Dynamic Media Cloud Service can be configured manually.

1. Navigate to **AEM > Tools > Cloud Services**.
2. Click on **Dynamic Media Configuration** card.
3. Navigate into the **global** folder and tap **Create** in the top left.
4. Create a new Dynamic Media cloud configuration
    * Title: **Dynamic Media**
    * Email: **dynamicmedia-na@adobe.com**
    * Password: **$Dynamicna1**
    * Region: **North America - Enterprise**
    * Press `Connect to Dynamic Media`
    * Company: **DynamicMediaNA**
    * Company Root Folder Path: **DynamicMediaNA/**
        * *Do **not** change this value.*
    * Publish Assets: **Immediately**
    * Secure Preview Server: **https://preview1.assetsadobe.com**
        * *Do **not** change this value.*

    ![Cloud Config](./dynamic-media/images/cloud-config.png)

### Manual Set up Special instructions

* **Publish Assets: Upon Activation**
    * During manual set up, if `Publish Assets: Upon Activation` is selected, you **must** wait 15 mins for the supporting assets to be published to and processed by Scene 7.
    * After waiting 15 mins, you must Publish any Image and Viewer presets at:
        * <a href="/mnt/overlay/dam/gui/content/s7dam/viewerpresets/viewerpresets.html" target="_blank">AEM > Tools > Assets > Viewer Presets</a>
        * <a href="/mnt/overlay/dam/gui/content/s7dam/imagepresets/imagepresets.html" target="_blank">AEM > Tools > Assets > Image Presets</a>
* Allow all Presets to publish before using. When `Publish Assets` is set to `Immediately`, they will automatically queue up to publish.

