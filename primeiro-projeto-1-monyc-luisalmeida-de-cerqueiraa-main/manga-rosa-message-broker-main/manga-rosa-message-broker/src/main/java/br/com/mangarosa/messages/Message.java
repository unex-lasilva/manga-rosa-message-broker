package br.com.mangarosa.messages;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private String content;
    private String producer;
    private Instant expirationTime; // Adiciona um campo para armazenar o tempo de expiração
    private Object id;

    public Message(String content, String producer) {
        this.content = content;
        this.producer = producer;
        this.expirationTime = Instant.now().plusSeconds(300); // Define a expiração para 5 minutos
    }

    public String getContent() {
        return content;
    }

    public String getProducer() {
        return producer;
    }

    public Instant getExpirationTime() {
        return expirationTime; // Método para obter o tempo de expiração
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expirationTime); // Método para verificar se a mensagem expirou
    }

    public boolean isConsumed() {
        return false;
    }

    public void setConsumed(boolean b) {

    }

    public void setProducer(String pyMarketPlaceProducer) {
    }

    public void setId(UUID uuid) {

    }

    public UUID getId() {
        return (UUID) id;
    }

    public long[] getMessage() {
        return null;
    }
}
