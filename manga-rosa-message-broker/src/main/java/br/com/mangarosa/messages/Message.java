package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;

import java.time.LocalDateTime;

/**
 * Messagem para ser processada
 */
public class Message {

    private Producer producer;
    private final LocalDateTime createdAt;
    private LocalDateTime consumedAt;
    private boolean isConsumed;
    private String message;
    private Consumer consumedBy;

    public Message(Producer producer, String message){
        setProducer(producer);
        setMessage(message);
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Retorna o produtor que criou a mensagem
     * @return o producer
     */
    public Producer getProducer() {
        return producer;
    }

    private void setProducer(Producer producer) {
        if(producer == null)
            throw new IllegalArgumentException("The message's producer can't be null");
        this.producer = producer;
    }

    /**
     * Retorna o horário de criação da mensagem
     * @return o horário que foi criado
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Retorna se a mensagem foi consumida ou não
     * @return se foi consumido
     */
    public boolean isConsumed() {
        return isConsumed;
    }

    /**
     * Informa que a mensagem foi consumida
     * @param consumed se foi consumido
     */
    public void setConsumed(boolean consumed) {
        isConsumed = consumed;
        this.consumedAt = LocalDateTime.now();
    }

    /**
     * Retorna a mensagem que deve ser consumida
     * @return mensagem
     */
    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        if(message == null || message.isBlank() || message.isEmpty())
            throw new IllegalArgumentException("The message content can't be null or empty or blank");
        this.message = message;

    }

    /**
     * Retorna o consumidor que consumiu a mensagem
     * @return consumidor
     */
    public Consumer getConsumedBy() {
        return consumedBy;
    }

    /**
     * Atribui o consumidor que consumiu a mensagem
     * @param consumedBy consumidor
     */
    public void setConsumedBy(Consumer consumedBy) {
        if(consumedBy == null)
            throw new IllegalArgumentException("The message's consumer can't be null!");
        this.consumedBy = consumedBy;
    }

    /**
     * Retorna a data e hora que a mensagem foi consumida
     * @return data e hora do consumo
     */
    public LocalDateTime getConsumedAt(){
        return this.consumedAt;
    }
}
