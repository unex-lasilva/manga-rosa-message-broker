package main.br.com.mangarosa.messages;

import main.br.com.mangarosa.interfaces.Consumer;
import main.br.com.mangarosa.interfaces.Producer;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Messagem para ser processada
 */
public class Message implements Serializable {

    private String id;
    private Producer producer;

    private final LocalDateTime createdAt;

    private final List<MessageConsumption> consumptionList;
    private boolean isConsumed;
    private String message;

    public Message() {
        this.createdAt = LocalDateTime.now();
        this.consumptionList = new ArrayList<>();
    }

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public Message setProducer(Producer producer) {
        this.producer = producer;
        return this;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getId() {
        return id;
    }

    /**
     * Retorna o produtor que criou a mensagem
     * @return o producer
     */
    public Producer getProducer() {
        return producer;
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
    }

    /**
     * Retorna a mensagem que deve ser consumida
     * @return mensagem
     */
    public String getMessage() {
        return message;
    }

    /**
     * Adiciona o consumo da mensagem
     * @param consumer consumer
     */
    public void addConsumption(Consumer consumer){
        if(consumer == null)
            throw new IllegalArgumentException("Consumer can't be null in a consumptio");
        this.consumptionList.add(new MessageConsumption(consumer));
    }

    public Map<String, String> toMap() throws IllegalAccessException {
       final HashMap<String, String> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            Object value = field.get(this);
            if(value != null) {
                map.put(field.getName(), value.toString());
            }
        }
        return map;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("createdAt", createdAt.toString());
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("isConsumed", isConsumed);
        jsonObject.addProperty("consumptionList", consumptionList.toString());
        return jsonObject.toString();
    }
}
