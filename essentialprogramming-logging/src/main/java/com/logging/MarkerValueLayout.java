package com.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import static org.apache.logging.log4j.core.pattern.PatternConverter.CATEGORY;

@Plugin(name = "MarkerValueLayout", category = CATEGORY)
@ConverterKeys({"markerValue"})
public class MarkerValueLayout  extends LogEventPatternConverter {

    private final Log4jMarkerFactory markerFactory = new Log4jMarkerFactory();

    private MarkerValueLayout(final String[] options)
    {
        super("a", "b");
    }

    public static MarkerValueLayout newInstance(final String[] options) {
        return new MarkerValueLayout(options);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        if (event.getMarker() != null) {
            final Log4jMarker marker = markerFactory.getMarker(event.getMarker().getName());
            toAppendTo.append(marker.getValue());
        } else {
            toAppendTo.append("N/A");
        }
    }
}
