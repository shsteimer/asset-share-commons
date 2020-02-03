All Demos consolidates a variety of common demo sites, assets and tools into a single AEM Project that can be deployed to AEM as a Cloud Service via Cloud Manager.
All demos can also be deployed to the local Quickstart Jar using it's **all** package, via Package Manger.

It is important to remember that **any** code deployed to AEM as a Cloud Service **must** originate in an AEM Project that is committed to Git, and pushed into Adobe Cloud Manager for deployment. No code (ie. nothing in `/apps`) can be deployed to AEM as a Cloud Service via Package Manager.

## What's in All Demos?

All Demos includes:

+ Demo Utils for AEM as a Cloud Service
+ WKND Site
+ WKND App (SPA Editor) (formerly known as WKND Events)
+ WKND Mobile (AEM Headless)
+ Core Components Showcase
+ Asset Share Commons

## How to deploy All Demos to AEM as a Cloud Service

<iframe width="854" height="480" src="https://video.tv.adobe.com/v/31473?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

The high-level overview of deploying All Demos to AEM as a Cloud Service.

1. Git **clone** the [All Demos Git Repository](https://git.corp.adobe.com/aem-technical-marketing/com.adobe.aem.demos.all-demos) to your local machine. (Requires Adobe network access)
    ```
    $ git clone https://git.corp.adobe.com/aem-technical-marketing/com.adobe.aem.demos.all-demos
    ```
2. **Add the Adobe Cloud Manager Git repository** as an upstream repository
    ```
    $ cd com.adobe.aem.demos.all-demos

    $ git remote add adobe <Cloud Manager Git Url>

    $ git remote -v
    ```
3. Git **push** the All Demos code base to a branch in the newly added upstream repository
    ```
    $ git push adobe master:all-demos 
    ```
4. Configure an Adobe Cloud Manager deployment pipeline to deploy the branch containing All Demos code
5. Execute the Adobe Cloud Manager pipeline to build and deploy All Demos code to the selected AEM as a Cloud Service environment (this can take 30+ mins)

## Other resources

+ [All Demos Git Repository](https://git.corp.adobe.com/aem-technical-marketing/com.adobe.aem.demos.all-demos)
+ [Download the latest All Demos content package (for Quickstart Jar use only)](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-all-demos.html)

