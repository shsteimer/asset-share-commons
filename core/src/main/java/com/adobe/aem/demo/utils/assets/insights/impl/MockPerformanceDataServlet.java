package com.adobe.aem.demo.utils.assets.insights.impl;

import com.day.cq.dam.api.Asset;
import org.apache.commons.lang.time.DateUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component(
        service = {Servlet.class},
        property = {
                "service.ranking:Integer=" + Integer.MAX_VALUE,
                "sling.servlet.resourceTypes=sling/servlet/default",
                "sling.servlet.methods=GET",
                "sling.servlet.extensions=json",
                "sling.servlet.selectors=performanceData"
        }
)
@Designate(ocd = MockPerformanceDataServlet.Cfg.class)
@SuppressWarnings("squid:S2384")
public class MockPerformanceDataServlet extends SlingSafeMethodsServlet {
    private static final Logger log = LoggerFactory.getLogger(MockPerformanceDataServlet.class);
    // static strings for request parsing
    private static final String FROM_DATE_REQ_PARAM = "from-date";
    private static final String TO_DATE_REQ_PARAM = "to-date";
    private static final String GRANULARITY_REQ_PARAM = "granularity";
    private static final String DATE_FORMAT_PATERN = "yyyy-MM-dd";
    private transient Cfg cfg;
    @Reference
    private ServiceComponentRuntime scr;
    private BundleContext bundleContext;

    protected void doGet(SlingHttpServletRequest request,
                         SlingHttpServletResponse response) throws ServletException,
            IOException {

        log.info("Invoking Mock Performance Data Generator [ {} ]", request.getResource().getPath());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATERN);


        Resource resource = request.getResource();
        response.setContentType("application/json");

        try {
            Asset asset = resource.adaptTo(Asset.class);

            if (asset == null) {
                throw new RepositoryException("Requested path is not an Asset");
            }

            String metric = request.getParameter("metric");
            String granularity = request.getParameter(GRANULARITY_REQ_PARAM);
            Date fromDate = sdf.parse(request.getParameter(FROM_DATE_REQ_PARAM));
            Date toDate = sdf.parse(request.getParameter(TO_DATE_REQ_PARAM));

            int offset = 0;
            int seed = asset.getPath().hashCode();

            if ("impressions".equals(metric)) {
                offset = randomInRange(new SecureRandom(BigInteger.valueOf(seed).toByteArray()), cfg.minBaseline(), cfg.maxBaseline());
                int min = new Double(cfg.maxBaseline() * .1d).intValue();
                if (offset < min) {
                    offset = min;
                }
            }

            log.debug("Offset [ {} ]", offset);

            JSONObject json = null;
            if ("day".equals(granularity)) {
                json = this.generateDays(fromDate, toDate, seed, offset);
            } else if ("week".equals(granularity)) {
                json = this.generateWeeks(fromDate, toDate, seed, offset);
            } else if ("month".equals(granularity)) {
                json = this.generateMonths(fromDate, toDate, seed, offset);
            }

            if (json != null) {
                response.getWriter().write(json.toString());
            } else {
                sendErrorMessage(response, "Unable to generate mock Insights data");
            }

        } catch (ParseException e) {
            sendErrorMessage(response, e.getMessage());
        } catch (RepositoryException e) {
            sendErrorMessage(response, e.getMessage());
        } catch (JSONException e) {
            sendErrorMessage(response, e.getMessage());
        }
    }

    //{"period":["2016-2-25","2016-2-26","2016-2-27","2016-2-28","2016-2-29","2016-3-1"],"metric":{"data":[3,13,10,15,14,12]}}
    private JSONObject generateDays(Date fromDate, Date toDate, int seed, int offset) throws JSONException {
        JSONObject json = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATERN);

        List<String> dates = new ArrayList<String>();
        List<String> metrics = new ArrayList<String>();
        Random rand = new SecureRandom(BigInteger.valueOf(seed).toByteArray());

        for (Date fromIter = fromDate; !fromIter.after(toDate); fromIter = DateUtils.addDays(fromIter, 1)) {
            dates.add(sdf.format(fromIter));

            int plus = randomInRange(rand, 0, offset);
            int base = randomInRange(rand, cfg.minBaseline(), cfg.maxBaseline());

            log.debug("Base [ {} ] -- Plus [ {} ]", base, plus);

            metrics.add(String.valueOf(base + plus));
        }

        json.put("period", dates);
        JSONObject data = new JSONObject();
        data.put("data", metrics);
        json.put("metric", data);

        return json;
    }

    private JSONObject generateWeeks(Date fromDate, Date toDate, int seed, int offset) throws JSONException {
        JSONObject json = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATERN);

        List<String> dates = new ArrayList<String>();
        List<String> metrics = new ArrayList<String>();
        Random rand = new SecureRandom(BigInteger.valueOf(seed).toByteArray());


        for (Date fromIter = fromDate; !fromIter.after(toDate); fromIter = DateUtils.addWeeks(fromIter, 1)) {
            dates.add(sdf.format(fromIter));

            int plus = randomInRange(rand, 0, offset);
            int base = randomInRange(rand, cfg.minBaseline(), cfg.maxBaseline());

            log.debug("Base [ {} ] -- Plus [ {} ]", base, plus);

            metrics.add(String.valueOf(7 * (base + plus)));
        }

        json.put("period", dates);
        JSONObject data = new JSONObject();
        data.put("data", metrics);
        json.put("metric", data);

        return json;
    }

    private JSONObject generateMonths(Date fromDate, Date toDate, int seed, int offset) throws JSONException {
        JSONObject json = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATERN);

        List<String> dates = new ArrayList<String>();
        List<String> metrics = new ArrayList<String>();
        Random rand = new SecureRandom(BigInteger.valueOf(seed).toByteArray());

        for (Date fromIter = fromDate; !fromIter.after(toDate); fromIter = DateUtils.addMonths(fromIter, 1)) {
            dates.add(sdf.format(fromIter));

            int plus = randomInRange(rand, 0, offset);
            int base = randomInRange(rand, cfg.minBaseline(), cfg.maxBaseline());

            log.debug("Base [ {} ] -- Plus [ {} ]", base, plus);

            metrics.add(String.valueOf(30 * (base + plus)));
        }

        json.put("period", dates);
        JSONObject data = new JSONObject();
        data.put("data", metrics);
        json.put("metric", data);

        return json;
    }

    private int randomInRange(Random rand, int min, int max) {
        return min + rand.nextInt((max - min) + 1);
    }

    private void sendErrorMessage(SlingHttpServletResponse response, String msg)
            throws IOException {
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();
        JSONObject errResponse = new JSONObject();
        try {
            errResponse.put("msg", msg);
            output.println(errResponse);
        } catch (JSONException e) {
            output.println("{'msg':msg}");
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Activate
    protected void activate(Cfg cfg) {
        this.cfg = cfg;

    }


    @ObjectClassDefinition(name = "AEM Demo Utils - Asset Insights - Mock Performance Data")
    public @interface Cfg {
        @AttributeDefinition(
                name = "Min Clicks",
                description = "Sets the minimum bound for Clicks."
        )
        int minBaseline() default 0;

        @AttributeDefinition(
                name = "Max Clicks",
                description = "Set the maximum bound for Clicks"
        )
        int maxBaseline() default 40;
    }
}
