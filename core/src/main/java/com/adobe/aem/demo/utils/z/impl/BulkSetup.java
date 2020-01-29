package com.adobe.aem.demo.utils.z.impl;

import com.adobe.aem.demo.utils.Executable;
import com.adobe.aem.demo.utils.impl.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component(service = {Servlet.class},
           property = {
                   "sling.servlet.methods=GET",
                   "sling.servlet.resourceTypes=demo-utils/instructions/bulk-setup",
                   "sling.servlet.selectors=install",
                   "sling.servlet.extensions=html"
           }
)
public class BulkSetup extends SlingAllMethodsServlet {
    private static Logger log = LoggerFactory.getLogger(BulkSetup.class);

    @Reference(
            cardinality = ReferenceCardinality.MULTIPLE,
            policyOption = ReferencePolicyOption.GREEDY
    )
    private List<Executable> executables;

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        final ValueMap params = new ValueMapDecorator(request.getRequestParameterMap().entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> (Object) entry.getValue())));

        try {
            int count = 0;
            for (final Executable executable : executables) {
                if (executable != null && executable.accepts(request.getResourceResolver(), params)) {
                    executable.execute(request, response);
                    log.info("AEM Demo Utils bulk setup [ {} ]", executable.getName());
                    count++;
                }
            }
            if (count > 0) {
                if(request.getResourceResolver().hasChanges()) {
                    request.getResourceResolver().commit();
                }
                log.info("{} demo resources were set up.", count);
                response.sendRedirect(Constants.SUCCESS_URL);
            } else {
                log.error("Zero demo resources were set up.");
                response.sendRedirect(Constants.FAILURE_URL);
            }
        } catch (Exception e) {
            log.error("An error occurred during the bulk set up.", e);
            response.sendRedirect(Constants.FAILURE_URL);
        }
    }
}



