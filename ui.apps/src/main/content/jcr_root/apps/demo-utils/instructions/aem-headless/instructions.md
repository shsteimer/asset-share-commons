The AEM Headless demo showcases how AEM can be used to expose content via HTTP APIs as JSON for consumption by native Mobile Apps; In this case an Android App, however any Mobile App platform (ex. iOS) could be built following the same approaches.

The use of Android is because it has a cross-platform emulator that all users (Windows, macOS, and Linux) of this demo can use to run the native App.

_This demo is a by-product of the [Getting Started with AEM Headless](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-with-aem-headless/overview.html) Tutorial._

## Overview video

<iframe width="854" height="480" src="https://video.tv.adobe.com/v/28315?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

## Value proposition

> A NOTE ABOUT POSITIONING: While this approach can be used to power ANY programmatic consumer, including SPA's, Adobe recommends using AEM SPA Editor as the first choice for any SPA-related use-case.
This demo specifically targets a native Mobile App to help draw the clear distinction of when to use AEM Content Services (Pages with Components that are serialized to JSON and consumed via custom code) and AEM SPA Editor to support the creation of SPAs.

This demo shows how AEM supports managing content centrally in AEM and syndicating it in a Headless fashion to a native Android Mobile App.

This demo is a specific use-case in the larger [Hybrid CMS story](/apps/demo-utils/instructions/hybrid-cms.html) that focuses re-using content specifically in the Mobile App channel.
We explore how AEM supports the rapid creation of a HTTP API, that allows for authors to specific and author the content the API exposes.

At a high-level this demo shows:

* Content that represent an Event using Content Fragments
* AEM Content Services end-points using AEM Sites' Templates and Pages that expose the Event data as JSON
* AEM WCM Core Components can be used to enable marketers to author JSON end-points
* Consume AEM Content Services JSON from an Mobile App

<!-- QUICKSTART_INSTRUCTIONS -->

<!-- CLOUD-SERVICE_INSTRUCTIONS -->

## Additional Resources

* [Getting Started with AEM Headless Tutorial](https://docs.adobe.com/content/help/en/experience-manager-learn/getting-started-with-aem-headless/overview.html)
* [AEM Headless Github Repository](https://github.com/adobe/aem-guides-wknd-mobile)
* [Hybrid CMS Demo on DemoHub](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-hybrid-cms.html)
