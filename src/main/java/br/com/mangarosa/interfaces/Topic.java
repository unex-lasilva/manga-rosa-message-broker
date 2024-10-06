package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Interface que representa um tópico, que é uma estrutura de fila onde mensagens são armazenadas
 * para serem consumidas pelos consumidores.
 */
public interface Topic extends Serializable {

    /**
     * Retorna o nome do tópico.
     *
     * @return O nome do tópico.
     */
    String name();

    /**
     * Adiciona uma mensagem ao final da fila do tópico.
     *
     * @param message A mensagem a ser processada.
     */
    void addMessage(Message message);

    /**
     * Adiciona um consumidor que receberá mensagens dos produtores.
     *
     * @param consumer O consumidor a ser adicionado.
     */
    void subscribe(Consumer consumer);

    /**
     * Remove um consumidor que receberia mensagens dos produtores.
     *
     * @param consumer O consumidor a ser removido.
     */
    void unsubscribe(Consumer consumer);

    /**
     * Retorna a lista de consumidores associados ao tópico.
     *
     * @return Uma lista de consumidores.
     */
    List<Consumer> consumers();

    /**
     * Retorna o repositório que conecta com o banco de dados chave-valor.
     *
     * @return O repositório de mensagens.
     */
    MessageRepository getRepository();

    /**
     * Notifica todos os consumidores sobre a nova mensagem que está disponível para consumo.
     *
     * @param message A mensagem que deve ser notificada.
     */
    default void notifyConsumers(Message message) {
        consumers().forEach(consumer -> {
            CompletableFuture<Boolean> completableFuture = CompletableFuture
                    .supplyAsync(() -> consumer.consume(message));
            completableFuture.thenAccept(result -> {
                // Pode-se adicionar lógica para lidar com o resultado, se necessário
                System.out.printf("Mensagem consumida: %s\n", message); // Mensagem de log
            });
        });
    }
}
