## Cloud Set up

1. Clone the "All Demos" Git repository (requires access to Adobe VPN and [https://git.corp.adobe.com](https://git.corp.adobe.com/aem-technical-marketing/com.adobe.aem.demos.all-demos))
    + `$ git clone git@git.corp.adobe.com:aem-technical-marketing/com.adobe.aem.demos.all-demos.git`
2. Follow [these video instructions](https://docs.adobe.com/content/help/en/experience-manager-cloud-service/implementing/deploying/overview.html#introduction) on how to push the code base to your AEM as a Cloud Service Git repository and deploy to your Environment.

## Set up and Run the Android App Locally

1. Download the latest <a href="http://link.enablementadobe.com/aem-headless_mobile-app" target="_blank" class="button">wknd-mobile.x.x.x.apk</a> (the Android app)
1. Download and install [Android Studio](https://developer.android.com/studio)
1. Set-up the Android Studio and run the Mobile App (`wknd-mobile.x.x.x.apk`) in the Android Emulator. Watch the video below, or follow the [text instructions](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-with-aem-headless/chapter-7.html#running-the-mobile-app-locally).

   <iframe width="854" height="480" src="https://video.tv.adobe.com/v/28341?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
   mozallowfullscreen allowfullscreen scrolling="no"></iframe>

1. Follow the instructions for [configuring the Mobile App for non-localhost use](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-with-aem-headless/chapter-7.html#configuring-the-mobile-app-for-non-localhost-use), and point the host to your AEM as a Cloud Service environments AEM Publish Service URL, for example: https://publish-pXXXX-eXXXXX.adobeaemcloud.com/
The Mobile App __must__ source content from AEM Publish, as AEM Author does not allow un-authenticated access to its content.

## Reviewing the JSON End-points

The WKND Mobile app accesses the JSON via AEM Publish Service at:

* [https://publish-pXXXX-eXXXXX.adobeaemcloud.com/content/wknd-mobile/en/api/events.model.json](https://aem.enablementadobe.com/content/wknd-mobile/en/api/events.model.json)

This is the AEM Content Services rendition of the page, invoked via the `.model.json` selector and extension on the URL. AEM Content Services walks the Page's components, and serializes them JSON conforming to the AEM Content Services JSON schema.
All AEM WCM Core Components are AEM Content Services compatible, and any Custom Components can be made AEM Content Services compatible by having their Sling Model implement the `ComponentExporter` interface.
