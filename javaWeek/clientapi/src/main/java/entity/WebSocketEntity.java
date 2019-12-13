package entity;

import helper.ConvertJsonStringObject;
import model.Message;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

public class WebSocketEntity extends WebSocketClient {
    private Queue<String> messageReceivedQueue;
    private Message message;

    public WebSocketEntity(URI serverUri) {
        super(serverUri);
    }

    public WebSocketEntity(URI serverUri, Draft draft) {
        super(serverUri, draft);
        messageReceivedQueue = new LinkedList<String>();
    }

    public WebSocketEntity(URI serverURI, String senderType, String messageType) {
        super(serverURI);
        messageReceivedQueue = new LinkedList<String>();
        this.message = new Message();
        this.message.senderType = senderType;
        this.message.messageType = messageType;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        ConvertJsonStringObject converter = new ConvertJsonStringObject();
        send(converter.convertToJsonString(this.message));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        send("closed");
    }

    @Override
    public void onMessage(String message) {
        messageReceivedQueue.add(message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        String v = StandardCharsets.UTF_8.decode(message).toString();
        messageReceivedQueue.add(v);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public Queue<String> getMessageQueue() {
        return messageReceivedQueue;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
