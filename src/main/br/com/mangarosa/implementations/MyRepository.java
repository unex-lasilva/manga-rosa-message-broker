package main.br.com.mangarosa.implementations;

import main.br.com.mangarosa.interfaces.MessageRepository;
import main.br.com.mangarosa.interfaces.Topic;
import main.br.com.mangarosa.messages.Message;

import java.util.*;

public class MyRepository implements MessageRepository
{
    private final Map<String, Topic> topics;

    public MyRepository(HashMap<String, Topic> t) {
        topics = t;
    }

    @Override
    public void append(String topic, Message message) {
        if (topics.containsKey(topic)) {
            topics.get(topic).addMessage(message);
        } else {
            topics.put(topic, new Topico(topic));
            topics.get(topic).addMessage(message);
        }
    }

    @Override
    public void consumeMessage(String topic, String messageId) {
        try {
            topics.get(topic).getMessages().forEach((e) -> {
                if (e.getId().equals(messageId)) {
                    e.setConsumed(true);
                   System.out.println(e);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        List<Message> messages = new ArrayList<>();
        try {
            topics.get(topic).getMessages().forEach((e) -> {
                if (!e.isConsumed()) {
                    messages.add(e);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        List<Message> messages = new ArrayList<>();
        try {
            topics.get(topic).getMessages().forEach((e) -> {
                if (e.isConsumed()) {
                    messages.add(e);
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    @Override
    public List<Message> getAllMessagesByTopic(String topic) {
        try {
            return topics.get(topic).getMessages();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
