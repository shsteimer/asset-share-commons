## Local Set up

### AEM Content Packages

_Install via AEM Package Manager on __BOTH__ AEM Author and Publish_

* <a href="https://link.enablementadobe.com/demo-wknd-mobile-all" target="_blank" class="button">com.adobe.aem.guides.wknd-mobile.all-x.x.x.zip</a>

### Android Mobile App

* <a href="http://link.enablementadobe.com/aem-headless_mobile-app" target="_blank" class="button">wknd-mobile.x.x.x.apk</a>
* <a href="https://developer.android.com/studio" class="button">Android Studio</a>

## Set up Instructions

1. Install `com.adobe.aem.guides.wknd-mobile.all-x.x.x.zip` on [AEM Author via Package Manager](http://localhost:4502/crx/packmgr/index.jsp)
1. Install `com.adobe.aem.guides.wknd-mobile.all-x.x.x.zip` on [AEM Publish via Package Manager](http://localhost:4503/crx/packmgr/index.jsp)
1. Download [Android Studio](https://developer.android.com/studio)
1. Set-up the Android Studio and run the Mobile App (`wknd-mobile.x.x.x.apk`) in the Android Emulator. Watch the video below, or follow the [text instructions](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-with-aem-headless/chapter-7.html#running-the-mobile-app-locally).

   <iframe width="854" height="480" src="https://video.tv.adobe.com/v/28341?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
   mozallowfullscreen allowfullscreen scrolling="no"></iframe>

1. If running AEM Publish on something other than `http://localhost:4503`, follow the instructions for [configuring the Mobile App for non-localhost use](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-with-aem-headless/chapter-7.html#configuring-the-mobile-app-for-non-localhost-use).
The Mobile App __must__ source content from AEM Publish, as AEM Author does not allow un-authenticated access to its content.

## Reviewing the JSON End-points

The WKND Mobile app accesses the JSON via AEM Publish at:

* [http://localhost:4503/content/wknd-mobile/en/api/events.model.json](http://localhost:4503/content/wknd-mobile/en/api/events.model.json)

This is the AEM Content Services rendition of the page, invoked via the `.model.json` selector and extension on the URL. AEM Content Services walks the Page's components, and serializes them JSON conforming to the AEM Content Services JSON schema.
All AEM WCM Core Components are AEM Content Services compatible, and any Custom Components can be made AEM Content Services compatible by having their Sling Model implement the `ComponentExporter` interface.

