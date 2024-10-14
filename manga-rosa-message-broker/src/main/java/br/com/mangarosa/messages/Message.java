package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.my_impl.redis.utils.RedisMessagesUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Esta classe representa uma mensagem a ser processada.
 * 
 * A mensagem pode ser construída de duas formas:
 * (1) Por fornecer os valores manualmente e
 * (2) Por fornecer uma json string contendo os valores.
 * 
 * Esta classe é uma modificação da base fornecida pelo professor Lucas Almeida, que adiciona as seguintes mudanças...
 * (1) Maior flexibilidade para manipular a mensagem e seus valores e
 * (2) Suporte estendido para converter uma mensagem salva no formato Json String em um objeto Java
 * 
 * (Útil para processar tanto mensagens que serão enviadas ao Redis quanto as mensagens obtidas a partir de lá).
 * @author Denilson Santos (fez modificações em cima da classe-base)
 * @author Professor Lucas Oliveira (criou a classe-base)
 */

public class Message implements Serializable {

    // ------------------------------------------------ //
    // ------------------ Atributos ------------------- //
    // ------------------------------------------------ //

    /** Identificador único da mensagem. */
    private String id;
    /** Produtor da mensagem; quem a produziu. */
    private Producer producer;
    /** Horário de criação da mensagem (atribuído imutável após definição). */
    private final LocalDateTime createdAt;
    /** Histórico dos consumos desta mensagem (a lista é imutável, não pode ser reatribuída). */
    private final List<MessageConsumption> consumptionList;
    /** A mensagem foi consumida? A resposta é representada por booleano true ou false. */
    private boolean isConsumed;
    /** O conteúdo da mensagem. */
    private String message;

    // ------------------------------------------------ //
    // ----------------- Construtores ----------------- //
    // ------------------------------------------------ //

    /**
     * Construtor da classe Message.
     * Instancia a mensagem atribuindo automaticamente os seguintes atributos...
     * - Horário de criação (utilizará o horário do momento em que a mensagem foi instanciada);
     * - Lista de consumos (iniciará com uma lista vazia).
     * @param producer O produtor da mensagem.
     * @param message O conteúdo da mensagem.
     */
    public Message(Producer producer, String message){
        setProducer(producer);
        setMessage(message);
        this.createdAt = LocalDateTime.now();
        this.consumptionList = new ArrayList<>();
    }

    /**
     * Construtor da classe Message.
     * Instancia a mensagem atribuindo automaticamente os seguintes atributos...
     * - Lista de consumos (iniciará com uma lista vazia).
     * Este construtor permite que o horário da mensagem seja fornecido previamente.
     * @param producer O produtor da mensagem.
     * @param createdAt Horário em que a mensagem foi criada.
     * @param message O conteúdo da mensagem.
     */
    public Message(Producer producer, LocalDateTime createdAt, String message){
        setProducer(producer);
        setMessage(message);
        this.createdAt = createdAt;
        this.consumptionList = new ArrayList<>();
    }

    /**
     * Construtor da classe Message.
     * Instancia a mensagem fornecendo os valores manualmente
     * @param id O identificador único da mensagem.
     * @param producer O produtor da mensagem.
     * @param createdAt Horário em que a mensagem foi criada.
     * @param consumptionList Histórico dos consumos da mensagem (inclui quem consumiu e o horário em que consumiu a mensagem), no formato de lista.
     * @param isConsumed A mensagem foi consumida? Passar um booleano true/false.
     * @param message O conteúdo da mensagem.
     */
    public Message(String id, Producer producer, LocalDateTime createdAt, List<MessageConsumption> consumptionList, boolean isConsumed, String message){
        setId(id);
        setProducer(producer);
        setConsumed(isConsumed);
        setMessage(message);
        this.createdAt = createdAt;
        this.consumptionList = consumptionList;
    }

    /**
     * Construtor da classe Message.
     * Instancia a mensagem a partir de uma Json String contendo todos os metadados da mensagem.
     * A string deve se parecer com algo como...
     * {
     * "createdAt": "2024-10-03T17:08:22.270320600",
     * "isConsumed": "false",
     * "consumptionList": "[
     *      {consumedAt=2024-10-03T17:42:19.311416, consumerName=consumer1},
     *      {consumedAt=2024-10-03T18:41:01.313116, consumerName=consumer2}
     * ]",
     * "producer": "RedisProducer",
     * "id": "1b88cb05-81c3-11ef-9700-7f6ddafa8e7c",
     * "message": "olá mundo!"
     * }
     */
    public Message(String jsonString) {
        JsonNode json = RedisMessagesUtils.getJsonNode(jsonString);
        if (json == null)
            throw new IllegalArgumentException("Invalid JSON String sent to the constructor");
        this.id = json.get("id").asText();
        this.producer = RedisMessagesUtils.getProducerByName(json.get("producer").asText());
        this.createdAt = LocalDateTime.parse(json.get("createdAt").asText());
        this.consumptionList = RedisMessagesUtils.getConsumptionsListFromString(json.get("consumptionList").asText());
        this.isConsumed = Boolean.parseBoolean(json.get("isConsumed").asText());
        this.message = json.get("message").asText();
    }

    // ------------------------------------------------ //
    // ------------------- Setters -------------------- //
    // ------------------------------------------------ //

