package com.adobe.aem.demo.utils.models;

/**
 * Instructions Markdown to HTML SLing Model
 **/
public interface MarkdownWrapper {
    /***
     * @return the HTML rendition of the instructions.md markdown file.
     */
    String getInstructions();

    String getCloudServiceInstructions();

    String getQuickstartInstructions();
}