package br.com.mangarosa.messages;

import br.com.mangarosa.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
// Declara a classe `Message`, que implementa `Serializable` para permitir a serialização de suas instâncias

    private String id;
    private String producer;

    private final LocalDateTime createdAt;

    private final List<String> consumptionList;
    private boolean isConsumed;
    private String message;

    public Message(String producer, String message){
    // Construtor da classe Message

        setProducer(producer);
        setMessage(message);
        this.createdAt = LocalDateTime.now();
        this.consumptionList = new ArrayList<>();
    }

    public List<String> getConsumptionList() {
        return consumptionList;
        // Retorna a lista de consumidores que consumiram a mensagem
    }

    }


    /**
     * Retorna o id da mensagem baseado na data de criação
     * @return o id da mensagem
     */
    public String getId() {
        return id;
    }

    /**
     * Atribui o valor de id gerado
     * @param id id da mensagem
     */
    public void setId(String id){
        if(id == null || id.isBlank() || id.isEmpty())
            throw new IllegalArgumentException("The message id can't be null, blank or empty");
        this.id = id;
    }

    /**
     * Retorna o produtor que criou a mensagem
     * @return o producer
     */
    public String getProducer() {
        return producer;
    }

    private void setProducer(String producer) {
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
     * Adiciona o consumo da mensagem
     * @param consumer consumer
     */
    public void addConsumption(String consumer){
        if(consumer == null)
            throw new IllegalArgumentException("Consumer can't be null in a consumptio");
        this.consumptionList.add(consumer);
    }

    // Converte a mensagem em um mapa, refletindo seus campos
    public Map<String, String> toMap() throws IllegalAccessException {
       final HashMap<String, String> map = new HashMap<>(); // Cria um novo mapa para armazenar os campos
        Field[] fields = this.getClass().getDeclaredFields(); // Obtém todos os campos da classe
        for (Field field: fields) {
            field.setAccessible(true); // Permite acesso a campos privados
            Object value = field.get(this);  // Obtém o valor do campo
            if(value != null) {
                map.put(field.getName(), value.toString()); // Adiciona o campo e seu valor ao mapa
            }
        }
        return map; // Retorna o mapa com os campos da mensagem
    }

    public String toJson() {
    // Converte a mensagem em uma representação JSON

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())  // Registra adaptador para LocalDateTime
                .create();

        return gson.toJson(this); // Converte a instância da mensagem em JSON
    }

    public static Message fromJson(String json) {
    // Converte uma representação JSON em uma instância de mensagem

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())  // Registra adaptador para LocalDateTime
                .create();
        return gson.fromJson(json, Message.class); // Converte o JSON em uma instância da classe Message
    }
}
