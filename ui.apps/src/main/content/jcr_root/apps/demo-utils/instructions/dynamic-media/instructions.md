<form method="post" target="_blank"><input type="hidden" name=":cq_csrf_token"/></form>

<div class="unsupported">
Parts of Dynamic Media currently do not work on AEM as a Cloud Service due to bugs.

Known areas that currently do NOT work include: Viewers, including 360/VR, Preview artwork
</div>

<!-- CLOUD-SERVICE_INSTRUCTIONS -->

<!-- QUICKSTART_INSTRUCTIONS -->

#### Checking the Set up Sync Status

**Syncing** refers to the pushing of resources and files to Scene 7 for processing. 

> Note you don't have to do anything...just wait!

Syncing is *required* for assets, viewers, artwork, etc. to work in the AEM Author preview environment.

*This table will automatically update every 5 seconds after Dynamic Media is configured... note the initial sync can take 15-30 mins to complete.*

<table>
<thead>
<tr>
<th>Files to sync to S7</th>
<th>Number of files left to sync</th>
</tr>
</thead>
<tbody>
    <tr>
        <td>
            /content/dam/_DMSAMPLE
        </td>
        <td>
            <span class="dmSampleStatus">Initializing...</span>
        </td>
    </tr>
    <tr>
        <td>
            /content/dam/_CSS/_OOTB
        </td>
        <td>
             <span class="cssStatus">Initializing...</span>
        </td>
    </tr>
</tbody>
</table>

For the exact paths pending sync, click the button below...

<a href="/.sampleassets.listUnsynced.json" class="button" data-action="form">List un-synced</a>
*A blank response for statuses is good! It means there are no un-synced/activated resources.*

## Checking Dynamic Media Set up Progress

### Checking the Set up Activation Status

**Activation** refers to the pushing fo resources and files to public Dynamic Media Delivery network. 

<table>
<thead>
<tr>
<th>Number of presets/CSS/artwork to activate</th>
</tr>
</thead>
<tbody>
    <tr>
        <td>
            <span class="unactivatedStatus">Initializing...</span>
        </td>
    </tr>
</tbody>
</table>

*This number should be ~323 when the syncing is complete, and the presets/CSS/artwork are ready to be activated.*

<a href="/.sampleassets.listUnactivated.json" class="button" data-action="form">List unactivated</a>
<a href="/.sampleassets.activateOotb.json" class="button" data-action="form">Activate OOTB Presets, CSS and artwork</a>

### Processing assets for Dynamic Media

There is no good way of checking the status of this process other than:

1. Navigate to <a href="/system/console/slingevent" x-cq-linkchecker="skip">/system/console/slingevent</a>
2. Locate the section `Active JobQueue: Granite Transient Workflow Queue`
    * `Jobs` reports number of assets left to process.
    * `Finished Jobs` reports number of assets already processed.
    * (`Average Processing Time` x `Jobs`) / `Active Jobs` should give a rough estimate of the time left to process the remaining jobs.

### Demos and next steps

Other Demo Resources:

* [Smart Crop Demo](https://internal.adobedemo.com/content/demo-hub/en/demos/external/aem-assets-smart-crop.html)
* [Dynamic Media Live Demos](https://landing.adobe.com/en/na/dynamic-media/ctir-2755/live-demos.html)
* [See Adobe Demo Hub for the full collection of demos](http://demo.adobe.com/)

## Other materials

* [Understanding Dynamic Media with AEM Assets](https://helpx.adobe.com/experience-manager/kt/assets/using/dynamic-media-overview-feature-video.html)
* [Collection of AEM Assets Dynamic Media Videos](http://exploreadobe.com/dynamic-media-upgrade/)
* Adobe Docs
    * [Configuring Dynamic Media Scene7](https://helpx.adobe.com/experience-manager/6-4/assets/using/config-dms7.html)
    * [Managing Image Presets](https://helpx.adobe.com/experience-manager/6-4/assets/using/managing-image-presets.html)
    * [Managing Viewer Presets](https://helpx.adobe.com/experience-manager/6-4/assets/using/managing-viewer-presets.html)
    * [Troubleshooting Dynamic Media Scene7](https://helpx.adobe.com/experience-manager/6-4/assets/using/troubleshoot-dms7.html)

</form>

<script type="text/javascript" src="/etc.clientlibs/clientlibs/granite/jquery.js"></script>
<script>

    $(function() {
        $('[data-action="with-redirect"]').click(function(e) {
            var el = $(this);
            e.preventDefault();

            $.getJSON('/libs/granite/csrf/token.json', function(csrf) {
                $.ajax(el.attr('href'), el.attr('method') || 'get', { ':cq_csrf_token': csrf.token }, function() {
                    window.open('/apps/demo-utils/instructions/install/success.html');
                }).fail(function() {
                    window.open('/apps/demo-utils/instructions/install/failure.html');
                });;
            });
        });

        $('[data-action="form"]').click(function(e) {
            var el = $(this);
            e.preventDefault();

            $.getJSON('/libs/granite/csrf/token.json', function(csrf) {
                var form = $('form');
                form.find('[name="\\:cq_csrf_token"]').val(csrf.token);
                form.attr('method', el.data('method') || 'post').attr('action', el.attr('href'));
                form.submit();
            });
        });

        setInterval(function() {
            $.getJSON('/libs/granite/csrf/token.json', function(csrf) {
                $.post('/.sampleassets.listUnsynced.json', {':cq_csrf_token': csrf.token}, function (data) {
                    var lines = data.split('\n'),
                        dmSampleCount = 0,
                        cssCount = 0;

                    $.each(lines, function (index, line) {
                        if (line.indexOf('/content/dam/_DMSAMPLE/') > -1) {
                            dmSampleCount++;
                        } else if (line.indexOf('/content/dam/_CSS/') > -1) {
                            cssCount++;
                        }
                    });

                    $('.dmSampleStatus').text(dmSampleCount);
                    $('.cssStatus').text(cssCount);

                }).fail(function (data) {
                    if (!data || data.indexOf('EofException') === -1) {
                        $('.dmSampleStatus').text('Dynamic Media is not configured');
                        $('.cssStatus').text('Dynamic Media is not configured');
                    }
                }).always(function () {
                    $.post('/.sampleassets.listUnactivated.json', {':cq_csrf_token': csrf.token}, function (data) {
                        var lines = data.split('\n'),
                            unActivatedCount = 0;

                        $.each(lines, function (index, line) {
                            if (line.trim() !== '') {
                                unActivatedCount++;
                            }
                        });

                        $('.unactivatedStatus').text(unActivatedCount);
                    }).fail(function (data) {
                        if (!data || data.indexOf('EofException') === -1) {
                            $('.unactivatedStatus').text('Dynamic Media is not configured');
                        }
                    });
                });
            })
        }, 5000);
    });
</script>

