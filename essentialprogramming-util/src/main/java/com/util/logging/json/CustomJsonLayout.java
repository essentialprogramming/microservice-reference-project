package com.util.logging.json;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * log4j2-custom-layout
 */
@Plugin(name = "CustomJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE)
public class CustomJsonLayout extends AbstractStringLayout {

    private static final String REPLACEMENT = " ";
    /**
     * Avoid synchronization.
     * </p>
     * {@link DateFormat} implementation is not thread safe, so give one {@link SimpleDateFormat} instance for each thread.
     *
     * @see DateFormat
     * @see SimpleDateFormat
     */
    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SS")); //NOSONAR


    public CustomJsonLayout(Configuration config, Charset charset) {
        super(config, charset, null, null);

    }

    @PluginFactory
    public static CustomJsonLayout createLayout(@PluginConfiguration final Configuration config,
                                                @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset) {
        return new CustomJsonLayout(config, charset);
    }

    @SneakyThrows
    @Override
    public String toSerializable(LogEvent event) {

        final ObjectMapper objectMapper = new ObjectMapper();
        final ObjectNode rootNode = objectMapper.createObjectNode();
        Message message = event.getMessage();

        rootNode.put("date", DATE_FORMAT_THREAD_LOCAL.get().format(Date.from(Instant.now())));
        rootNode.put("level", event.getLevel().name());
        rootNode.put("thread", event.getThreadName());
        rootNode.put("threadId", event.getThreadId());
        rootNode.put("loggerName", event.getLoggerName());

        if (message instanceof ParameterizedMessage) {
            ParameterizedMessage parameterizedMessage = (ParameterizedMessage) message;
            Map<String, String> messageParameters = MessageParametersExtractor.extractParameters(parameterizedMessage);
            for (Map.Entry<String, String> entry : messageParameters.entrySet())
                rootNode.put(entry.getKey(), entry.getValue());
        }


        rootNode.put("message", sanitize(message.getFormattedMessage()));


        // Exceptions
        if (event.getThrown() != null) {
            final Throwable throwable = event.getThrown();

            final String exceptionsClass = throwable.getClass().getCanonicalName();
            if (exceptionsClass != null) {
                rootNode.put("exception", exceptionsClass);
            }

            final String exceptionsMessage = throwable.getMessage();
            if (exceptionsMessage != null) {
                rootNode.put("cause", exceptionsMessage);
            }

            //TODO Format stacktrace
            final StackTraceElement[] stackTrace = throwable.getStackTrace();
            ArrayNode stacktraceArray = objectMapper.createArrayNode();

            if (stackTrace != null) {

                for (StackTraceElement element : stackTrace) {
                    String exception = "";

                    exception = exception
                            + "Class: " + element.getClassName()
                            + " -> Method: " + element.getMethodName()
                            + " -> Line: " + element.getLineNumber();
                    stacktraceArray.add(exception);
                }
                rootNode.set("stacktrace", stacktraceArray);

            }
        }

        return objectMapper.writeValueAsString(rootNode).concat("\r\n");
    }

    /**
     * Sanitizes a string to prevent log-forging attacks.
     * Could be private, but then we couldn't unit test it.
     *
     * @param message The string to log.
     * @return The sanitized string
     */
    public String sanitize(String message) {
        return message.replace("\n", REPLACEMENT).replace("\r", REPLACEMENT);
    }

}
