
## Local Set up

To use this feature you need two instances of AEM running. To avoid confusion we will refer to the **Sites** instance as the AEM author environment in which you will *access* the Connected Assets.

The **Assets** instance, also an AEM author environment, *is* the Connected Assets.

* For this set up the **Sites** instance is running on port **4502**.
* For this set up the **Assets** instance is running on port **4504**.

### Installation of AEM Instances


1. Download the [Quickstart Jar from the AEM SDK](https://downloads.experiencecloud.adobe.com/content/software-distribution/en/aemcloud.html) and have your `license.properties` file handy.
2. Unzip the AEM SDK zip and copy the Quickstart Jar and license.properties and create a folder structure like the following, renaming the jar to ensure AEM runs on the appropriate port:

```plain
/aem-connected-assets
	/sites
		+ aem-author-p4502.jar
		+ license.properties
	/assets
		+ aem-author-p4504.jar
		+ license.properties
```

3. Double-click each `aem-author-pXXXX.jar` to install initiate the install.
4. Install Demo Utils to **both** instances via Package Manager.

### Automatic setup of Connected Assets

On the **Sites** instance (port 4502) click the button below to automatically set up the Connected Assets feature:

<a href="/apps/demo-utils/instructions/remote-dam.install.html" class="button">Automatic Setup</a> 

> Ensure that Demo Utils is also installed on the **Assets** instance.



