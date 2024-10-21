package br.com.mangarosa.my_impl.redis.utils;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageConsumption;
import br.com.mangarosa.my_impl.redis.interfaces.NewConsumer;
import br.com.mangarosa.my_impl.redis.interfaces.NewProducer;

import java.time.Duration;
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
 * Este é um pacote de ferramentas para trabalhar com mensagens do Redis.
 * Várias funcionalidades relacionadas ao processamento das mensagens foram reunidas aqui.
 * Não pode ser instanciado.
 */

public class RedisMessagesUtils {

    private RedisMessagesUtils() {}

    /**
     * Transforma o objeto MessageConsumption em um HashMap (pares de chave-valor).
     * Facilita a portabilidade do objeto para outros formatos, como por exemplo, json ou string.
     * @param consumption O consumo instanciado que será convertido
     * @return um HashMap (pares de chave-valor) contendo as informações do consumo da mensagem.
     */
    public static Map<String, String> getConsumerInfoMap(MessageConsumption consumption) throws IllegalAccessException {
        final HashMap<String, String> map = new HashMap<>();
        map.put("consumerName", consumption.getConsumer().name());
        map.put("consumedAt", consumption.getConsumedAt().toString());
        return map;
    }

    /**
     * Transforma uma string json em um objeto Json manipulável pela aplicação.
     * @param jsonString A string seguindo a estrutura de um json
     * @return um objeto Json que pode ser mais facilmente manipulado
     */
    public static JsonNode getJsonNode(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        mapper.registerModule(new JavaTimeModule());
        try {
            json = mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } return json;
    }

    /**
     * Converte o horário de criação armazenado dentro de uma string json para um objeto LocalDateTime manipulável.
     * A conversão é realizada somente na chave "createdAt".
     * @param jsonString A string seguindo a estrutura de um json
     * @return o horário obtido a partir da string json
     */
    public static LocalDateTime getJsonCreatedAt(String jsonString) {
        JsonNode json = getJsonNode(jsonString);
        if (json == null) return null;
        return LocalDateTime.parse(json.get("createdAt").asText());
    }

    /**
     * Retorna o Producer correto a partir do nome passado.
     * @param producerName Nome do producer
     * @return (Producer) o produtor correspondente ao nome passado
     */
    public static Producer getProducerByName(String producerName) {
        return new NewProducer(producerName);
    }

    /**
     * Retorna o Consumer correto a partir do nome passado.
     * @param consumerName Nome do consumer
     * @return (Consumer) o consumidor correspondente ao nome passado
     */
    public static Consumer getConsumerByName(String consumerName) {
        return new NewConsumer(consumerName);
    }

    /**
     * Transforma uma lista de consumos de mensagem salva em formato de string, em uma lista real do Java novamente.
     * Por padrão, os consumos são salvos em json string da seguinte forma:
     * [{consumedAt=2024-10-05T18:41:21.369246200, consumerName=consumer}].
     * A string passada deve seguir o mesmo formato.
     * @param consumptionsListStr representação da lista de consumos em String
     * @return uma lista contendo o registro de consumos da mensagem
     */
    public static List<MessageConsumption> getConsumptionsListFromString(String consumptionsListStr) {
        List<MessageConsumption> consumptionsList = new ArrayList<>();
        if (consumptionsListStr.equals("[]")) return consumptionsList;
        consumptionsListStr = consumptionsListStr.substring(2, consumptionsListStr.length()-2).replace(",", "");
        String[] consumptionsInfos = consumptionsListStr.split("\\} \\{");
        for (String consumptionInfo : consumptionsInfos) {
            consumptionsList.add(getJsonConsumption(consumptionInfo));
        } return consumptionsList;
    }

    /**
     * Obtém um objeto MessageConsumption a partir de um fragmento de string Json
     * O fragmento json deve se parecer com: 
     * "consumedAt=2024-09-29T19:25:47.897282900 consumerName=consumer"
     * @param consumptionInfos O fragmento da string json
     * @return o consumo da mensagem convertido em um objeto Java manipulável
     */
    public static MessageConsumption getJsonConsumption(String consumptionInfos) {
        consumptionInfos = consumptionInfos.replace("consumedAt=", "");
        consumptionInfos = consumptionInfos.replace("consumerName=", "");
        String[] parts = consumptionInfos.split(" ");
        LocalDateTime consumptionTime = LocalDateTime.parse(parts[0]);
        Consumer consumer = getConsumerByName(parts[1]);
        return new MessageConsumption(consumer, consumptionTime);
    }

    /**
     * Verifica e retorna quanto tempo uma mensagem ficou armazenada no repositório
     * @param message mensagem a ser verificada
     * @return há quanto tempo (em segundos) a mensagem está no repositório
     */
    public static int getDurationOfMessage(Message message) {
        final LocalDateTime createdAt = message.getCreatedAt();
        final LocalDateTime timeNow = LocalDateTime.now();
        return (int) Duration.between(createdAt, timeNow).getSeconds();
    }

    /**
     * Verifica e retorna quanto tempo uma mensagem ficou armazenada no repositório
     * @param timeCreation passa o LocalDateTime correspondente ao horário em que a mensagem foi criada
     * @return há quanto tempo (em segundos) a mensagem está no repositório
     */
    public static int getDurationOfMessage(LocalDateTime timeCreation) {
        final LocalDateTime timeNow = LocalDateTime.now();
        return (int) Duration.between(timeCreation, timeNow).getSeconds();
    }
}