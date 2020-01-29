package com.adobe.aem.demo.utils.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryUtil {

    public static List<Resource> findResources(ResourceResolver resourceResolver, Map<String, String> params) throws RepositoryException {
        List<Resource> resources = new ArrayList<>();
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        Query query = queryBuilder.createQuery(PredicateGroup.create(params), resourceResolver.adaptTo(Session.class));

        ResourceResolver leakingResourceResolver = null;
        try {
            SearchResult result = query.getResult();
            for (final Hit hit : result.getHits()) {
                Resource resource = hit.getResource();
                if (leakingResourceResolver == null) {
                    leakingResourceResolver = resource.getResourceResolver();
                }

                Resource r = resourceResolver.getResource(resource.getPath());

                if (!org.apache.commons.lang3.StringUtils.startsWithAny(r.getPath(), Constants.EXCLUDE_DAM_PATH_PREFIXES)) {
                    resources.add(r);
                }
            }
        } finally {
            if (leakingResourceResolver != null) {
                leakingResourceResolver.close();
            }
        }

        return resources;
    }
}

