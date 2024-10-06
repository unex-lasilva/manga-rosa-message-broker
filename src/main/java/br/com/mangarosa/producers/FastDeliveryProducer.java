package br.com.mangarosa.producers;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

/**
 * Classe que representa um produtor de mensagens para entregas rápidas.
 */
public class FastDeliveryProducer {
    private MessageRepository repository;
    private String topic;

    public FastDeliveryProducer(MessageRepository repository, String topic) {
        this.repository = repository;
        this.topic = topic;
    }

    /**
     * Produz uma nova mensagem para o tópico de entrega rápida.
     *
     * @param message A mensagem a ser produzida.
     */
    public void produceMessage(Message message) {
        repository.append(topic, message);
    }
}
