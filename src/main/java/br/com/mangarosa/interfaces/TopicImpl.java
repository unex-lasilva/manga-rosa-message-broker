package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class TopicImpl implements Topic {

    private final String name;
    private final MessageRepository messageRepository; // Adicione um repositório de mensagens
    private final List<Consumer> consumers = new ArrayList<>();

    public TopicImpl(String name, MessageRepository messageRepository) {
        this.name = name;
        this.messageRepository = messageRepository; // Inicialize o repositório
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void addMessage(Message message) {
        messageRepository.append(name, message); // Adiciona a mensagem ao repositório
        notifyConsumers(message);
    }

    @Override
    public void subscribe(Consumer consumer) {
        consumers.add(consumer);
    }

    @Override
    public void unsubscribe(Consumer consumer) {
        consumers.remove(consumer);
    }

    @Override
    public List<Consumer> consumers() {
        return consumers;
    }

    @Override
    public MessageRepository getRepository() {
        return messageRepository; // Retorna o repositório de mensagens
    }
}
