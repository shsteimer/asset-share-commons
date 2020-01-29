### Download and Install

1. Download:
	* <a href="https://link.enablementadobe.com/demo-wknd-events-all" target="_blank" class="button">com.adobe.aem.guides.wknd-events.all-X.X.X.zip</a>
2. Navigate to AEM > Tools > Deployment > Packages
3. Click on **Upload Package**. Upload and Install the file from Step 1.
	* com.adobe.aem.guides.wknd-events.all-X.X.X.zip
4. Watch the demo video for more instructions on how to demo the capability.

## Front End Development

Front end developers will want to work outside of AEM using a local node server for rapid development. The content (JSON model) from AEM will be proxied into the application.

To setup:

1. The following technologies and tools are needed to demo the React SSR capability locally:
	* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
	* [Apache Maven](https://maven.apache.org/) (3.3.9 or newer)
	* [Node.js v10+](https://nodejs.org/en/)
	* [npm 6+](https://www.npmjs.com/)
2. Download and unzip the source:
	* <a href="https://link.enablementadobe.com/spa-editor-src" target="_blank" class="button">aem-guides-wknd-events-SRC.zip</a>
3. Open a new terminal window and navigate to the root folder of the unzipped file from step 2.
4. Install the entire project with the following command:

```shell
$ mvn -PautoInstallPackage -Padobe-public clean install
```

5. In the terminal navigate to the sub-folder: `react-app`
6. Run the following command to start the local development server:

```shell
$ npm run start
```

7. Open a browser and navigate to [/aem/start.html](/aem/start.html), login as `admin:admin`.
8. In the **same** browser open a new tab and navigate to: [http://localhost:3000/content/wknd-events/react/home.html](http://localhost:3000/content/wknd-events/react/home.html)
9. Open the source code in an IDE like **Visual Studio Code**.
10. Make a change to a file like `<scr-code-location>demo-spa-editor-x.x.x/react-app/src/styles/_variables.scss` changing the primary color from yellow to blue (line 37):

	```diff
	- $color-primary:       $color-yellow;
	+ $color-primary: 		blue;
	```

11. The browser should automatically refresh with the changes:

	![fed auto reload](./spa-editor/images/fed-auto-reload.gif)

12. The **content** is being proxied in directly from AEM. You can make changes to the content on the AEM side (http://localhost:4502) and see them reflected in the development server (http://localhost:3000).

## Server Side Rendering (SSR) Setup

Note that SSR is currently in **Technical Preview** and only works for the **React** version of the app. Watch the demo video above (~17:00) to see how to demo.

1. The following technologies and tools are needed to demo the React SSR capability locally:
	* [Java 1.8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
	* [Apache Maven](https://maven.apache.org/) (3.3.9 or newer)
	* [Node.js v10+](https://nodejs.org/en/)
	* [npm 6+](https://www.npmjs.com/)
2. Download and unzip the source:
	* <a href="https://link.enablementadobe.com/spa-editor-src" target="_blank" class="button">aem-guides-wknd-events-SRC.zip</a>
3. Open a new terminal window and navigate to the root folder of the unzipped file from step 2.
4. Install the entire project with the following command:

```shell
$ mvn -PautoInstallPackage -Padobe-public clean install
```

5. In the terminal navigate to the sub-folder: `<src-code-location>/demo-spa-editor-x.x.x/react-app`
6. Start the express server with the following command:

```shell
$ npm run start:server
```

7. Open a new browser and navigate to: [http://localhost:4502/content/wknd-events/react/home.html?wcmmode=disabled](http://localhost:4502/content/wknd-events/react/home.html?wcmmode=disabled)



