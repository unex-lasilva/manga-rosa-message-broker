package br.com.mangarosa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class TTopic implements Topic{
    private String name;
    private Queue<Message> messages;
    private List<Consumer> consumers;
    private MessageRepository repository;

    TTopic(String name, MessageRepository repository) {
        this.name = name;
        this.consumers = new ArrayList<>();
        this.repository = repository;

        this.messages = new LinkedList<>();
    }

    @Override
    public String name() {
        // retorna o nome do tópico
        return this.name;
    }

    @Override
    public void addMessage(Message message) {
        // adiciona a msg
       this.messages.add(message);
    }

    @Override
    public void subscribe(Consumer consumer) {
        // adiciona o consumidor
        this.consumers.add(consumer);
    }

    @Override
    public void unsubscribe(Consumer consumer) {
        // remove o consumidor
        this.consumers.remove(consumer);
    }

    @Override
    public List<Consumer> consumers() {
        // retorna a lista de consumidores
        return this.consumers;
    }

    @Override
    public MessageRepository getRepository() {
        // retorna o repository
        return repository;
    }

}
