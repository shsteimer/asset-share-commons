# AEM Smart Translation Search

> Watch an video of Smart Translation Search: (~2min)

<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="   https://video.tv.adobe.com/v/21297/?quality=9&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen 
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

> Note this is best demo'd with Smart Tags, as (non-custom) Smart Tags are only applied in English.

## Set up 

1. Download one of the **pre-configured** language pack to demo (these are LARGE and may take some time to download).

    * [French to English (fr-en.zip)](https://adobe.sharepoint.com/:u:/s/aemtechnicalmarketing/EY2B-gMhQNJNhzQkdGC_480BTP5EFDzIhsWOlmoIlv83iA?e=Z5kiu8) - 3.93GB 
    * [Italian to English (it-en.zip)](https://adobe.sharepoint.com/:u:/s/aemtechnicalmarketing/EY2B-gMhQNJNhzQkdGC_480BTP5EFDzIhsWOlmoIlv83iA?e=Z5kiu8) - 3.82GB  
    * [German to English (de-en.zip)](https://adobe.sharepoint.com/:u:/s/aemtechnicalmarketing/EZZEASjVWHlEgRB1sw1f43MBhpCk3nKS7f1z0EAU2HlU_w?e=CZ65aW) - 3.92GB
    * [Spanish to English (es-en.zip)](https://adobe.sharepoint.com/:u:/s/aemtechnicalmarketing/EYgdhbrYEJVPtVnapGHKsWIBkYxuOTRNKDv9GuVfJUdDaw?e=F6GeHC) - 4.79GB 

2. **Stop** AEM
3. Unzip the pre-configured language package from above (ie. `es-en.zip`) into `<your-aem-folder>/crx-quickstart/opt` resulting in `<your-aem-folder>/crx-quickstart/opt/es-en`.
4. Restart AEM from the command prompt allocating 6GB of memory.
	* `java -jar -Xmx7g cq-author-4502.jar`
	* If you are using a Dynamic Media run mode, ensure this is maintained, ie.  `java -jar -Xmx6g cq-author-dynamicmedia_scene7-4502.jar`
	* *Its recommended you only demo using 1 language pack, and each language pack requires an additional ~5GB of memory allocation; for example use BOTH French and Spanish language packs, a an extra ~10GB of memory would need to be allocated, and AEM would have to be started with `-Xmx12g` which may overwhelm personal laptops. This is possible, however ensure you have enough available memory on your machine to accomodate.*
5. And finally click below... <br/> <a href="/apps/demo-utils/instructions/smart-translation-search.install.html" class="button">Auto-configure Smart Translation Search</a>

## Other language packs

If other language packs are required, they can be downloaded and configured manually. The 4 language packages above are pre-configured for easy installation.

* [Apache Joshua Language Packs](https://cwiki.apache.org/confluence/display/JOSHUA/Language+Packs)
* [Set up instructions](https://helpx.adobe.com/experience-manager/kt/assets/using/smart-translation-search-technical-video-setup.html)

----

## Demos

* [Smart Translation Search](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-smart-translation-search.html)
    * *Note this is best demo'd with Smart Tags, as (non-custom) Smart Tags are only applied in English.*

## Other materials

* Videos
    * [Set up Smart Translation Search with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/smart-translation-search-technical-video-setup.html)
    * [Using Smart Translation Search with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/smart-translation-search-feature-video-use.html)
* Adobe Docs
    * [Apache Oak Machine Translation Search Docs](http://jackrabbit.apache.org/oak/docs/query/search-mt.html)
* Downloads
    * [List of all Apache Joshua Language Packs](https://cwiki.apache.org/confluence/display/JOSHUA/Language+Packs)
    * [oak-search-mt bundle download](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.apache.jackrabbit%22%20AND%20a%3A%22oak-search-mt%22) *Download the version that matches your Oak Core bundle*
