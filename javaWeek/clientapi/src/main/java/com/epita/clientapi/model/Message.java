package com.epita.clientapi.model;

public class Message {
    public String className;
    public String jsonContent;
    public String senderUID;

    public Message(String className, String jsonContent) {
        this.className = className;
        this.jsonContent = jsonContent;
        this.senderUID = "";
    }

    public Message(String className, String jsonContent, String senderUID) {
        this.className = className;
        this.jsonContent = jsonContent;
        this.senderUID = senderUID;
    }

    public Message() { }
}
