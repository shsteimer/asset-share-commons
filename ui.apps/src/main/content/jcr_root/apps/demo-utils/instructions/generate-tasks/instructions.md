<iframe title="Adobe Video Publishing Cloud Player" width="854" height="480" src="https://video.tv.adobe.com/v/16831t2/?quality=9&marketingtech.adobe.analytics.additionalAccounts=tmdtmdaemdemoutilsprod" frameborder="0" webkitallowfullscreen
mozallowfullscreen allowfullscreen scrolling="no"></iframe>

## How to use

This package provides an AEM Workflow that can be used to generate a number of Inbox Tasks across a window of time, to help demonstrate the abilities of AEM's calendaring features.

1. On AEM Author, go to <a href="/libs/cq/workflow/admin/console/content/models.html/etc/workflow/models"  target="_blank">Tools > Workflow > Models</a>
2. Select **Generate Tasks** workflow model
3. In the top left, click **Start Workflow**
4. Select a payload, valid options are:
	* AEM Assets folder
	* AEM Sites page
	* The payload dictates the content that is eligible to be randomly selected for association with generated tasks.
5. Optionally, provide the following configuration parameters via the **Comment** text area.
	* **contentTasks**
		* Total number tasks to create
		* Valid values: > 0
		* Default: 25
	* **nonContentTasks**
		* Number of tasks to generate **not** associated with content
		* Valid values: > 0
		* Default: 10
	* **dayRangeStart**
		* 	Number of days relative to today, the earliest generated task may start
		*  Valid values: Negative or positive numbers
		*  Default: -4
	*  **dayRangeEnd**
		* Number od days relative to today, the latest generated task may start
		* Default: 8
	* **maxTaskDuration**
		* Max number of days a task may span
		* Valid values: > 0
		* Default: 5
	* **forceProjectPath**
		* Determines how tasks are associated with AEM Projects
		* Valid values
			* Configuration param does not exist: Content in a folder or page structure covered by an existing AEM Project will be associated with that AEM Project  			
			* Leave value blank: No project association will occur
			* Absolute path to an AEM Project: All tasks will be associate with the provided project
		* Default: Configuration param does not exist 

#### Example configuration provided via Comment text area

```
contentTasks=50
nonContentTasks=12
dayRangeStart=-7
dayRangeEnd=14
maxTaskDuration=7
forceProjectPath=/content/projects/some-project
```

![Screenshot](./generate-tasks/images/screenshot.png)

6. Tap **Run**
7. Open the <a href="/aem/inbox" target="_blank">AEM Inbox</a> to see the generated tasks.


## Automatically remove Tasks

Click the button below to remove all tasks under `/var/taskmanagement/tasks`.

<a href="/apps/demo-utils/instructions/generate-tasks.remove.html" class="button">Remove existing tasks</a>

## Other materials

* Videos
    * [Using the Inbox in AEM](https://helpx.adobe.com/experience-manager/kt/platform-repository/using/inbox-feature-video-use.html)
    * [Using Calendar View with AEM Projects and Inbox](https://helpx.adobe.com/experience-manager/kt/platform-repository/using/projects-and-inbox-calendar-view-feature-video-use.html)
* Adobe Docs
    * [Your Inbox](https://helpx.adobe.com/experience-manager/6-4/sites/authoring/using/inbox.html)



