package br.com.mangarosa.my_impl.redis.interfaces;

import java.util.Set;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import redis.clients.jedis.Jedis;

/**
 * Esta classe é uma interface genérica de produtor que utiliza o Redis.
 * Ela representa um Producer que produz e publica mensagens para lá.
 * @author Denilson Santos
 */

public class RedisProducer implements Producer {
    /** Nome descritivo do produtor. */
    public String name;
    /** Chave do Redis para acessar os tópicos em que este produtor publica. */
    public String redisKey;

    public RedisProducer(String producerName) {
        this.name = producerName;
        this.redisKey = "producer:" + this.name + ":topics";
    }

    /**
     * Adiciona um novo tópico à lista de tópicos em que o produtor publica.
     * @param topic (Topic) novo tópico que o produtor utilizará para fazer publicações.
     */
    @Override
    public void addTopic(Topic topic) {
        final RedisRepository repository = (RedisRepository) topic.getRepository();
        repository.getController().sadd(redisKey, topic.name());
    }

    /**
     * Remove um tópico da lista de tópicos em que o produtor publica.
     * @param topic (Topic) tópico o qual o produtor deixará de publicar.
     */
    @Override
    public void removeTopic(Topic topic) {
        final RedisRepository repository = (RedisRepository) topic.getRepository();
        repository.getController().srem(redisKey, topic.name());
    }

    /**
     * Envia uma mensagem em todos os tópicos atrelados a este produtor.
     * Todos os consumidores destes tópicos receberão a mensagem.
     * @param message (String) Mensagem a ser publicada.
     */
    @Override
    public void sendMessage(String message) {
        final RedisRepository repository = new RedisRepository();
        final Jedis controller = repository.getController();
        final Set<String> topicsProducer = controller.smembers(redisKey);
        for (String topic : topicsProducer) {
            controller.publish(topic, message);
        }
    }

    /**
     * @return o nome deste produtor (deve ser único em um broker)
     */
    @Override
    public String name() {
        return this.name;
    }
}
