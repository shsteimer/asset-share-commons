package com.adobe.aem.demo.utils.tasks.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.AbstractExecutable;
import com.adobe.aem.demo.utils.impl.CleanerUtil;
import com.adobe.aem.demo.utils.impl.Constants;
import com.adobe.aem.demo.utils.impl.QueryUtil;
import com.adobe.aem.demo.utils.tasks.InboxTasks;
import com.adobe.granite.taskmanagement.Task;
import com.adobe.granite.taskmanagement.TaskManager;
import com.adobe.granite.workflow.WorkflowException;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.util.Text;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.servlet.Servlet;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component(
        service = {Executable.class, Servlet.class, InboxTasks.class},
        property = {
                "sling.servlet.methods=GET",
                "sling.servlet.resourceTypes=demo-utils/instructions/generate-tasks",
                "sling.servlet.selectors=remove",
                "sling.servlet.extensions=html"
        }
)
public class InboxTasksImpl extends AbstractExecutable implements InboxTasks {
    private static final Logger log = LoggerFactory.getLogger(InboxTasksImpl.class);

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

    private final long[] TASK_PRIORITY = new long[]{
            200,
            0,
            -200
    };

    private final String[] ASSET_MESSAGES = new String[]{
            "Translate %s for global distribution", // 1
            "Legal review of %s", // 2
            "Brand review of %s", // 3
            "Update %s to new brand standards", // 4
            "Complete metadata updates to %s", // 5
            "Apply new tagging taxonomy to %s", // 6
            "Send %s to the creative agency", // 7
            "Link share %s with external agency", // 8
            "Publish %s to Brand Portal" // 9
    };

    private final String[] ASSET_PROJECT_ASSIGNEES = new String[]{
            "role_editor", // 1
            "role_observer", // 2
            "role_observer", // 3
            "role_owner", // 4
            "role_editor", // 5
            "role_editor", // 6
            "role_owner", // 7
            "role_owner", // 8
            "role_owner" //9
    };

    private final String[] ASSET_ASSIGNEES = new String[]{
            "projects-users", // 1
            "workflow-users", // 2
            "workflow-users", // 3
            "dam-users", // 4
            "content-authors", // 5
            "tag-administrators", // 6
            "dam-users", // 7
            "dam-users", // 8
            "dam-users" //9
    };

    private final String[] PAGE_MESSAGES = new String[]{
            "Update copy %s", // 1
            "Legal review of %s", // 2
            "Review use of brand voice on %s", // 3
            "Determine cross-channel reuse potential of %s", // 4
            "Update %s to new brand standards", // 5
            "Update %s to use new logo", // 6
            "Update contact information/links", //7
            "Replace copy on %s with Content Fragment", //8
            "Replace experience on %s with Experience Fragment", //9
            "Un-publish expired campaign materials [ %s ]", // 10
            "Create a translation of %s"
    };

    private final String[] PAGE_PROJECT_ASSIGNEES = new String[]{
            "role_editor", // 1
            "role_observer", // 2
            "role_observer", // 3
            "role_owner", // 4
            "role_owner", // 5
            "role_owner", // 6
            "role_editor", // 7
            "role_editor", // 8
            "role_editor", // 9
            "role_editor", // 10
            "role_owner", // 11
    };

    private final String[] PAGE_ASSIGNEES = new String[]{
            "contributor", // 1
            "contributor", // 2
            "content-authors", // 3
            "content-authors", // 4
            "template-authors", // 5
            "template-authors", // 6
            "content-authors", // 7
            "dam-users", // 8
            "content-authors", //9
            "content-authors", // 10
            "projects-users" // 11
    };

    private final String[] NON_PAYLOAD_TASK_TITLES = new String[]{
            "Decommission legacy marketing campaign", // 1
            "Check-in with 3rd party agency on creative progress", // 2
            "AEM application deployment", // 3
            "Review Content architecture", // 4
            "Review tag taxonomy", // 5
            "Align marketing teams to support cross-channel content delivery", // 6
            "Create content for the new marketing campaign", // 7
            "Verify creative agency has committed to time line" // 8
    };

    private final String[] NON_PAYLOAD_ASSIGNEES = new String[]{
            "content-authors", // 1
            "projects-users", // 2
            "administrators", // 3
            "dam-users", // 4
            "tag-administrators", // 5
            "content-authors", // 6
            "content-authors", // 7
            "content-authors", // 8
    };

