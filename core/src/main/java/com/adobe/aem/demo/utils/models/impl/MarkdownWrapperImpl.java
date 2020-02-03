package com.adobe.aem.demo.utils.models.impl;

import com.adobe.aem.demo.utils.models.MarkdownWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.featureflags.Features;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Model(adaptables = {SlingHttpServletRequest.class}, adapters = {MarkdownWrapper.class}, resourceType = {
        MarkdownWrapperImpl.RESOURCE_TYPE}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class MarkdownWrapperImpl implements MarkdownWrapper {
    private static final Logger log = LoggerFactory.getLogger(MarkdownWrapperImpl.class);

    protected static final String RESOURCE_TYPE = "demo-utils/components/base";

    //private static final Pattern INCLUDE = Pattern.compile("<!--\\s?INCLUDE:\\s?([\\.a-zA-Z0-9_-]+)\\s?-->", Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);

    private static final String CLOUD_SERVICE_TAG = "<!-- CLOUD-SERVICE_INSTRUCTIONS -->";
    private static final String QUICKSTART_TAG = "<!-- QUICKSTART_INSTRUCTIONS -->";

    private Resource resource;

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private Features features;

    @PostConstruct
    private void init() {
        resource = request.getResource();
    }

    public String getCloudServiceInstructions() {
        if (isCloudService()) {
            return getInstructions("cloud-service.md");
        }
        return "";
    }

    public String getQuickstartInstructions() {
        if (!isCloudService()) {
            return getInstructions("quickstart.md");
        }
        return "";
    }

    public String getInstructions() {
        String instructions = getInstructions("instructions.md");

        instructions = StringUtils.replace(instructions, CLOUD_SERVICE_TAG, getCloudServiceInstructions());
        instructions = StringUtils.replace(instructions, QUICKSTART_TAG, getQuickstartInstructions());

        return instructions;
    }

    protected String getInstructions(final String filename) {
        InputStream is = getMarkdownFile(filename);
        try {
            return processMarkdownFile(is);
        } catch (IOException e) {
            log.error("Could not process markdown file", e);
        }
        return null;
    }

    protected String processMarkdownFile(final InputStream inputStream) throws IOException {
        if (inputStream != null) {

            try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

                final ArrayList<Extension> extensions = new ArrayList<>();
                extensions.add(TablesExtension.create());
                final Parser parser = Parser.builder().extensions(extensions).build();
                final HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();

                final Node document = parser.parseReader(reader);
                final String htmlString = renderer.render(document);

                return htmlString;

            } catch (IOException e) {
                log.error("Issue parsing markdown file", e);
            } finally {
                inputStream.close();
            }
        }

        // No instructions available
        return null;
    }

    private InputStream getMarkdownFile(final String filename) {
        log.debug("Resource Path " + resource.getPath() + "/" + filename + "/jcr:content");

        final Resource dataResource = resource.getChild(filename + "/jcr:content");

        if (dataResource != null) {
            return dataResource.adaptTo(InputStream.class);
        }

        return null;
    }

    private boolean isCloudService() {
        if (StringUtils.equalsIgnoreCase(request.getParameter("m"), "cs")) {
            return true;
        } else  if (StringUtils.equalsIgnoreCase(request.getParameter("m"), "q")) {
            return false;
        } else {
            return features.isEnabled("com.adobe.dam.asset.nui.feature.flag");
        }
    }
}