package com.adobe.aem.demo.utils.models;

/**
 * Sling Model to expose critical metadata about the current version of AEM / DemoUtils
 **/
public interface VersionMetadata {
    /***
     * @return the current version of DemoUtils installed
     */
    String getUtilsVersion();

}