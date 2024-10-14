package br.com.mangarosa.producer;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashSet;
import java.util.Set;

public class BaseProducer implements Producer {

    private Set<Topic> topics;
    private RedisClient redisClient;
    private RedisCommands<String, String> redisCommands;
    private String producerName; 

    public BaseProducer(RedisClient redisClient, String producerName) {
        this.topics = new HashSet<>();
        this.redisClient = redisClient;
        this.redisCommands = redisClient.connect().sync();
        this.producerName = producerName; 
    }

    @Override
    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    @Override
    public void removeTopic(Topic topic) {
        topics.remove(topic);
    }

    @Override
    public void sendMessage(String message) {
        for (Topic topic : topics) {
            // Adiciona a mensagem ao tópico
            Message m = new Message(this, message);
            topic.addMessage(m);
        }
    }

    // Retorna o nome do producer
    @Override
    public String name() {
        return this.producerName;
    }
    @Override
    public String toString() {
        return name();
    }
}

