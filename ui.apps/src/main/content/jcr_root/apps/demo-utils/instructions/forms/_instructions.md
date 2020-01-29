# AEM  Forms

### AEM 6.5+ is required


## Set up of AEM Forms

### Install AEM Forms

> It is expected that the **AEM** instance is running on port **4502**. 

1. Download the appropriate **AEM Forms Add On** Package based on your operating system:
	* <a href="https://link.enablementadobe.com/forms-aemfd-osx" target="_blank" class="button">Mac OS</a>
	* <a href="https://link.enablementadobe.com/forms-aemfd-win" target="_blank" class="button">Windows</a>  

2.  Open [package manager](http://localhost:4502/crx/packmgr/index.jsp)

3. Upload the package you downloaded in the previous step

4. Click on the package and click install to install the **AEM Forms Addon Package**

> The installation takes a while to complete. Please be patient. Once the installation is complete, you will be prompted to restart the AEM instance. **Please follow the following steps before re-starting the server**

### Update sling.properties

1. Open the [Bundles Web Console](http://localhost:4502/system/console/bundles) 
2. Make sure all the bundles are in **active** or **resolved** state. This could take a few minute, please be patient. 
3. Once all the bundles are in active or resolved state, **stop** the AEM server.
4. Open the sling.properties file.It is located in `<AEM Install Folder>\crx-quickstart\conf` folder. 
5. Add the following lines to the  end of the file 

  ```
  sling.bootdelegation.class.com.rsa.jsafe.provider.JsafeJCE=com.rsa.*
  sling.bootdelegation.class.org.bouncycastle.jce.provider.BouncyCastleProvider=org.bouncycastle.*
  ```
6. Save the **sling.properties** files

### Restart AEM from the command line

> Forms requires more memory allocated to the JVM, if running forms you will need to start AEM from the command line from now on:

1. Open a command prompt and navigate to the folder containing the AEM JAR file. 
2. Type in the following command to start the AEM instance:

   ```
   java -jar -Xmx2048M <Name-of-your-AEM-JAR-file>.jar -gui
   ```

  

  
