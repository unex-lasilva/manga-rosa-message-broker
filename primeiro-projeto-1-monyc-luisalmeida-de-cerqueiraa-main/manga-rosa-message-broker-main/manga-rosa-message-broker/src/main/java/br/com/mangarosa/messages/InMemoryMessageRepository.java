package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.MessageRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class InMemoryMessageRepository implements MessageRepository {
    private final Map<UUID, Message> messages = new HashMap<>();

    @Override
    public void append(Message message) {
        UUID messageId = UUID.randomUUID(); // Gera um novo UUID para a mensagem
        message.setId(UUID.fromString(messageId.toString())); // Certifique-se de que seu método setId aceita uma String
        messages.put(messageId, message); // Adiciona a mensagem ao repositório
    }

    @Override
    public Message find(String id) {
        return messages.values().stream()
                .filter(message -> message.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Implemente outros métodos de MessageRepository conforme necessário
}