    /**
     * Atribui um identificador (que deve ser único) à mensagem
     * @param id id da mensagem
     */
    public void setId(String id){
        if (id == null || id.isBlank() || id.isEmpty())
            throw new IllegalArgumentException("The message id can't be null, blank or empty");
        this.id = id;
    }

    /**
     * Atrela um produtor à mensagem
     * @param producer produtor: quem produziu a mensagem
     */
    public void setProducer(Producer producer) {
        if (producer == null)
            throw new IllegalArgumentException("The message's producer can't be null");
        this.producer = producer;
    }

    /**
     * Especifica se a mensagem já foi consumida
     * @param consumed foi consumida?
     */
    public void setConsumed(boolean consumed) {
        this.isConsumed = consumed;
    }

    /**
     * Determina o conteúdo da mensagem
     * @param message conteúdo da mensagem
     */
    public void setMessage(String message) {
        if (message == null || message.isBlank() || message.isEmpty())
            throw new IllegalArgumentException("The message content can't be null or empty or blank");
        this.message = message;
    }

    // ------------------------------------------------ //
    // ------------------- Getters -------------------- //
    // ------------------------------------------------ //

    /**
     * @return o identificador único da mensagem
     */
    public String getId() {
        return this.id;
    }

    /**
     * @return o produtor da mensagem (a instância Producer)
     */
    public Producer getProducer() {
        return this.producer;
    }

    /**
     * @return o horário em que a mensagem foi criada
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * @return o histórico de consumos da mensagem (incluindo o nome do consumidor e o horário em que a mensagem foi consumida)
     */
    public List<MessageConsumption> getConsumptionList() {
        return this.consumptionList;
    }

    /**
     * @return se a mensagem foi consumida ou não (true/false)
     */
    public boolean isConsumed() {
        return this.isConsumed;
    }
    
    /**
     * @return o conteúdo da mensagem
     */
    public String getMessage() {
        return this.message;
    }

    // ------------------------------------------------ //
    // --------- Métodos de Obtenção de Dados --------- //
    // ------------------------------------------------ //
    
    /**
     * Diferentemente do método getProducer, este retorna o nome do produtor, e não a instância
     * @return o nome do produtor (em String)
     */
    public String getProducerName() {
        return this.producer.getClass().getSimpleName();
    }

    /**
     * Obtém os consumidores desta mensagem, transformados de objetos para HashMaps
     * @return uma lista de HashMaps, cada map representa um consumidor e é composto por pares de chave-valor do tipo String.
     */
    public List<Map<String, String>> getConsumersInfoMap(){
        List<Map<String, String>> mapsList = new ArrayList<>();
        for (MessageConsumption consumption : this.getConsumptionList()) {
            try {
                mapsList.add(RedisMessagesUtils.getConsumerInfoMap(consumption));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } return mapsList;
    }

    /**
     * Obtém os consumidores desta mensagem, transformados de objetos para json string
     * @return uma json string, representando a lista de consumidores da mensagem.
     */
    public String getConsumersJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        mapper.registerModule(new JavaTimeModule());
        try {
            jsonString = mapper.writeValueAsString(this.getConsumersInfoMap());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } return jsonString;
    }

    // ------------------------------------------------ //
    // ------ Métodos de Manipulação da Mensagem ------ //
    // ------------------------------------------------ //

    /**
     * Adiciona o consumo da mensagem
     * @param consumer consumer (quem consumiu a mensagem)
     */
    public void addConsumption(Consumer consumer){
        if(consumer == null)
            throw new IllegalArgumentException("Consumer can't be null in a consumptio");
        this.consumptionList.add(new MessageConsumption(consumer));
    }

    // ------------------------------------------------ //
    // ------------- Métodos de Conversão ------------- //
    // ------------------------------------------------ //

    /**
     * Transforma a mensagem em um HashMap
     * @return um HashMap contendo pares de chave-valor, ambos são do tipo String
     */
    public Map<String, String> toMap() throws IllegalAccessException {
        final Map<String, String> map = new HashMap<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            Object value = field.get(this);
            if(value != null) {
                map.put(field.getName(), value.toString());
            }
        } return map;
    }
     
    /**
     * Aplica mudanças no HashMap gerado a partir do método toMap().
     * O map obtido aqui é capaz de transportar todos os metadados da mensagem para uma outra aplicação,
     * algo que não é possível ao armazenar somente os endereços de memória dos objetos próprios desta aplicação.
     * @return um HashMap contendo pares de chave-valor, ambos são do tipo String
     */
     
     public Map<String, String> toPortableMap() throws IllegalAccessException {
        final Map<String, String> messageMap = this.toMap();
        final Map<String, String> modificationsMap = new HashMap<>();
        modificationsMap.put("consumptionList", this.getConsumersInfoMap().toString());
        modificationsMap.put("producer", this.getProducerName());
        for (Map.Entry<String, String> modificacao : modificationsMap.entrySet()) {
            messageMap.put(modificacao.getKey(), modificacao.getValue());
        } return messageMap;
    }

    /**
     * Transforma a mensagem em uma string que segue a mesma estrutura de um json
     * @return uma String em formato de json
     */
    public String toJsonString(){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        mapper.registerModule(new JavaTimeModule());
        try {
            jsonString = mapper.writeValueAsString(this.toPortableMap());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } return jsonString;
    }
}