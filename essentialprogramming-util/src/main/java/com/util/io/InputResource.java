package com.util.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public interface InputResource {

    /**
     * Resolve the given resource location to a {@code java.net.URL}.
     *
     * @return a corresponding URL object
     * @throws FileNotFoundException if the resource cannot be resolved to a URL
     */
    static URL getURL(String resourceLocation) throws FileNotFoundException {
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Resource location must not be null");
        }
        if (resourceLocation.startsWith(InputResourcePrefix.CLASSPATH_URL_PREFIX.getPrefix())) {
            String path = resourceLocation.substring(InputResourcePrefix.CLASSPATH_URL_PREFIX.getPrefix().length());
            ClassLoader loader = ResourceUtils.getClassLoader();
            URL url = (loader != null ? loader.getResource(path) : ClassLoader.getSystemResource(path));
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description + " cannot be resolved to URL because it does not exist");
            }
            return url;
        }
        try {
            // try URL
            return new URL(resourceLocation);
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex2) {
                throw new FileNotFoundException(
                        "Resource location [" + resourceLocation + "] is neither a URL not a well-formed file path");
            }
        }
    }

    /**
     * Gets the input stream to the resource. The obtained input stream
     * must be closed once reading has finished.
     *
     * @return Input stream to the resource, never null.
     * @throws IOException in case the resource cannot be found.
     */
    InputStream getInputStream() throws IOException;
}
