package com.adobe.aem.demo.utils.impl;

import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.taskmanagement.TaskManagerException;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/components/instructions",
                "sling.servlet.selectors=redirect",
                "sling.servlet.extensions=html",
        }
)
public class InstructionsRedirectServlet extends SlingSafeMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(InstructionsRedirectServlet.class);

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        final String taskId = StringUtils.stripToEmpty(request.getParameter("item"));

        final TaskManager taskManager = request.getResourceResolver().adaptTo(TaskManager.class);
        try {
            final Task task = taskManager.getTask(taskId);
            response.sendRedirect(task.getContentPath());
            return;
        } catch (TaskManagerException e) {
            log.error("Could not obtain task for taskId [ {} ]", taskId);
        }

        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}