    @Override
    public String getName() {
        return "inbox-tasks";
    }

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            if ("remove".equals(request.getRequestPathInfo().getSelectorString())) {
                CleanerUtil.removeChildren(request.getResourceResolver(), "/var/taskmanagement/tasks");
            } else {
                execute(request, response);
            }
            response.sendRedirect(Constants.SUCCESS_URL);
        } catch (Exception e) {
            log.error("Could not clean up existing tasks", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }

    @Override
    public void execute(SlingHttpServletRequest request, SlingHttpServletResponse response) throws Exception {
        final ResourceResolver resourceResolver = request.getResourceResolver();

        ValueMap p = new ValueMapDecorator(new HashMap<>());
        p.put("path", "/content/dam");

        generateInboxTasks(resourceResolver, p);
    }

    @Override
    public void generateInboxTasks(final ResourceResolver resourceResolver, final ValueMap params) throws WorkflowException {
        try {
            final String path = params.get("path", String.class);

            int contentTaskLimit = params.get(PARAM_CONTENT_TASKS, MAX_CONTENT_TASKS);
            log.debug("Content Task limit [ {} ]", contentTaskLimit);

            int nonContentTaskLimit = params.get(PARAM_NON_CONTENT_TASKS, MAX_NON_CONTENT_TASKS);
            log.debug("Non-content Task limit [ {} ]", nonContentTaskLimit);

            int dayRangeStart = params.get(PARAM_DAY_RANGE_START, DEFAULT_DAY_RANGE_START);
            log.debug("Day Range Start [ {} ]", dayRangeStart);

            int dayRangeEnd = params.get(PARAM_DAY_RANGE_END, DEFAULT_DAY_RANGE_END);
            log.debug("Day Range End [ {} ]", dayRangeEnd);

            int maxTaskDuration = params.get(PARAM_MAX_TASK_DURATION, MAX_TASK_DURATION);
            log.debug("Max Task Duration [ {} ]", maxTaskDuration);

            String forceProjectPath = params.get(PARAM_FORCE_PROJECT_PATH, null);
            log.debug("Force project path [ {} ]", forceProjectPath);

            boolean autoApplyProjectAssignment = forceProjectPath == null;
            //boolean forceSkipProjectAssignment = "".equals(forceProjectPath);
            boolean forceApplyProjectAssignment = StringUtils.startsWith(forceProjectPath, "/");

            final List<Resource> taskPayloads = getTaskContent(resourceResolver, path, contentTaskLimit);
            final Map<String, Set<String>> coverage = getProjectCoverage(resourceResolver);

            int count = 0;
            for (Resource payload : taskPayloads) {
                if (count >= contentTaskLimit) {
                    break;
                }

                String projectPath = null;
                if (forceApplyProjectAssignment) {
                    projectPath = forceProjectPath;
                } else if (autoApplyProjectAssignment) {
                    // forceProjectPath param wasnt provided, so look up based on the content
                    projectPath = getProjectPath(resourceResolver, payload.getPath(), coverage);
                }

                TaskManager taskManager = getTaskManager(resourceResolver, projectPath);
                Task task = taskManager.getTaskManagerFactory().newTask(Task.DEFAULT_TASK_TYPE);
                if (StringUtils.isNotBlank(projectPath)) {
                    log.debug("Set project as [ {} ]", projectPath);
                    task.setProperty("projectPath", projectPath);
                }
                task.setProperty("taskCount", count++);

                taskManager.createTask(populateTask(task, resourceResolver, payload.getPath(), dayRangeStart, dayRangeEnd, maxTaskDuration));
                log.debug("[ {} ] Created Payload task for [ {} ]", count, task.getName());
            }

            // Non payload tasks
            for (int i = 0; i < nonContentTaskLimit; i++) {
                TaskManager taskManager = getTaskManager(resourceResolver, null);
                Task task = taskManager.getTaskManagerFactory().newTask(Task.DEFAULT_TASK_TYPE);
                task.setProperty("taskCount", count++);

                if (forceApplyProjectAssignment) {
                    log.debug("Set project as [ {} ]", forceProjectPath);
                    task.setProperty("projectPath", forceProjectPath);
                }

                taskManager.createTask(populateTask(task, resourceResolver, null, dayRangeStart, dayRangeEnd, maxTaskDuration));
                log.debug("[ {} ] Created non-payload task for [ {} ]", i + 1, task.getName());
            }

            log.info("Generated a total of [ {} ] random tasks.", count);
            if (resourceResolver.hasChanges()) {
                resourceResolver.commit();
            }
        } catch (Exception e) {
            log.error("Unable to complete processing the Workflow Process step", e);

            throw new WorkflowException("Unable to complete processing the Workflow Process step", e);
        }
    }


    private List<Resource> getTaskContent(ResourceResolver resourceResolver, String payloadPath, int limit) throws RepositoryException {
        String nodeType = null;

        if (StringUtils.equals(payloadPath, DamConstants.MOUNTPOINT_ASSETS) ||
                StringUtils.startsWith(payloadPath, DamConstants.MOUNTPOINT_ASSETS)) {
            nodeType = DamConstants.NT_DAM_ASSET;
        } else if (StringUtils.startsWith(payloadPath, "/content")) {
            nodeType = NameConstants.NT_PAGE;
        }


        Set<String> collected = new HashSet<String>();
        List<Resource> result = new ArrayList<Resource>();

        if (nodeType != null) {

            final Map<String, String> map = new HashMap<String, String>();

            map.put("type", nodeType);
            map.put("path", payloadPath);
            map.put("property", "jcr:content/dam:s7damType");
            map.put("property.operation", "not");
            map.put("orderby", "@jcr:content/jcr:lastModified");
            map.put("orderby.sort", "desc");
            map.put("p.offset", "0");
            map.put("p.limit", "-1");

            final AtomicInteger count = new AtomicInteger(0);
            QueryUtil.findResources(resourceResolver, map).stream().forEach(r -> {
                if (count.get() >= limit) {
                    return;
                }

                if (StringUtils.startsWithAny(r.getPath(), Constants.EXCLUDE_DAM_PATH_PREFIXES)) {
                    return;
                }

                if (!collected.contains(r.getPath())) {
                    collected.add(r.getPath());
                    result.add(r);
                    count.incrementAndGet();
                    log.debug("[ {} ] Collected [ {} ] for task generation", count, r.getPath());
                }

            });
        }

        return result;
    }

    private Task populateTask(Task task, ResourceResolver resourceResolver, String payload, int dayRangeStart, int dayRangeEnd, int maxTaskDuration) {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

        Random random = new SecureRandom();
        Calendar startDate = Calendar.getInstance();

        // Randomly select from the full range, -4 -- 8 == 12 days, pull/push by dayRangeStart

        int daysDuration = (-1 * dayRangeStart) + dayRangeEnd;
        if (daysDuration < 0) {
            log.warn("Day Duration was specified to be a negative value [ {} ] defaulting to [ {} ]", daysDuration, DEFAULT_DAY_RANGE_END + (-1 * DEFAULT_DAY_RANGE_START));
            daysDuration = DEFAULT_DAY_RANGE_END + (-1 * DEFAULT_DAY_RANGE_START);
        }

        startDate.add(Calendar.DATE, random.nextInt(daysDuration) + dayRangeStart);
        startDate.set(Calendar.HOUR_OF_DAY, random.nextInt(9) + 9);
        startDate.set(Calendar.MINUTE, random.nextInt(60));
        startDate.set(Calendar.SECOND, random.nextInt(60));

        Calendar dueDate = Calendar.getInstance();
        dueDate.setTime(startDate.getTime());

        dueDate.add(Calendar.DATE, new SecureRandom().nextInt(maxTaskDuration - 1) + 1);

        if (StringUtils.isNotBlank(payload)) {
            task.setContentPath(payload);
        }

        task.setProperty("priority", TASK_PRIORITY[random.nextInt(TASK_PRIORITY.length)]);

        task.setProperty("startTime", startDate.getTime());
        task.setProperty("progressBeginTime", startDate.getTime());

        task.setProperty("dueTime", dueDate.getTime());

        if (StringUtils.isEmpty(payload)) {
            // Skip to default catch
        } else if (DamUtil.resolveToAsset(resourceResolver.getResource(payload)) != null) {
            return populateAssetTask(task, resourceResolver);
        } else if (pageManager.getContainingPage(payload) != null) {
            return populatePageTask(task, resourceResolver);
        }

        return populateNonContentTask(task, resourceResolver);
    }

    private Task populateNonContentTask(Task task, ResourceResolver resourceResolver) {
        int taskCount = 0;
        try {
            taskCount = (int) task.getProperty("taskCount");
        } catch (Exception e) {
            log.error("Could not collect Message Count from the Task");
        }
        int index = taskCount % NON_PAYLOAD_TASK_TITLES.length;

        task.setName(NON_PAYLOAD_TASK_TITLES[index]);
        task.setDescription("Perform activities to " + StringUtils.lowerCase(task.getName()));
        task.setCurrentAssignee(getAssignee(resourceResolver, NON_PAYLOAD_ASSIGNEES[index], null));

        return task;
    }

    private Task populateAssetTask(Task task, ResourceResolver resourceResolver) {
        final Asset asset = DamUtil.resolveToAsset(resourceResolver.getResource(task.getContentPath()));
        final String title = StringUtils.defaultIfBlank(asset.getMetadataValue("dc:title"), asset.getName());

        int taskCount = 0;
        try {
            taskCount = (int) task.getProperty("taskCount");
        } catch (Exception e) {
            log.error("Could not collect Message Count from the Task");
        }
        int index = taskCount % ASSET_MESSAGES.length;

        task.setName(String.format(ASSET_MESSAGES[index], title));
        task.setDescription("Perform the tasks to " + StringUtils.lowerCase(task.getName()));

        if (task.getProperty("projectPath") != null) {
            task.setCurrentAssignee(getAssignee(resourceResolver, ASSET_PROJECT_ASSIGNEES[index], task.getProperty("projectPath").toString()));
        } else {
            task.setCurrentAssignee(getAssignee(resourceResolver, ASSET_ASSIGNEES[index], null));
        }

        return task;
    }

    private Task populatePageTask(Task task, ResourceResolver resourceResolver) {
        final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        final Page page = pageManager.getContainingPage(task.getContentPath());
        final String title = StringUtils.defaultIfBlank(page.getPageTitle(),
                StringUtils.defaultIfBlank(page.getTitle(),
                        StringUtils.defaultIfBlank(page.getNavigationTitle(), page.getName())));

        int taskCount = 0;
        try {
            taskCount = (int) task.getProperty("taskCount");
        } catch (Exception e) {
            log.error("Could not collect Message Count from the Task");
        }
        int index = taskCount % PAGE_MESSAGES.length;

        task.setName(String.format(PAGE_MESSAGES[index], title));
        task.setDescription("Perform the tasks to " + StringUtils.lowerCase(task.getName()));

        if (task.getProperty("projectPath") != null) {
            task.setCurrentAssignee(getAssignee(resourceResolver, PAGE_PROJECT_ASSIGNEES[index], task.getProperty("projectPath").toString()));
        } else {
            task.setCurrentAssignee(getAssignee(resourceResolver, PAGE_ASSIGNEES[index], null));
        }

        return task;
    }

    private Map<String, Set<String>> getProjectCoverage(ResourceResolver resourceResolver) {
        final Map<String, Set<String>> coverage = new HashMap<String, Set<String>>();

        final Iterator<Resource> resources = resourceResolver.findResources(
                "SELECT * FROM [nt:unstructured] AS p WHERE ISDESCENDANTNODE([/content/projects]) AND (p.[sling:resourceType] = 'cq/gui/components/projects/admin/card/linkcard' OR p.[sling:resourceType] = 'cq/gui/components/projects/admin/pod/assetpod')",
                Query.JCR_SQL2
        );

        while (resources.hasNext()) {
            try {
                Resource r = resources.next();

                String projectPath = Text.getAbsoluteParent(r.getPath(), 2);

                Set<String> paths = coverage.get(projectPath);
                if (paths == null) {
                    paths = new HashSet<String>();
                }

                if (r.isResourceType("cq/gui/components/projects/admin/card/linkcard")) {
                    if ("experiences".equals(r.getParent().getName())) {
                        paths.add(r.getValueMap().get("suffix", ""));
                    }
                } else {
                    paths.add(r.getValueMap().get("assetPath", ""));
                }

                Resource projectJcrContentResource = resourceResolver.getResource(projectPath + "/jcr:content");
                if (projectJcrContentResource != null) {
                    paths.add(projectJcrContentResource.getValueMap().get("damFolderPath", ""));
                    coverage.put(projectPath, paths);
                }
            } catch (Exception e) {
                log.warn("Could not successfully resolve a project for the search result. Skipping.");
            }
        }

        return coverage;
    }

    private String getProjectPath(ResourceResolver resourceResolver, String payload, Map<String, Set<String>> coverage) {
        if (new SecureRandom().nextBoolean() &&
                StringUtils.isNotBlank(payload) &&
                resourceResolver.getResource("/content/projects") != null) {

            for (Map.Entry<String, Set<String>> entry : coverage.entrySet()) {
                for (String path : entry.getValue()) {
                    if (StringUtils.startsWith(payload, path)) {
                        return entry.getKey();
                    }
                }
            }
        }

        return null;
    }

    private TaskManager getTaskManager(ResourceResolver resourceResolver, String projectPath) {
        if (!StringUtils.isEmpty(projectPath)) {
            Resource taskResource = resourceResolver.getResource(projectPath + "/jcr:content/tasks");
            if (taskResource != null) {
                return taskResource.adaptTo(TaskManager.class);
            }
        }
        return resourceResolver.adaptTo(TaskManager.class);
    }

    /**
     * Helper methods.
     */

    private String getAssignee(ResourceResolver resourceResolver, String rawUserId, String projectPath) {
        Resource projectResource = resourceResolver.getResource(projectPath);

        if (projectResource == null) {
            return rawUserId;
        } else {
            return projectResource.getValueMap().get(rawUserId, "administrators");
        }
    }
}