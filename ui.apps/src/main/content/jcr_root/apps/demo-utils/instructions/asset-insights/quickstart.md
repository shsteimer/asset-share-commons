## Local configurations (not available on AEM as a Cloud Service)

The ranges for both Performance and Usage data can be configured via OSGi configuration.

### Performance data configuration

![Asset Insights - OSGi Performance](./asset-insights/images/osgi-performance.png)

1. Go to the <a href="/system/console/configMgr/com.adobe.aem.demo.utils.assets.insights.impl.MockPerformanceDataServlet" target="aem-osgi" x-cq-linkchecker="skip">AEM OSGi Configuration Console</a>
2. Click on **AEM Demo Utils - Asset Insights - Mock Performance Data Servlet**
3. Configure the min and max values for the random performance data generation
	* **Min Clicks** 
		* Sets the minimum bound for Clicks
	* **Max Clicks**
		* Set the maximum bound for Clicks 
	* *Impressions will always be greater than Clicks, and be computed as (# of Clicks) + random number between min and max*
4. Click **Save** when complete
5. Performance data configuration changes will be reflected immediately on refresh of the Asset > Properties > Insights tab


### Usage and Summary data configuration

![Asset Insights - OSGi Usage](./asset-insights/images/osgi-usage.png)

1. Go to the <a href="/system/console/configMgr/com.adobe.aem.demo.utils.assets.insights.impl.MockAssetInsightsWorkflowProcess" target="aem-osgi" x-cq-linkchecker="skip">AEM OSGi Configuration Console</a>
2. Click on **AEM Demo Utils - Asset Insights - Usage and Summary Performance Generator**
3. Configure the min and max values for the random summary and usage data generation
	* **Min Usage** 
		* Sets the minimum bound for any given Usage score
	* **Max Usage**
		* Sets the maximum bound for any given Usage score
	* **Min Performance Clicks** 
		* Sets the minimum bound for Clicks summary score
	* **Max Performance Clicks**
		* Set the maximum bound for Clicks summary score
	* **Min Performance Impressions** 
		* Sets the minimum bound for Impressions summary score
	* **Max Performance Impressions**
		* Set the maximum bound for Impressions summary score 
4. Click **Save** when complete
5. Usage and Summary data is not reflected immediately and the **Generate Asset Insights Usage Data** must be run again over assets to use the newly configured values.
