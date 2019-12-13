package helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Message;

import java.io.IOException;

public class ConvertJsonStringObject {
    public String convertToJsonString(Message message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Message convertToMessage(String mess) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(mess, Message.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
