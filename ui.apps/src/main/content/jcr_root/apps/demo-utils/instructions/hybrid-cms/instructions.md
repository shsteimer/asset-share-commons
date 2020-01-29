<div class="aem-logo"></div>
<div class="adobe-logo"></div>

# AEM as a Hybrid CMS Demo

AEM is often misunderstood as a monolithic CMS that must "own the glass" for the channels it delivers content to; AEM Sites, Screens, Communities. Enterprises want to leverage AEM to manage content centrally and use it ACROSS the enterprise, even channels not owned by AEM (Mobile, IoT, etc.)

> Watch the demo and setup: 

<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/27721?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen 
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

## Value proposition

This demo shows how AEM supports managing content centrally in AEM and using it across Traditional channels (AEM Sites) as well as in a Headless fashion, using an Ionic App (used for Mobile app development) to consume and display AEM content using AEM's Content Services. AEM's SPA Editor presents another variation, in which content from AEM is delivered using Content Services as JSON to a Single Page Application, while still providing authors with in-context editing capabilities. 

At a high-level this demo shows:

* **Content Fragments** that represent We.Retail athletes can be leveraged on the We.Retail website (powered by AEM Sites), used on the We.Retail SPA (powered by AEM SPA Editor) and exposed dynamically using a **Content Fragment List** to be consumed in the Mobile App.
* **Experience Fragment** used on the We.Retail website and consumed in the Mobile App. Live copy is used to create a Summary variation.
* There is an FAQs section that is made up of Content Fragments that can be consumed by the Mobile App using the same approach as the Athletes with a **Content Fragment List**.
* Another FAQ portion of the app that leverages the **Assets HTTP APIs** to provide Create, Review, Update, and Delete (CRUD) functionality of the FAQ content fragments.

## Downloads

* <a href="http://link.enablementadobe.com/demo-hybrid-cms-ui-apps" target="_blank" class="button">demo-hybrid-cms.ui.apps.zip</a>
* <a href="http://link.enablementadobe.com/aem-hybrid-cms_ionic-app" target="_blank" class="button">aem-hybrid-cms_ionic-app.zip</a>

## Ionic app automatic setup

### Pre-requisite for both macOS and Windows

