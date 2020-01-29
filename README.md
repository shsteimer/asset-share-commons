## AEM as Cloud Service Demo Utils

## Building

This project uses Maven for building. Common commands:

From the root directory, run ``mvn -PautoInstallPackage clean install`` to build the bundle and content package and install to a CQ instance.

From the bundle directory, run ``mvn -PautoInstallBundle clean install`` to build *just* the bundle and install to a CQ instance.

## Releasing

The release of AEM Demo Utils is manual and the artifacts are not deployed to an artifact repository, thus we must skip the deploy step.

The release versions do NOT follow semantic versioning, rather the version is the YYYY.MM.DD of release.

```
$ mvn release:prepare
$ mvn release:perform -Darguments="-Dmaven.deploy.skip=true"
```

Generate the zip file for Demo Utils and replace the existing AEM Package with the latest version :

```
https://dhauthor1.adobedemo.com/assets.html/content/dam/demo-hub/demos/aem-demo-utils/assets/
```

1. Upload and then publish the latest package
2. Unpublish and delete the prior package

Update and Publish AEM Demo Utils Page Properties

```
https://dhauthor1.adobedemo.com/editor.html/content/demo-hub/en/demos/external/aem-demo-utils.html
```

* Page Properties > Demo Hub Configuration Tab > Page Update Date
    * Set to "today"
* Page Properties > Basic > Title
    * Set to "AEM Demo Utils <latest version>

## Updating release tag in Launch

1. Log in to Experience Cloud and select the [Tech Marketing Demos](https://experience.adobe.com/#/@techmarketingdemos/home) organization
2. From the solution switcher, choose [Launch](https://launch.adobe.com/companies/COa4db079ed9594409abc0539cf76b82ca/properties) and select the [AEM Demo Utils - Analytics](https://launch.adobe.com/companies/COa4db079ed9594409abc0539cf76b82ca/properties/PRdbdf6a6e9e824f2dae3e0861504d7bfc/overview) Launch Property
3. Click on the **Rules** Tab and open **AEM Demo Utils Page Load** rule to edit it.
4. Under the Actions list, look for the **Version Checker** action and open to edit it.
5. Open the javascript editor and update the **latestVersion** JS variable to the [current release version of aem-demo-utils](https://git.corp.adobe.com/aem-technical-marketing/aem-demo-utils/releases).
   *   latestVersion = "2020.01.20"
6. Save your changes.
7. Navigate to the **Publishing** tab
8. Add all your changes to a library
9. Build and deploy your library to a development environment.
10. Approve your build from development to staging.
11. Build and deploy your library to the staging environment.
12. If everything looks good, publish your changes from staging to the production environment.

## Releasing AEM Demo Utils as part of All Demos

1. Build the release version of AEM Demo Utils locally
2. Clone the [com.adobe.aem.demo.all-demos Git repo](https://git.corp.adobe.com/aem-technical-marketing/com.adobe.aem.demos.all-demos)
3. Remove any existing version of demo-utils from the all-demos project's `local-maven-repository`
4. Deploy the release AEM Demo Utils `ui.apps` and `ui.content` packages to the all-demos project's `local-maven-repository`
    ```
    mvn deploy:deploy-file -Durl=file:///Users/dgonzale/code/demo/com.adobe.aem.demo.all-demos/local-maven-repository/ -Dfile=/Users/dgonzale/code/demo/com.adobe.aem.demo.demo-utils/ui.content/target/com.adobe.aem.demo.demo-utils.ui.content-2020.01.16-SNAPSHOT.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.content -Dpackaging=zip -Dversion=2020.01.16-SNAPSHOT
    mvn deploy:deploy-file -Durl=file:///Users/dgonzale/code/demo/com.adobe.aem.demo.all-demos/local-maven-repository/ -Dfile=/Users/dgonzale/code/demo/com.adobe.aem.demo.demo-utils/ui.apps/target/com.adobe.aem.demo.demo-utils.ui.apps-2020.01.16-SNAPSHOT.zip -DgroupId=com.adobe.aem.demo -DartifactId=com.adobe.aem.demo.demo-utils.ui.apps -Dpackaging=zip -Dversion=2020.01.16-SNAPSHOT
    ```
5. Commit the changes to the `all-demos` Git repository and push to git.corp.adobe.com
6. See the [com.adobe.aem.demo.all-demos Git repo](https://git.corp.adobe.com/aem-technical-marketing/com.adobe.aem.demos.all-demos) README.md for instructions on how to release all-demos


