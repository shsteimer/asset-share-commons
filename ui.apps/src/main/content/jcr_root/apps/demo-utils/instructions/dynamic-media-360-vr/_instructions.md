
<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/26391/?quality=9&autoplay=false&hidetitle=true&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen scrolling="no"></iframe>

360-degree videos, also known as spherical videos are video recordings where a view in every direction is recorded at the same time, shot using an omnidirectional camera or collection of cameras. During playback on a flat display the user has control of the viewing direction, and playback on mobile devices typically leverage built in gyroscope control.

360 Video can be delivered using standard video extensions like .mp4, .mkv, .mov. The most common codec is H.264.

Dynamic Media provides processing of the video asset and special viewers that allow end-user to interact (pan, rotate, etc.) with the 360/VR video.

## 360/VR Demo

1. Ensure that you have completed the steps to [Set up Dynamic Media](/apps/demo-utils/instructions/dynamic-media.html) **before** proceeding. (Sorry if you came here first!)
1. Navigate to AEM Assets directory and create a new folder for 360/VR videos.
   
   ![Create a folder for 360/VR videos](./dynamic-media-360-vr/images/create-folder.png)

2. Open the folder properties and select the **Video Profiles** tab.
3. From the *Profile Name* dropdown, select the **Adaptive Video Encoding** profile.(Adaptive Video Encoding profile is available OOTB)

    ![Adaptive Video Profile](./dynamic-media-360-vr/images/adaptive-video-profile.png)

4. You can notice the *Adaptive Video Encoding* profile name on your folder in card view.

5. Open the folder and upload the [Sample Video file](https://link.enablementadobe.com/demo-aem-dynamic-media-360-vr). This sample file has an aspect ratio of 2:1. By default, video files with an aspect ratio of 2:1 (width:height) will be processed as a 360 video.

> Please allow few minutes for the video file to be completely processed. Processing time may vary for each file depending on its file size. A video thumbnail is generated upon successful processing of your video. This thumbnail image can be customized by a user by selecting a more appropriate thumbnail from the video file.

6. Open the video file and navigate to **Viewers** presets.

![Viewers](./dynamic-media-360-vr/images/viewer-preset.png)

7. For a 360 video file, you can notice the following presets available

![360 and VR Viewers](./dynamic-media-360-vr/images/vr-presets.png)

8. In order to add 360/VR files to a sites page, you need to add the **Video 360 Media** Dynamic Media component to your site template policy and enable it.

9. You should now be able to add the Video 360 Media component to your sites page and render your sample video file. When adding a video file to the component you can choose the preset as either Video360VR or Video360_social.

## Demos

* [Dynamic Media Live Demos](https://landing.adobe.com/en/na/dynamic-media/ctir-2755/live-demos.html)
* [See Adobe Demo Hub for the full collection of demos](http://demo.adobe.com/)

## Other materials

* [Collection of AEM Assets Dynamic Media Videos](http://exploreadobe.com/dynamic-media-upgrade/)
* [Using Dynamic Media 360/VR](https://helpx.adobe.com/experience-manager/6-5/assets/using/360-video.html)
* Adobe Docs
  * [Configuring Dynamic Media Scene7](https://helpx.adobe.com/experience-manager/6-4/assets/using/config-dms7.html)
  * [Managing Image Presets](https://helpx.adobe.com/experience-manager/6-4/assets/using/managing-image-presets.html)
  * [Managing Viewer Presets](https://helpx.adobe.com/experience-manager/6-4/assets/using/managing-viewer-presets.html)
  * [Troubleshooting Dynamic Media Scene7](https://helpx.adobe.com/experience-manager/6-4/assets/using/troubleshoot-dms7.html)
