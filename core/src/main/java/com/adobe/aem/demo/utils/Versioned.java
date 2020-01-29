package com.adobe.aem.demo.utils;

import com.day.cq.commons.Version;

public interface Versioned {

    Version getVersion();

    String getName();
}
