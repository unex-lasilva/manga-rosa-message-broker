package br.com.mangarosa.producers;

import br.com.mangarosa.topics.Topic;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.messages.Message;


import java.util.ArrayList;

public class TProducer implements Producer {
// A classe TProducer implementa a interface Producer

    private String nameProducer;
    private final ArrayList<Topic> topics;

    public TProducer(String nameProducer) {
        this.nameProducer = nameProducer;
        this.topics = new ArrayList<Topic>();
    }

    @Override
    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    @Override
    public void removeTopic(Topic topic) {
        this.topics.remove(topic);
    }

    @Override
    public void sendMessage(String messageContent) {

        Message message = new Message(nameProducer, messageContent);
        for(Topic topic : topics) {
            topic.addMessage(message);
        }
    }

    @Override
    public String name(int index) {
        return topics.get(index).name();
    }
}
