package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.util.List;

/***
 * Tópico é uma estrutura de fila onde as mensagens ficam para serem consumidas
 * pelos consumidores.
 */
public interface Topic {
    /***
     * Retorna o nome do tópico
     * @return topic name
     */
    String name();

    /**
     * Adiciona uma mensagem no final da fila do tópico
     * @param message mensagem a ser processada
     */
    void addMessage(Message message);

    /**
     * Adiciona um consumidor das mensagens dos produtores
     * @param consumer consumidor
     */
    void subscribe(Consumer consumer);

    /**
     * Remove um consumidor das mensagens dos produtores
     * @param consumer consumidor
     */
    void unsubscribe(Consumer consumer);

    /**
     * Retorna a lista de consumidores
     * @return lista de consumidores
     */
    List<Consumer> consumers();

    /***
     * notifica a todos os consumidores que tem uma nova mensagem para ser consumida.
     * @param message
     */
    default void notifyConsumers(Message message){
        addMessage(message);

        consumers().forEach(consumer -> consumer.consume(message));
    }
}
