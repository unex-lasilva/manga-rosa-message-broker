package br.com.mangarosa.my_impl.redis.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.my_impl.redis.utils.RedisMessagesUtils;
import br.com.mangarosa.my_impl.redis.utils.NewTopicUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Esta classe é uma interface genérica de tópico que utiliza o Redis.
 * Um tópico é um canal de mensagens dentro do Redis que guarda mensagens voltadas a um perfil de cliente (consumer) específico.
 * Um repositório pode contar com vários tópicos.
 * Por isso, para ampla utilização, é recomendado implementar a partir desta interface genérica classes de tópicos mais específicos.
 */

public class NewTopic implements Topic {
    public String name;
    /** Repositório ao qual o tópico pertence. */
    @JsonIgnore
    public RedisRepository repository;

    // ------------------------------------------------ //
    // ----------------- Construtores ----------------- //
    // ------------------------------------------------ //

    /**
     * Construtor da classe RedisTopic.
     * Por padrão, novas instâncias se conectam automaticamente a uma instância padrão de repositório Redis.
     */
    public NewTopic(String topicName){
        this.name = topicName;
        this.repository = new RedisRepository();
    }

    /**
     * Construtor da classe RedisTopic.
     * Este construtor permite atrelar um tópico a um repositório Redis já iniciado previamente.
     * @param repository repositório Redis ao qual este tópico ficará atrelado.
     */
    public NewTopic(String topicName, RedisRepository repository){
        this.name = topicName;
        this.repository = repository;
    }

    // ------------------------------------------------ //
    // -------------------- Métodos ------------------- //
    // ------------------------------------------------ //

    /**
     * @return o nome deste tópico
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * Adiciona uma mensagem ao final da fila do tópico
     * @param message (Message) mensagem a ser adicionada
     */
    @Override
    public void addMessage(Message message) {
        this.getRepository().append(this.name(), message);
    }

    /**
     * Adiciona um novo consumidor para este tópico.
     * @param consumer (Consumer) consumidor
     */
    @Override
    public void subscribe(Consumer consumer) {
        this.getRepository().getController().sadd(NewTopicUtils.getConsumersKey(this.name()), consumer.name());
    }


    /**
     * Remove um consumidor deste tópico
     * @param consumer (Consumer) consumidor
     */
    @Override
    public void unsubscribe(Consumer consumer) {
        this.getRepository().getController().srem(NewTopicUtils.getConsumersKey(this.name()), consumer.name());
    }

    // ------------------------------------------------ //
    // ---------------- Métodos Getters --------------- //
    // ------------------------------------------------ //

    /**
     * @return uma lista contendo todos os consumidores deste tópico
     */
    @Override
    public List<Consumer> consumers() {
        List<Consumer> consumers = new ArrayList<>();
        String topicKey = NewTopicUtils.getConsumersKey(this.name());
        Set<String> consumersName = this.getRepository().getController().smembers(topicKey);
        for (String consumerName : consumersName) {
            consumers.add(RedisMessagesUtils.getConsumerByName(consumerName));
        } return consumers;
    }

    /**
     * @return (RedisRepository) o repositório atrelado a este tópico
     */
    @Override
    public RedisRepository getRepository() {
        return this.repository;
    }
}