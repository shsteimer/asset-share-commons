package com.adobe.aem.demo.utils.tasks;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.granite.workflow.WorkflowException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

public interface InboxTasks extends Executable {
    void generateInboxTasks(ResourceResolver resourceResolver, ValueMap params) throws WorkflowException;
}
