### Reset Smart Tagging and Training

Smart Tagging is now isolated to your specific AEM instance (not shared across demoers). Smart Tags for the AEM instance can be reset in the following ways:

* **Reset Tagging Status:** Once an asset is smart tagged, it cannot not be smart tagged again for 24 hours. Resetting the **tagging status** allows the asset to be smart tagged immediately.
* **Reset Training Status:** Once an asset has been trained for smart tagging, it cannot trained again for 24 hours. Resetting the **training status** allows the asset to be re-trainined immediately.
* **Full Reset of Training & Tagging:** Prior trainings are stored in the Adobe Cloud for this AEM instance. To reset all training, a full reset of Smart Tag training is required. This also resets the **training status** and **tagging status** for assets.
    * Smart Tags must be set up, otherwise clicking this button will result in an error.

<a href="/apps/demo-utils/instructions/smart-tags.reset.html?tagging" class="button">Reset Tagging Status</a>
<a href="/apps/demo-utils/instructions/smart-tags.reset.html?training" class="button">Reset Training Status</a>
<a href="/apps/demo-utils/instructions/smart-tags.reset.html?all" class="button">Full Reset of Training & Tagging</a>


## Troubleshooting Smart Tags Training

1. Verify the Smart Tags configs are correct via the <strong><a x-cq-linkchecker="skip" target="_blank" href="/system/console/jmx/com.day.cq.dam.similaritysearch.internal.impl%3Atype%3Dsimilaritysearch">Similarity Search JMX console</a> > validateConfigs() > Invoke</strong>. Ensure all response entries are successful.
1. The first time training is run for a tenant, it **MUST** be on two sets of images each with their OWN distinct tags. If that's not how the first training was executed, reset the tenant (using the buttons above) and re-train using this approach.
1. Create a new **[Smart Tags Training report](/mnt/overlay/dam/gui/content/reports/reportlist.html) > Create**. If the training report is empty for the trained tags, re-run the report every 5 minutes until there is report data for the applied tags.

## Demos

* [Smart Tags](https://internal.adobedemo.com/content/demo-hub/en/demos/external/smart-tags-2-0.html)
* [Custom Smart Tags (with Training)](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-assets-custom-smart-tags.html)

## Other Materials

* Videos
    * [Smart Tags with Training Video](https://helpx.adobe.com/experience-manager/kt/assets/using/enhanced-smart-tags-feature-video-use.html)
    * [Set up Smart Tags with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/smart-tags-technical-video-setup.html)
    * [Showing Smart Tags Scores](https://helpx.adobe.com/experience-manager/kt/assets/using/smart-tags-technical-video-setup.html#ShowingSmartTagsscoresforinstructionalpurposes)
    * [Understanding Smart Tags in AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/smart-tags-feature-video-understand.html)
* Adobe Docs
    * [Configuring Smart Tags](https://helpx.adobe.com/experience-manager/6-4/assets/using/config-smart-tagging.html)
    * [Smart Tags Training Guidelines](https://helpx.adobe.com/experience-manager/6-4/assets/using/smart-tags-training-guidelines.html)
    * [Enhanced Smart Tags](https://helpx.adobe.com/experience-manager/6-4/assets/using/enhanced-smart-tags.html#TrainingtheSmartContentService)
* Adobe Medium Blog post
    * [Effic