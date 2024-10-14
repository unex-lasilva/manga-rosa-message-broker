package br.com.mangarosa.producer;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhysicPersonDeliveryProducer implements Producer
{
    private final Map<String, Topic> topicList;
    private final String producerName;

    public PhysicPersonDeliveryProducer(String name) {
        topicList = new HashMap<>();
        producerName = name;
    }

    @Override
    public void addTopic(Topic topic) {
        try {
            if (topic.isCreated()) {
                if (!topicList.containsKey(topic.name())) {
                    topicList.put(topic.name(), topic);
                    System.out.printf("O tópico \"%s\" foi adicionado com sucesso!%n", topic.name());
                } else
                    System.out.println("Este tópico já existe na fila");
            } else
                throw new Exception("Tópico não criado");
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Override
    public void removeTopic(Topic topic) {
        try {
            if (topicList.containsKey(topic.name())) {
                topicList.remove(topic.name());
                System.out.printf("O tópico \"%s\" foi removido com sucesso!%n", topic.name());
            } else
                System.out.println("Tópico não encontrado");
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String name() {
        return producerName;
    }

    @Override
    public HashMap<String, Message> generateMessage(String message, int count) {
        HashMap<String, Message> messages = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String messageId = UUID.randomUUID().toString();
            Message m = new Message()
                    .setMessage(message)
                    .setId(messageId)
                    .setProducer(this);
            messages.put(messageId, m);
        }
        return messages;
    }
}