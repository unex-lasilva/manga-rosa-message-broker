package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.util.List;

public interface Topic {
    /***
     * Retorna o nome do tópico
     * @return topic name
     */
    String name();

    void addMessage(Message message);
    void subscribe(Consumer consumer);
    void unsubscribe(Consumer consumer);
    List<Consumer> consumers();
    default void notifyConsumers(Message message){
        addMessage(message);
        consumers().forEach(consumer -> consumer.consume(message));
    }
}
