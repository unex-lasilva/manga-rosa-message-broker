package br.com.mangarosa.messages; // Adicione esta linha no início do arquivo
import br.com.mangarosa.HProducer;
import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;

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
    private HProducer producer;

    private final LocalDateTime createdAt;

    private final List<MessageConsumption> consumptionList;
    private boolean isConsumed;
    private String message;


    public Message(HProducer producer, String message){
        setProducer(producer);
        setMessage(message);
        this.createdAt = LocalDateTime.now();
        this.consumptionList = new ArrayList<>();
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
    public HProducer getProducer() {
        return producer;
    }

    private void setProducer(HProducer producer) {
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


    /**
     * Cria um objeto Message a partir de uma string formatada.
     * Este método é usado principalmente no RedisMessageRepository para reconstruir objetos Message
     * a partir de strings armazenadas no Redis.
     *
     * @param messageStr A string formatada contendo os dados da mensagem (formato: "id|conteúdo")
     * @return Um novo objeto Message criado a partir da string fornecida
     */
    public static Message fromString(String messageStr) {
        // Divide a string em partes usando o caractere '|' como separador
        String[] parts = messageStr.split("\\|");
        
        // Cria uma nova mensagem com um novo HProducer e o conteúdo da mensagem (terceira parte)
        // Nota: Aqui, um novo HProducer é criado, o que pode não refletir o produtor original
        Message message = new Message(new HProducer(), parts[1]);
        
        // Define o ID da mensagem (primeira parte)
        message.setId(parts[0]);
        
        // Define o status de consumo da mensagem (segunda parte, convertida para boolean)
        
        return message;
    }

    /**
     * Retorna uma representação em string da mensagem.
     * Este método é usado no RedisMessageRepository para converter objetos Message em strings
     * antes de armazená-los no Redis.
     *
     * @return Uma string representando a mensagem no formato "id|isConsumed|conteúdo"
     */
    @Override
    public String toString() {
        // Formata a string com o ID, status de consumo e conteúdo da mensagem
        return String.format("%s|%s", id, message);
    }

}
