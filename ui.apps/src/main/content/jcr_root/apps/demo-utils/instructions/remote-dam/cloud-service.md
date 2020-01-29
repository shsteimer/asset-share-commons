# AEM Connected Assets

> AEM Connected Assets is supported for AEM as a Cloud Service for the following:
>
>  AMS or On Prem AEM Sites connecting to AEM as a Cloud Service Assets.

<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/26060/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen 
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

## Set up of Connected Assets feature

To use this feature you need two instances of AEM running. To avoid confusion we will refer to the **Sites** instance as the AEM author environemnt in which you will *access* the Connected Assets. The **Assets** instance, also an AEM author environment, *is* the Connected Assets.

* It is expected that the **Sites** instance is running on port **4502**. 
* It is expected that the **Assets** instance is running on port **4504**.

### Installation of AEM Instances

1. Get a copy of the AEM 6.5 JAR and license.properties file.
2. Copy the jar and license and create a folder structure like the following, renaming the jar to ensure AEM runs on the appropriate port:

```plain
/aem
	/sites
		+ aem65-author-p4502.jar
		+ license.properties
	/assets
		+ aem65-author-p4504.jar
		+ license.properties
```

3. Double+Click the jars to install.
4. Install Demo Utils to **both** instances

### Automatic setup of Connected Assets

On the **Sites** instance (port 4502) click the button below to automatically set up the Connected Assets feature:

<a href="/apps/demo-utils/instructions/remote-dam.install.html" class="button">Automatic Setup</a> 

> Ensure that DemoUtils is also installed on the **Assets** instance.

## How to use

1. On the **Assets** instance upload a net-new image to the DAM (ensure its not an image that exists on the **Sites** instance).
2. In a new browser, log in to the **Sites** instance and navigate to a page using the Sites Editor.
3. Open the Content Finder and click the Connected Assets icon (*cloud* icon).

![Connected Assets browser](./remote-dam/images/content-finder.png)

## Other materials

* [Using Connected Assets with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/connected-assets-feature-video-use.html)




