package com.util.io;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

final class ResourceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceUtils.class);

    private ResourceUtils() {
        throw new IllegalAccessError("Instantiation prohibited");
    }

    static ClassLoader getClassLoader() {
        ClassLoader loader;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (SecurityException ex) {
            // No thread context class loader -> use class loader of this class.
            LOGGER.info("No thread context class loader -> use class loader of this class", ex);
            loader = ResourceUtils.class.getClassLoader();
            if (loader == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    loader = ClassLoader.getSystemClassLoader();
                } catch (SecurityException e) {
                    // Cannot access system ClassLoader
                    LOGGER.error("Cannot access system ClassLoader", e);
                }
            }
        }

        return loader;
    }

    public boolean exists(URL url) {
        try {
            URLConnection con = url.openConnection();
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).setRequestMethod("HEAD");
            }
            HttpURLConnection httpCon = con instanceof HttpURLConnection ? (HttpURLConnection) con : null;
            if (httpCon != null) {
                int code = httpCon.getResponseCode();
                if (code == 200) {
                    return true;
                }

                if (code == 404) {
                    return false;
                }
            }

            if (con.getContentLengthLong() > 0L) {
                return true;
            } else if (httpCon != null) {
                httpCon.disconnect();
                return false;
            }

            return false;

        } catch (IOException var5) {
            return false;
        }
    }

    public boolean isReadable(URL url) {
        try {

            URLConnection con = url.openConnection();
            if (con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).setRequestMethod("HEAD");
            }
            if (con instanceof HttpURLConnection) {
                HttpURLConnection httpCon = (HttpURLConnection) con;
                int code = httpCon.getResponseCode();
                if (code != 200) {
                    httpCon.disconnect();
                    return false;
                }
            }

            long contentLength = con.getContentLengthLong();
            if (contentLength > 0L) {
                return true;
            } else if (contentLength == 0L) {
                return false;
            }

            return false;
        } catch (IOException var5) {
            return false;
        }
    }
}
