package br.com.mangarosa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class TTopic implements Topic {

    private String name;
    private Queue<Message> mensagens;
    private List<Consumer> consumers;
    private MessageRepository repository;

    public TTopic(String name, MessageRepository repository) {
        this.name = name;
        this.mensagens = new LinkedList<>();
        this.consumers = new ArrayList<>();
        this.repository = repository;
    }

    @Override
    public String name() {
        return this.name;
    }

    /**
     * Adiciona uma mensagem no final da fila do tópico
     *
     * @param message mensagem a ser processada
     */
    @Override
    public void addMessage(Message message) {
        this.mensagens.add(message);
    }

    /**
     * Adiciona um consumidor das mensagens dos produtores
     *
     * @param consumer consumidor
     */
    @Override
    public void subscribe(Consumer consumer) {
        this.consumers.add(consumer);
    }

    /**
     * Remove um consumidor das mensagens dos produtores
     *
     * @param consumer consumidor
     */
    @Override
    public void unsubscribe(Consumer consumer) {
        this.consumers.remove(consumer);
    }

    /**
     * Retorna a lista de consumidores
     *
     * @return lista de consumidores
     */
    @Override
    public List<Consumer> consumers() {
        return this.consumers;
    }

    public Queue<Message> mensagens() {
        return this.mensagens;
    }

    /**
     * Retorna o repository que conecta com o banco chave-valor
     *
     * @return repository
     */
    @Override
    public MessageRepository getRepository() {
        return this.repository;
    }

}
