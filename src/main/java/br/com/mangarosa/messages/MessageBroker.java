package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MessageBroker {

    private final Map<String, Topic> topics;
    private final MessageRepository repository;
    private final ScheduledExecutorService scheduleAtFixedRate;

    public MessageBroker(MessageRepository repository){
        this.topics = new HashMap<>();
        this.repository = repository;
        this.scheduleAtFixedRate = Executors.newScheduledThreadPool(5);
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

    public void notifyConsumers(){
        Runnable notifyTask = () -> {
            topics.keySet().forEach(key -> {
                        List<Message> messages = repository
                                .getAllNotConsumedMessagesByTopic(key);

                        if(Objects.nonNull(messages)){

                        }
                    });
        };
        ScheduledFuture<?> scheduledFuture = scheduleAtFixedRate
                .scheduleAtFixedRate(notifyTask, 2, 1, TimeUnit.MINUTES);
    }
}
