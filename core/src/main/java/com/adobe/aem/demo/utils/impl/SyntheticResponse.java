package com.adobe.aem.demo.utils.impl;

import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class SyntheticResponse extends SlingHttpServletResponseWrapper {
    // Default to 200
    private int status = 200;
    private StringWriter stringWriter = new StringWriter();
    private PrintWriter printWriter = new PrintWriter(stringWriter);

    public SyntheticResponse(final SlingHttpServletResponse wrappedResponse) {
        super(wrappedResponse);
    }

    public void sendError(int sc) throws IOException {
        this.status = sc;
        super.sendError(sc);
    }

    public void sendError(int sc, String msg) throws IOException {
        this.status = sc;
        super.sendError(sc, msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int sc) {
        this.status = sc;
        super.setStatus(sc);
    }

    public PrintWriter getWriter() {
        return printWriter;
    }

    public String getString() {
        return stringWriter.toString();
    }

    public void clearWriter() {
        printWriter.close();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }
}
