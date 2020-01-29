package com.adobe.aem.demo.utils.impl;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class SyntheticRequest extends SlingHttpServletRequestWrapper {
    private final String method;
    private final Map<String, String[]> requestParams;

    public SyntheticRequest(final SlingHttpServletRequest wrappedRequest,
                            final String method,
                            final Map<String, String[]> requestParams) {
        super(wrappedRequest);
        this.method = method;
        this.requestParams = requestParams;
    }

    @Override
    public String getMethod() {
        if (StringUtils.isBlank(this.method)) {
            return super.getMethod();
        } else {
            return this.method;
        }
    }

    public String getParameter(String name) {
        if (this.requestParams == null ||
                this.requestParams.get(name) == null ||
                this.requestParams.get(name).length == 0) {
            return super.getParameter(name);
        } else {
            return this.requestParams.get(name)[0];
        }
    }

    public Enumeration getParameterNames() {
        if (this.requestParams == null) {
            return super.getParameterNames();
        } else {
            return new IteratorEnumeration(this.requestParams.keySet().iterator());
        }
    }

    public String[] getParameterValues(String name) {
        if (this.requestParams == null) {
            return super.getParameterValues(name);
        } else {
            return this.requestParams.get(name);
        }
    }

    public Map getParameterMap() {
        return this.requestParams;
    }

    public RequestParameter getRequestParameter(String name) {
        return new SyntheticRequestParameter(name, getParameter(name));
    }

    public List<RequestParameter> getRequestParameterList() {
        List<RequestParameter> requestParameters = new ArrayList<>();

        Enumeration names = getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();

            Stream.of(getParameterValues(name)).forEach(v -> {
                requestParameters.add(new SyntheticRequestParameter(name, v));
            });
        }

        return requestParameters;
    }

    public RequestParameter[] getRequestParameters(String name) {
        final List<RequestParameter> requestParameters = new ArrayList<>();

        if (getParameterValues(name) != null) {
            Stream.of(getParameterValues(name)).forEach(v -> {
                requestParameters.add(new SyntheticRequestParameter(name, v));
            });

            return requestParameters.toArray(new RequestParameter[0]);
        } else {
            return new RequestParameter[0];
        }
    }

    public RequestParameterMap getRequestParameterMap() {
        return new SyntheticRequestParameterMap(this);
    }


    private class SyntheticRequestParameter implements RequestParameter {
        private final String name;
        private final String value;

        public SyntheticRequestParameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isFormField() {
            return false;
        }

        @CheckForNull
        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public long getSize() {
            return name.getBytes().length;
        }

        @Override
        public byte[] get() {
            return name.getBytes();
        }

        @CheckForNull
        @Override
        public InputStream getInputStream() throws IOException {
            return IOUtils.toInputStream(name);
        }

        @CheckForNull
        @Override
        public String getFileName() {
            return null;
        }

        @Nonnull
        @Override
        public String getString() {
            return value;
        }

        @Nonnull
        @Override
        public String getString(@Nonnull final String s) throws UnsupportedEncodingException {
            return value;
        }
    }

    private class SyntheticRequestParameterMap implements RequestParameterMap {

        private final SlingHttpServletRequest ctx;

        public SyntheticRequestParameterMap(SlingHttpServletRequest request) {
            this.ctx = request;
        }

        @CheckForNull
        public RequestParameter[] get(final String name) {
            return ctx.getRequestParameters(name);
        }

        @CheckForNull
        @Override
        public RequestParameter[] getValues(@Nonnull final String name) {
            return ctx.getRequestParameters(name);
        }

        @CheckForNull
        @Override
        public RequestParameter getValue(final String name) {
            return ctx.getRequestParameter(name);
        }

        @Override
        public int size() {
            return ctx.getParameterMap().size();
        }

        @Override
        public boolean isEmpty() {
            return ctx.getParameterMap().isEmpty();
        }

        @Override
        public boolean containsKey(final Object key) {
            return ctx.getParameterMap().containsKey(key);
        }

        @Override
        public boolean containsValue(final Object value) {
            AtomicBoolean contains = new AtomicBoolean(false);
            ctx.getRequestParameterList().stream().forEach(r -> {
                if (StringUtils.equals(r.getString(), (String) value)) {
                    contains.set(true);
                }
            });

            return contains.get();
        }

        @Override
        public RequestParameter[] get(final Object key) {
            return ctx.getRequestParameters((String) key);
        }

        @Override
        public RequestParameter[] put(final String key, final RequestParameter[] value) {
            throw new UnsupportedOperationException("Cannot modify synthetic request parameters");
        }

        @Override
        public RequestParameter[] remove(final Object key) {
            throw new UnsupportedOperationException("Cannot modify synthetic request parameters");
        }

        @Override
        public void putAll(final Map<? extends String, ? extends RequestParameter[]> m) {
            throw new UnsupportedOperationException("Cannot modify synthetic request parameters");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Cannot modify synthetic request parameters");
        }

        @Override
        public Set<String> keySet() {
            return ctx.getParameterMap().keySet();
        }

        @Override
        public Collection<RequestParameter[]> values() {
            throw new UnsupportedOperationException("Cannot do this with synthetic request parameters");
        }

        @Override
        public Set<Entry<String, RequestParameter[]>> entrySet() {
            throw new UnsupportedOperationException("Cannot do this with synthetic request parameters");
        }
    }

}
