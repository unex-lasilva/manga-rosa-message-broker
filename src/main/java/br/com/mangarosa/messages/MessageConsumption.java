package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;

import java.time.LocalDateTime;

/**
 * Registra o consumo de uma mensagem por um consumer
 */
public class MessageConsumption {

    private final Consumer consumer;

    private final LocalDateTime consumedAt;

    public MessageConsumption(Consumer consumer){
        this.consumer = consumer;
        this.consumedAt = LocalDateTime.now();
    }

    /**
     * Retorna o consumidor
     * @return consumer
     */
    public Consumer getConsumer() {
        return consumer;
    }

    /**
     * Retorna o horário que foi consumido
     * @return data e hora que foi consumida a mensagem
     */
    public LocalDateTime getConsumedAt() {
        return consumedAt;
    }
}
