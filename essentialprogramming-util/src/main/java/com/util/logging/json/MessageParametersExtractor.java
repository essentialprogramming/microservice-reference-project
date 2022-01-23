package com.util.logging.json;

import com.util.text.StringUtils;
import org.apache.logging.log4j.message.ParameterizedMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageParametersExtractor {

    public static Map<String, String> extractParameters(ParameterizedMessage message) {
        Map<String, String> parameterMap = new HashMap<>();
        String messageFormat = message.getFormat();
        Object[] parameters = message.getParameters();
        String[] messageSplit = messageFormat.split("\\{}");
        for (int i = 0; i < messageSplit.length; i++) {
            String messagePart = messageSplit[i].trim();
            if (StringUtils.isNotEmpty(messagePart)) {
                String lastCharacter = messagePart.substring(messagePart.length() - 1);
                if (lastCharacter.equals("=") || lastCharacter.equals(":")) {
                    String[] messagePartSplit = messagePart.split(" +");
                    String lastPart = messagePartSplit[messagePartSplit.length - 1];
                    String key = lastPart.equals("=") || lastPart.equals(":") ?
                            messagePartSplit[messagePartSplit.length - 2] : lastPart.substring(0, lastPart.length() - 1);
                    parameterMap.put(key, parameters[i].toString());
                }
            }
        }
        return parameterMap;
    }

}