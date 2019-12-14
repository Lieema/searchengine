package helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

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
