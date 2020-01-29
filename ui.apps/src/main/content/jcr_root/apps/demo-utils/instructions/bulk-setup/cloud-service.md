<form method="get" action="/apps/demo-utils/instructions/bulk-setup.install.html">

<label for="region"><strong>Region</strong></label>
<select name="region">
    <option value="na" selected>North America</option>
    <option value="emea">EMEA</option>
    <option value="apac">APAC</option>
</select>

<dl>

<dt><h4>Automatic configurations</h4></dt>

<!--
<dd>
    <input type="checkbox" id="adobe-stock" name="adobe-stock" value="install" checked/>
    <label for="adobe-stock">Adobe Stock integration</label>
</dd>
-->

<dd>
    <input type="checkbox" id="smart-tags" name="smart-tags" value="install" checked/>
    <label for="smart-tags">Smart Tags</label>
</dd>

<dd>
    <input type="checkbox" id="adobe-asset-link" name="adobe-asset-link" value="install"/>
    <label for="adobe-asset-link">Adobe Asset Link <em>(requires one-time manual extension set up)</em></label>
</dd>

<dd>
    <input type="checkbox" id="dynamic-media-scene7" name="dynamic-media-scene7" value="install"/>
    <label for="dynamic-media-scene7">Dynamic Media</label>
</dd>

<dt><h4>Automatic actions</h4></dt>

<dd>
    <input type="checkbox" id="asset-insights" name="asset-insights" value="apply" checked/>
    <label for="asset-insights">Generate Asset Insights <em>(for all assets)</em></label>
</dd>

<dd>
    <input type="checkbox" id="inbox-tasks" name="inbox-tasks" value="apply" checked/>
    <label for="inbox-tasks">Generate Inbox Tasks <em>(using assets as payloads)</em></label>
</dd>

</dl>

<input type="submit" value="Perform the Bulk Set up" class="button"/>
</form>