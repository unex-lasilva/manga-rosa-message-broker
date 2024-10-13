package br.com.mangarosa.interfaces;

import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Producer {
    private String producerId;
    private List<Topic> topics;

    public Producer(String producerId) {
        this.producerId = producerId;
        this.topics = new ArrayList<>();
    }

    public String getProducerId() {
        return producerId;
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public void removeTopic(Topic topic) {
        topics.remove(topic);
    }

    public void produce(String messageContent) {
        Message message = new Message(messageContent, producerId);
        for (Topic topic : topics) {
            topic.addMessage(message);  // Envia a mensagem para o tópico
        }
    }

    public abstract void sendMessage(String message);

    public abstract String name();
}
