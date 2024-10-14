package br.com.mangarosa.services;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class TopicImpl implements Topic {
    private final String name;
    private final List<Consumer> consumers;
    private final MessageRepository repository;

    public TopicImpl(String name, MessageRepository repository) {
        this.name = name;
        this.repository = repository;
        this.consumers = new ArrayList<>();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void addMessage(Message message) {
        repository.append(name, message);
        // Marcar como consumida no repositório após ser consumida
        repository.consumeMessage(name, message.getId());

        // Notificar consumidores
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
        return repository;
    }
}
