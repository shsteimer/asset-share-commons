package com.adobe.aem.demo.utils.tasks.impl;

import com.adobe.aem.demo.utils.tasks.InboxTasks;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Component(
        service = {WorkflowProcess.class},
        property = {
                "process.label=AEM Demo Utils - Task Generator"
        }
)
public class GenerateRandomTasks implements WorkflowProcess {
    private static final Logger log = LoggerFactory.getLogger(GenerateRandomTasks.class);

    private static final String PARAM_CONTENT_TASKS = "contentTasks";
    private static final String PARAM_NON_CONTENT_TASKS = "nonContentTasks";
    private static final String PARAM_DAY_RANGE_START = "dayRangeStart";
    private static final String PARAM_DAY_RANGE_END = "dayRangeEnd";
    private static final String PARAM_MAX_TASK_DURATION = "maxTaskDuration";
    private static final String PARAM_FORCE_PROJECT_PATH = "forceProjectPath";

    private static final int MAX_CONTENT_TASKS = 15;
    private static final int MAX_NON_CONTENT_TASKS = 10;
    private static final int DEFAULT_DAY_RANGE_START = -4;
    private static final int DEFAULT_DAY_RANGE_END = 8;
    private static final int MAX_TASK_DURATION = 5;

    @Reference
    private InboxTasks inboxTasks;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {

        final WorkflowData workflowData = workItem.getWorkflowData();
        final String type = workflowData.getPayloadType();

        // Check if the payload is a path in the JCR; The other (less common) type is JCR_UUID
        if (!StringUtils.equals(type, "JCR_PATH")) {
            return;
        }

        ResourceResolver resourceResolver = workflowSession.adaptTo(ResourceResolver.class);

        try {
            log.info("Generating tasks for [ {} ]", workflowData.getPayload().toString());
            String paramsStr = getParamsFromWorkflowComment(resourceResolver, workItem);

            final String path = workflowData.getPayload().toString();

            int contentTaskLimit = parseParamInt(paramsStr, PARAM_CONTENT_TASKS, MAX_CONTENT_TASKS);
            log.debug("Content Task limit [ {} ]", contentTaskLimit);

            int nonContentTaskLimit = parseParamInt(paramsStr, PARAM_NON_CONTENT_TASKS, MAX_NON_CONTENT_TASKS);
            log.debug("Non-content Task limit [ {} ]", nonContentTaskLimit);

            int dayRangeStart = parseParamInt(paramsStr, PARAM_DAY_RANGE_START, DEFAULT_DAY_RANGE_START);
            log.debug("Day Range Start [ {} ]", dayRangeStart);

            int dayRangeEnd = parseParamInt(paramsStr, PARAM_DAY_RANGE_END, DEFAULT_DAY_RANGE_END);
            log.debug("Day Range End [ {} ]", dayRangeEnd);

            int maxTaskDuration = parseParamInt(paramsStr, PARAM_MAX_TASK_DURATION, MAX_TASK_DURATION);
            log.debug("Max Task Duration [ {} ]", maxTaskDuration);

            String forceProjectPath = parseParamString(paramsStr, PARAM_FORCE_PROJECT_PATH, null);
            log.debug("Force project path [ {} ]", forceProjectPath);

            ValueMap params = new ValueMapDecorator(new HashMap<>());
            params.put("path", path);
            params.put(PARAM_CONTENT_TASKS, contentTaskLimit);
            params.put(PARAM_NON_CONTENT_TASKS, nonContentTaskLimit);
            params.put(PARAM_DAY_RANGE_START, dayRangeStart);
            params.put(PARAM_DAY_RANGE_END, dayRangeEnd);
            params.put(PARAM_MAX_TASK_DURATION, maxTaskDuration);
            params.put(PARAM_FORCE_PROJECT_PATH, forceProjectPath);

            inboxTasks.generateInboxTasks(resourceResolver, params);

        } catch (Exception e) {
            log.error("Unable to complete processing the Workflow Process step", e);

            throw new WorkflowException("Unable to complete processing the Workflow Process step", e);
        }
    }

    private String getParamsFromWorkflowComment(ResourceResolver resourceResolver, WorkItem workItem) {
        log.debug("Workflow Instance [ {} ]", workItem.getWorkflow().getId());
        Resource resource = resourceResolver.getResource(workItem.getWorkflow().getId());

        if (resource != null) {
            return resource.getValueMap().get("data/metaData/startComment", "");
        } else {
            return "";
        }
    }

    /**
     * Helper methods.
     */

    private int parseParamInt(String paramsStr, String paramName, int defaultValue) {
        String[] params = StringUtils.split(StringUtils.trim(paramsStr), System.lineSeparator());

        for (String param : params) {
            String[] tmp = StringUtils.split(param, "=");
            if (tmp.length == 2) {
                String key = StringUtils.trim(tmp[0]);
                String val = StringUtils.trim(tmp[1]);

                if (StringUtils.equalsIgnoreCase(paramName, key)) {
                    try {
                        int limit = Integer.parseInt(val);
                        log.debug("Parsed parameter for [ {} => {} ]", key, limit);
                        return limit;
                    } catch (Exception ex) {
                        log.error("Could not parse parameter for [ {} => {} ]", key, val);
                    }
                }
            }
        }

        return defaultValue;
    }

    private String parseParamString(String paramsStr, String paramName, String defaultValue) {
        String[] params = StringUtils.split(StringUtils.trim(paramsStr), System.lineSeparator());

        for (String param : params) {
            String[] tmp = StringUtils.split(param, "=");
            if (tmp.length == 1) {
                String key = StringUtils.stripToEmpty(tmp[0]);

                if (StringUtils.equalsIgnoreCase(paramName, key)) {
                    log.debug("Parsed parameter for [ {} => {} ]", key, "");
                    return "";
                }
            } else if (tmp.length == 2) {
                String key = StringUtils.stripToEmpty(tmp[0]);
                String val = StringUtils.stripToEmpty(tmp[1]);

                if (StringUtils.equalsIgnoreCase(paramName, key)) {
                    log.debug("Parsed parameter for [ {} => {} ]", key, val);
                    return val;
                }
            }
        }

        return defaultValue;
    }


}