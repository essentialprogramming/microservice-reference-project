package com.util.web;

import com.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class URLHelper {

    private static final Logger LOG = LoggerFactory.getLogger(URLHelper.class);
    private static final Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(png|jpeg|jpg|txt|doc|csv|pdf))$)");

    public static List<String> getPathParametersFromUrl(String input) {
        if (StringUtils.isEmpty(input)) return Collections.emptyList();
        final URI url;
        final String[] path;
        try {
            url = new URI(input);
            if (!url.getPath().contains("/"))
                return Collections.emptyList();
            if (StringUtils.isEmpty(url.getPath().substring(url.getPath().indexOf('/') + 1)))
                return Collections.emptyList();
            path = url.getPath().substring(url.getPath().indexOf('/') + 1).split("/");
        } catch (URISyntaxException e) {
            LOG.error("Error parsing url :" + input, e);
            return Collections.emptyList();
        }

        return Arrays.asList(path);
    }

    public static String getFileName(List<String> parameters) {
        return parameters.stream().filter(x -> pattern.matcher(x).matches()).findFirst().orElse("");
    }

}
