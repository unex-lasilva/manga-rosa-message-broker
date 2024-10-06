package br.com.mangarosa.messages;

import java.util.UUID;

public class Message {
    private String id;
    private String content;

    public Message(String content) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
