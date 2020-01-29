AEM Assets Visual Search, or Find Similar, leverages Adobe Sensei intelligence to locate image assets in the DAM that are visually similar to a selected image.

<iframe width="854" height="480" src="https://video.tv.adobe.com/v/29132/?quality=12&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

Visual Search piggybacks on the AEM Assets Smart Tag integration. When an image asset is processed for Smart Tags buy the Adobe I/O Smart Content Services, not only does it write back the Smart Tag keywords, it also writes back special "feature vector" data, which is representation of the image as a mathematical vector (you can think to it as a point in space). Images having similar contents will be represented by vectors lying close in the vector space.

Note that Visual Search does NOT simply compare Smart Tag values (ie. the keywords applied by Smart Tags), but rather they compare images that have similar visual characteristics that are identified by Adobe Sensei, and uses advances mathematics to compare image compositions for similarities.

<!-- CLOUD-SERVICE_INSTRUCTIONS -->

<!-- QUICKSTART_INSTRUCTIONS -->
