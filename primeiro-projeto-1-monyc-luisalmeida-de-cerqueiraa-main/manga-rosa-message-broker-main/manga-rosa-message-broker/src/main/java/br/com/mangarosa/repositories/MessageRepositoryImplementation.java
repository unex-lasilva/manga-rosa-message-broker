package br.com.mangarosa.repositories;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

import java.util.*;

public abstract class MessageRepositoryImplementation implements MessageRepository {
    // Mapeia o nome do tópico para uma fila de mensagens
    private final Map<String, Queue<Message>> topics = new HashMap<>();
    private Message message;
    private String topic;

    // Adiciona uma mensagem ao tópico
    @Override
    public void append(String topic, Message message) {

    }

    // Marca a mensagem como consumida com base no UUID
    @Override
    public void consumeMessage(String topic, UUID messageId) {

    }

    // Retorna uma lista de todas as mensagens não consumidas de um tópico
    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) throws IllegalArgumentException {
        // Retorna uma lista vazia por enquanto
        return List.of();
    }

    // Retorna uma lista de todas as mensagens consumidas de um tópico
    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        this.topic = topic;
        // Retorna uma lista vazia
        return List.of();
    }

    // Adiciona uma mensagem
    @Override
    public void append(Message message) {
        this.message = message;
    }

    // Busca uma mensagem pelo ID
    @Override
    public Message find(String id) {
        // Retorna null por enquanto
        return null;
    }

    // Adiciona uma mensagem a um tópico específico
    @Override
    public void addMessageToTopic(String topicName, Message message) {
        // Se o tópico ainda não existir, cria uma nova fila
        topics.putIfAbsent(topicName, new LinkedList<>());
        // Adiciona a mensagem à fila do tópico
        topics.get(topicName).add(message);
    }

    // Obtém e remove a primeira mensagem de um tópico
    @Override
    public Message getMessageFromTopic(String topicName) {
        // Obtém a fila de mensagens do tópico
        Queue<Message> queue = topics.get(topicName);
        if (queue != null) {
            // Remove mensagens expiradas da fila
            queue.removeIf(Message::isExpired);
            // Retorna e remove a primeira mensagem da fila
            return queue.poll();
        }
        // Retorna null se não houver mensagens
        return null;
    }


}
