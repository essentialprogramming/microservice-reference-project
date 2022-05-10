package com.util.xml;

import javax.xml.bind.*;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for (un-)marshalling XML objects
 */
public class JAXBUtility {

    private static final Map<Class<?>, JAXBContext> jaxbContextMap = new HashMap<>();

    /**
     * Marshall.
     *
     * @param <T>    The generic type
     * @param source The source
     * @return The string message
     */
    public static <T> String marshalObject(final T source) {
        final JAXBContext context = getJAXBContext(getPayloadClass(source));
        final Marshaller marshaller;
        try {
            marshaller = context.createMarshaller(); // creates marshaller
        } catch (final JAXBException e) {
            throw new RuntimeException("Error creating JAXB marshaller.", e);
        }

        final String payloadXml;
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            marshaller.marshal(source, stream); // marshall
            payloadXml = stream.toString(StandardCharsets.UTF_8.name());
        } catch (final Throwable e) {
            throw new RuntimeException("Error while marshalling.", e);

        }
        return payloadXml;
    }

    /**
     * Unmarshal.
     *
     * @param <T>        The generic type
     * @param xmlContent The input
     * @param type       The class type
     * @return The object of type
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(final String xmlContent, final Class<T> type) {
        final JAXBContext context = getJAXBContext(type);
        final Unmarshaller unmarshaller;
        try {
            unmarshaller = context.createUnmarshaller(); // creates unmarshaller
        } catch (final JAXBException e) {
            throw new RuntimeException("Error creating JAXB unmarshaller.", e);
        }

        final T targetObj;
        final T object;
        final StringReader stringReader = new StringReader(xmlContent);
        try {
            object = (T) unmarshaller.unmarshal(stringReader); // unmarshall
        } catch (final JAXBException e) {
            throw new RuntimeException("Error while unmarshalling.", e);
        }
        if (object instanceof JAXBElement) {
            targetObj = ((JAXBElement<T>) object).getValue();
        } else {
            targetObj = type.cast(object);
        }
        return targetObj;
    }

    /**
     * Gets the jaxb context.
     *
     * @param type the class type
     * @return the context
     */
    private static JAXBContext getJAXBContext(final Class<?> type) {

        JAXBContext context = jaxbContextMap.get(type); // get context
        if (context == null) {
            synchronized (JAXBUtility.class) {
                context = jaxbContextMap.get(type);
                if (context == null) {
                    try {
                        context = JAXBContext.newInstance(type);
                        jaxbContextMap.put(type, context);
                    } catch (final JAXBException e) {
                        throw new RuntimeException("Error creating the JAXB context.", e);
                    }
                }
            }
        }

        return context;
    }

    @SuppressWarnings("rawtypes")
    private static Class<?> getPayloadClass(Object payload) {
        if (payload instanceof JAXBElement) {
            return ((JAXBElement) payload).getDeclaredType();
        }

        return payload.getClass();
    }

}