1. NodeJS 8.x.x LTS is required for this application to run. This is a one-time install.
	* Please download NodeJS 8.x.x from [nodejs.org](http://nodejs.org) and install. If it is NOT installed, the subsequent steps will fail.
2. The `demo-hybrid-cms.ui.apps-x.x.x.zip` AEM pacakge must be installed on AEM author running on `http://localhost:4502` for this Ionic app to work. Only **AEM 6.5** is supported. 

### macOS

1. Download and unzip `aem-hybrid-cms_ionic-app-x.x.x.zip` to your macOS machine.
2. Double-click on `start-ionic-app.command`.
3. With the first double-click, you will be prompted by macOS that the program is not allowed to run. Navigate to `System Preferences > Security & Privacy` and allow the `start-ionic-app.command` to run.
4. Double-click `start-ionic-app.command` again.
5. A terminal window will open and attempt to auto-install. The initial install may take ~5 minutes.
6. When installation is complete, a new browser window to [http://localhost:8200](http://localhost:8200) will automatically open displaying the We.Retail Ionic app in Ionic's mobile-preview framework (iOS and Android).

### Windows

1. Download and unzip `aem-hybrid-cms_ionic-app-x.x.x.zip` to your macOS machine.
2. Double-click on `start-ionic-app.bat`.
3. With the first double-click, you will be prompted by Windows to allow the program to run. Accept this prompt.
4. A command window will open and attempt to auto-install. The initial install may take ~5 minutes.
5. When installation is complete, a new browser window to [http://localhost:8200](http://localhost:8200) will automatically open displaying the We.Retail Ionic app in Ionic's mobile-preview framework (iOS and Android).
  
## Ionic app manual setup (macOS or Windows)

If the automatic installation does not succeed, the Ionic app can be started manually.

1. Open a terminal (macOS) or command prompt (Windows) window.
2. Navigate to the unzipped folder: `../aem-hybrid-cms_ionic-app-x.x.x.zip/ionic-we-retail`
3. One IN the `ionic` folder execute, and verify the results the these commands.
4. `node -v` -> `8.x.x` (or `10.x.x`)
	* If `node` command  cannot be found, re-install NodeJS (see above).
5. `npm -v` -> `6.4.x` or greater
	* If `npm` command  cannot be found, re-install NodeJS as NodeJS includes NPM (see above).
6. `npm -g install ionic phonegap`
7. `npm install`
	* This will be very verbose. 
8. `ionic serve --lab`	 
9. 6. When installation is complete, a new browser window to [http://localhost:8200](http://localhost:8200) will automatically open displaying the We.Retail Ionic app in Ionic's mobile-preview framework (iOS and Android).

## Experience Fragment Demo

The Experience Fragment used in the demo is located at: [http://localhost:4502/editor.html/content/experience-fragments/we-retail/spotlight/current-spotlight/master.html](http://localhost:4502/editor.html/content/experience-fragments/we-retail/spotlight/current-spotlight/master.html)

Several variations have been added:

* **Master** - Master variation of the Experience fragment. Uses the Building Block feature to keep the image in sync between the sub variations.
* **Summary** - Live copy variation of the Master variation
* **Facebook Promo** - Facebook variation using the Facebook social media template
* **Pinterest Promo** - Pinterest variation using the Pinterest social media template

#### Add the Experience Fragment to a Sites Page

1. Navigate to a page in We.Retail i.e [http://localhost:4502/editor.html/content/we-retail/us/en.html](http://localhost:4502/editor.html/content/we-retail/us/en.html)
2. Add an Experience Fragment Component to the page.
3. Choose the Summary variation in the component dialog.

#### View the Experience Fragment on the mobile App

1. Open the Ionic App at [http://localhost:8200](http://localhost:8200)
2. The home page of the ionic App displays the Summary variation of the Experience fragment

#### Update the Experience Fragment

1. Open the Master variation at [http://localhost:4502/editor.html/content/experience-fragments/we-retail/spotlight/current-spotlight/master.html](http://localhost:4502/editor.html/content/experience-fragments/we-retail/spotlight/current-spotlight/master.html)
2. Change the image on the master variation.
3. Click the Summary, Facebook, and Pinterest variations to see the updated image.
4. On the Summary Variation
	1. Click the text paragraph and cancel the inheritance by clicking the chain link.
	2. Modify the paragraph to make it shorter. 
5. View the We.Retail Sites page and the Ionic Mobile App to see the updated image and text summary.

## Experience Fragment Demo - Dynamic Media Variation

#### Pre-requisites

The following is needed, instructions for setup can be found on Demo Utils.

* AEM running with Dynamic Media
* AEM Smart Crop Image presets created and applied to several images

1. Open the Master variation at [http://localhost:4502/editor.html/content/experience-fragments/we-retail/spotlight/current-spotlight/master.html](http://localhost:4502/editor.html/content/experience-fragments/we-retail/spotlight/current-spotlight/master.html)
2. Choose an image that has Smart Crop variations applied and update the image on the Master variation.
3. Open the Smart Image Component. Under the Dynamic Media Settings tab > toggle on Smart Crop
	![smart crop image dialog](./hybrid-cms/images/dynamic-media-component.png)
4. Use AEM's responsive emulator to see the updated crops
5. Refresh the Ionic App to see updated image that has been smart cropped

> Note* By default the Ionic App will attempt to render a Smart Crop variation named **Small**. You can override the Smart Crop variation used by filling out the **Mobile Crop Name** in the Smart Image Component dialog.

## Content Fragment Demo

There are 2 models used in the Content Fragment Demo

* Athletes
* FAQs

#### Content Fragment Models

* The Athlete Content Fragment Model is located at: [http://localhost:4502/mnt/overlay/dam/cfm/models/editor/content/editor.html/conf/we-retail/settings/dam/cfm/models/athlete](http://localhost:4502/mnt/overlay/dam/cfm/models/editor/content/editor.html/conf/we-retail/settings/dam/cfm/models/athlete)
* The FAQ Content Fragment Model is located at: [http://localhost:4502/mnt/overlay/dam/cfm/models/editor/content/editor.html/conf/we-retail/settings/dam/cfm/models/faq](http://localhost:4502/mnt/overlay/dam/cfm/models/editor/content/editor.html/conf/we-retail/settings/dam/cfm/models/faq)

#### Content Fragments

Several Content Fragments have already been created based on the models.

* We.Retail Athletes [http://localhost:4502/assets.html/content/dam/we-retail/en/athletes](http://localhost:4502/assets.html/content/dam/we-retail/en/athletes)
* FAQs [http://localhost:4502/assets.html/content/dam/we-retail/en/faqs](http://localhost:4502/assets.html/content/dam/we-retail/en/faqs)

#### Content Fragment Models on Sites Page and SPA Editor

The We.Retail Athletes are used on the AEM Sites page beneath the Athletes page: [http://localhost:4502/editor.html/content/we-retail/us/en/athletes.html](http://localhost:4502/editor.html/content/we-retail/us/en/athletes.html)

The same content fragments are referenced on the We.Retail SPA located at: [http://localhost:4502/editor.html/content/we-retail-spa/react/en/athletes.html](http://localhost:4502/editor.html/content/we-retail-spa/react/en/athletes.html)

#### Content Services API Pages

Part of the Ionic App is driven AEM Content Services. AEM Content Services is designed to expose AEM content for the headless use case. This demo uses the Content Fragment List component to collect a list of Content Fragments.

[http://localhost:4502/editor.html/content/we-retail/api/athletes.html](http://localhost:4502/editor.html/content/we-retail/api/athletes.html) - This is the endpoint that drives the Featured Athletes view of the Ionic app.

![Featured Athletes](./hybrid-cms/images/featured-athletes.png)

1. Click the Content Fragment List component and open the dialog.
2. Update the dialog and add the We.Retail > Athletes > Featured Athletes tag to further filter the list by only Featured Athletes.

	![Content Fragment Model](./hybrid-cms/images/cflist-dialog.png)

3. Saving the dialog will filter the list to only Content Fragments tagged with Featured Athletes.
4. Refreshing the Ionic App the Featured Athlete list will be filtered.

[http://localhost:4502/editor.html/content/we-retail/api/faq.html](http://localhost:4502/editor.html/content/we-retail/api/faq.html) - This is the endpoint that drives the FAQs (Read Only) view of the Ionic App and serves as another example.

#### Assets HTTP API

The Assets HTTP API is intended to be used to provide Create, Review, Update, and Delete (CRUD) operations for AEM Assets.
The HTTP API does not require any proxy pages to be created and can be called directly by 3rd party applications.
The Ionic FAQs (CRUD) view allows you to Create a new FAQ, Update existing FAQs, and delete an FAQ.

*The Assets HTTP API is intended to be used for CRUD operation, whereas READ-only operations, such as syndicating content from AEM to external Applications and System, should use the AEM Headless approach using Proxy page.*

![FAQ CRUD](./hybrid-cms/images/faq-crud.png)

![FAQ Delete](./hybrid-cms/images/delete-faq.png)

*Delete FAQs by sliding the item to the left in the List view*

The updated FAQ content fragment models will be reflected in AEM under: [http://localhost:4502/assets.html/content/dam/we-retail/en/faqs](http://localhost:4502/assets.html/content/dam/we-retail/en/faqs)

## Reviewing the JSON End-points

The Hybrid App accesses the JSON via:

* **Experience Fragment** via direct HTTP call to AEM
    * [http://localhost:4502/content/experience-fragments/we-retail/spotlight/current-spotlight/mobile/_jcr_content/root.mobile.html?wcmmode=disabled](http://localhost:4502/content/experience-fragments/we-retail/spotlight/current-spotlight/mobile/_jcr_content/root.mobile.html?wcmmode=disabled)
* **FAQs** via AEM Assets HTTP API (direct access into AEM Assets folder structure)
    * [http://localhost:4502/api/assets/we-retail/en/faqs.json](http://localhost:4502/api/assets/we-retail/en/faqs.json)
* **FAQ API** via AEM Content Services Proxy Pages & the Content Fragment List component
    * [http://localhost:4502/content/we-retail/api/faq/_jcr_content/root/contentfragmentlist.model.json](http://localhost:4502/content/we-retail/api/faq/_jcr_content/root/contentfragmentlist.model.json)
* **Athletes API** via AEM Content Services Proxy Pages & the Content Fragment List component
    * [http://localhost:4502/content/we-retail/api/athletes/_jcr_content/root/contentfragmentlist.model.json](http://localhost:4502/content/we-retail/api/athletes/_jcr_content/root/contentfragmentlist.model.json)


## Additional Resources

* [Hybrid CMS Demo on DemoHub](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-hybrid-cms.html)
* [Hybrid CMS White Paper](https://www.adobe.com/content/dam/www/us/en/marketing/experience-manager-sites/headless-content-management-system/pdfs/aem-hybrid-architecture-wp-1-18-19.pdf)
* [Hybrid CMS PPT](https://internal.adobedemo.com/content/dam/demo-hub/demos/aem-hybrid-cms/assets/AEM+Omnichannel+Experiences%20(1).pptx)