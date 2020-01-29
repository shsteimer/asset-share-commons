package com.adobe.aem.demo.utils.impl;

import org.apache.jackrabbit.vault.fs.io.ImportOptions;
import org.apache.jackrabbit.vault.packaging.JcrPackage;
import org.apache.jackrabbit.vault.packaging.JcrPackageManager;
import org.apache.jackrabbit.vault.packaging.PackageException;
import org.apache.jackrabbit.vault.packaging.Packaging;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.io.InputStream;


@Component(
        service = PackageHelper.class
)
public class PackageHelper {

    @Reference
    private Packaging packaging;

    public void installPackage(ResourceResolver resourceResolver, String packagePath) throws RepositoryException, IOException, PackageException {
        JcrPackageManager jcrPackageManager = packaging.getPackageManager(resourceResolver.adaptTo(Session.class));
        InputStream is = resourceResolver.getResource(packagePath).adaptTo(InputStream.class);
        JcrPackage jcrPackage = jcrPackageManager.upload(is, true, false);
        jcrPackage.install(new ImportOptions());
    }
}
