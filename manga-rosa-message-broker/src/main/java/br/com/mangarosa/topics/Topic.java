package br.com.mangarosa.topics;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.ITopic;
import br.com.mangarosa.interfaces.IMessageRepository;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class Topic implements ITopic {
// A classe Topic implementa a interface ITopic, sendo responsável por gerenciar tópicos e consumidores

    private String name;
    private List<Consumer> consumers;
    private MessageRepository repository;

    public Topic(String name) {
        this.name = name;
        this.repository = new MessageRepository();
        this.consumers = new ArrayList<>();
    }
    @Override
    public String name() {
        return name;
    }

    @Override
    public void addMessage(Message message) {
        repository.append(name(), message);
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
    public IMessageRepository getRepository() {
        return repository;
    }

    @Override
    public void notifyConsumers(Message message) {
        ITopic.super.notifyConsumers(message);
    }
}
