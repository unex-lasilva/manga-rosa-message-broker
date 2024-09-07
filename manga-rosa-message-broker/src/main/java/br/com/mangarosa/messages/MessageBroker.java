package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Topic;

import java.util.HashMap;
import java.util.Map;

public class MessageBroker {

    private final Map<String, Topic> topics;

    public MessageBroker(){
        this.topics = new HashMap<>();
    }

    public void createTopic(Topic topic){
        if(topics.containsKey(topic.name()))
            throw new IllegalArgumentException("The topic name already exists");
        this.topics.put(topic.name(), topic);
    }

    public void removeTopic(String topic){
        if(!topics.containsKey(topic))
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        Topic t = this.topics.get(topic);
        t.consumers().forEach(t::unsubscribe);
        topics.remove(topic);
    }

    public void subscribe(String topic, Consumer consumer){
        if(!topics.containsKey(topic))
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        Topic t = this.topics.get(topic);
        t.subscribe(consumer);
    }

    public void unsubscribe(String topic, Consumer consumer){
        if(!topics.containsKey(topic))
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        Topic t = this.topics.get(topic);
        t.unsubscribe(consumer);
    }

    public Topic getTopicByName(String topic){
        if(!topics.containsKey(topic))
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        return this.topics.get(topic);
    }
}
