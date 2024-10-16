package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;

/**
 * Broker de mensagens que facilita o envio e o recebimento de mensagens entre produtores e consumidores.
 * Serve como mediador para que as mensagens possam ser produzidas e consumidas através dos tópicos.
 */
public class MessageBroker {

    private final Producer producer;  // Instância do produtor de mensagens
    private final Consumer consumer;  // Instância do consumidor de mensagens

    /**
     * Construtor do MessageBroker.
     * @param producer Instância de um produtor para produzir mensagens
     * @param consumer Instância de um consumidor para consumir mensagens
     */
    public MessageBroker(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    /**
     * Produz uma mensagem em um tópico específico.
     * @param topic O tópico no qual a mensagem será produzida
     * @param message O conteúdo da mensagem a ser enviada
     */
    public void produceMessage(String topic, String message) {
        producer.produce(topic, message);
    }

    /**
     * Consome uma mensagem de um tópico específico.
     * @param topic O tópico de onde a mensagem será consumida
     */
    public void consumeMessage(String topic) {
        consumer.consume(topic);
    }
}


