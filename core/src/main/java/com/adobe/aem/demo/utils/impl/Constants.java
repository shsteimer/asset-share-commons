package com.adobe.aem.demo.utils.impl;

public class Constants {

    public static final String NA = "na";
    public static final String EMEA = "emea";
    public static final String APAC = "apac";

    public static final String SUCCESS_URL = "/apps/demo-utils/instructions/install/success.html";
    public static final String FAILURE_URL = "/apps/demo-utils/instructions/install/failure.html";

    public static final String[] EXCLUDE_DAM_PATH_PREFIXES = {
            "/content/dam/_",
            "/content/dam/assetinsights/",
            "/content/dam/catalogs/",
            "/content/dam/collections/",
            "/content/dam/dam:batch/",
            "/content/dam/formsanddocuments/",
            "/content/dam/formsanddocuments-themes/",
            "/content/dam/jcr:content",
            "/content/dam/templates/",
            "/content/dam/we-retail-screens/",
    };

    private Constants() {
    }

}
