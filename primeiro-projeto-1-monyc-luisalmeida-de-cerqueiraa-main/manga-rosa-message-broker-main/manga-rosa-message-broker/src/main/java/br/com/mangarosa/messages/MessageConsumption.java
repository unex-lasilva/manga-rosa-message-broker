package br.com.mangarosa.messages;

import java.time.LocalDateTime;

/**
 * Registra o consumo de uma mensagem por um consumer
 */
public class MessageConsumption {

    private final br.com.mangarosa.interfaces.consumer consumer;

    private final LocalDateTime consumedAt;

    public MessageConsumption(br.com.mangarosa.interfaces.consumer consumer){
        this.consumer = consumer;
        this.consumedAt = LocalDateTime.now();
    }

    /**
     * Retorna o consumidor
     * @return consumer
     */
    public br.com.mangarosa.interfaces.consumer getConsumer() {
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
