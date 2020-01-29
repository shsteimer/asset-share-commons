## Smart Crop Demo

1. Ensure that you have completed the steps to [Set up Dynamic Media](/apps/demo-utils/instructions/dynamic-media.html) **before** proceeding. (Sorry if you came here first!)
2. Navigate to the Image Profile Page: **Tools** > **Assets** > **<a href="/mnt/overlay/dam/gui/content/processingprofilepage/imageprocessingprofiles.html/conf/global/settings/dam/adminui-extension/imageprofile" target="_blank">Image Profiles</a>**
3. Click on the **Create** button and create a new image profile
   1. Select the cropping options as: `Smart Crop`
   2. Configure your options for Responsive Image Crop
   3. Save your changes

    ![Smart Crop Image profile](./smart-crop/images/smart-crop-image-profile.png)

4. Select the newly created Image Profile and assign it to an **Assets Folder** that contains images to smart crop.

    ![Image profile assign](./smart-crop/images/apply-processing-folder.png)

5. From the AEM Start menu navigate to **Assets** > **Files** > and find the the folder selected in the previous step and you should be able to notice the processing profile applied:

    ![smart crop](./smart-crop/images/surfing-folder-smart-crop.png)

6. Select the folder (or specific assets that are in the smart cropped folder) and in the top action bar, click **Reprocess Assets**. Note this will ONLY process assets in the immediate folder and NOT in sub-folders.
7. Wait for the assets to complete processing (the Processing badge should disappear from the asset cards)
8. Navigate into the folder and you should be able to select and asset and then select the smart crop button to see the crops:

    ![smart crop button](./smart-crop/images/smart-crop-button.png)

9. Any new assets uploaded to this folder will be automatically Smart Cropped.
