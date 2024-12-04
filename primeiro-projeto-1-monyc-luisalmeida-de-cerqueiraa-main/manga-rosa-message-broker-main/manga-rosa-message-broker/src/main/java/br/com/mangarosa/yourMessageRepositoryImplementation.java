package br.com.mangarosa;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class yourMessageRepositoryImplementation implements MessageRepository {
    private final Map<String, List<Message>> topicMessages = new HashMap<>();

    @Override
    public void save(Message message) {
        topicMessages.computeIfAbsent("queue/fast-delivery-items", k -> new ArrayList<>()).add(message);
    }

    @Override
    public Message find(String id) {
        for (List<Message> messages : topicMessages.values()) {
            for (Message message : messages) {
                if (message.getId().toString().equals(id)) {
                    return message;
                }
            }
        }
        return null; // Null se não existir
    }

    @Override
    public Message find(UUID id) {
        return null;
    }

    @Override
    public void addMessageToTopic(String name, Message message) {

    }

    @Override
    public Message getMessageFromTopic(String topicName) {
        return null;
    }

    @Override
    public void append(String topic, Message message) {

    }

    @Override
    public void consumeMessage(String topic, UUID messageId) {
        List<Message> messages = topicMessages.get(topic);
        if (messages != null) {
            messages.removeIf(message -> message.getId().equals(messageId)); // Remove a mensagem consumida
        }
    }

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topicName) {
        return topicMessages.getOrDefault(topicName, new ArrayList<>());
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        return List.of();
    }

    @Override
    public void append(Message message) {

    }
}
