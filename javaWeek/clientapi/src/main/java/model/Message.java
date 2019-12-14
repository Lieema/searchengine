package model;

public class Message {
    public String className;
    public String jsonContent;

    public Message(String className, String jsonContent) {
        this.className = className;
        this.jsonContent = jsonContent;
    }
}
