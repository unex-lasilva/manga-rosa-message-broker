package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;

import java.time.LocalDateTime;

public class Message {

    private Producer producer;
    private final LocalDateTime createdAt;
    private boolean isConsumed;
    private String message;
    private Consumer consumedBy;

    public Message(Producer producer, String message){
        setProducer(producer);
        setMessage(message);
        this.createdAt = LocalDateTime.now();
    }

    public Producer getProducer() {
        return producer;
    }

    private void setProducer(Producer producer) {
        if(producer == null)
            throw new IllegalArgumentException("The message's producer can't be null");
        this.producer = producer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isConsumed() {
        return isConsumed;
    }

    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        if(message == null || message.isBlank() || message.isEmpty())
            throw new IllegalArgumentException("The message content can't be null or empty or blank");
        this.message = message;

    }

    public Consumer getConsumedBy() {
        return consumedBy;
    }

    public void setConsumedBy(Consumer consumedBy) {
        if(consumedBy == null)
            throw new IllegalArgumentException("The message's consumer can't be null!");
        this.consumedBy = consumedBy;
    }
}
