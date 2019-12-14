package com.epita.clientapi.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.epita.clientapi.model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConvertJsonStringObject {
    public static Logger logger = LogManager.getLogger(ConvertJsonStringObject.class);

    public static String convertToJsonString(Message message) {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (Exception e) {
            logger.error("[CONVERTER] Message serialization failed");
        }
        return null;
    }

    public static Message convertToMessage(String mess) {

        try {
            return new ObjectMapper().readValue(mess, Message.class);
        } catch (Exception e) {
            logger.error("[CONVERTER] Message deserialization failed");
        }
        return null;
    }
}
