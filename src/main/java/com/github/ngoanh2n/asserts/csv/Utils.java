package com.github.ngoanh2n.asserts.csv;

import java.io.File;
import java.net.URL;

/**
 * @author ngoanh2n@gmail.com (Ho Huu Ngoan)
 */

class Utils {

    static File getFileFromResources(String relative) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(relative);
        if (resource == null) throw new IllegalArgumentException("File not found!");
        else return new File(resource.getFile());
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null
     */
    static <T> T checkNotNull(T reference, String format, Object... args) {
        if (reference == null) throw new NullPointerException(String.format(format, args));
        return reference;
    }
}
