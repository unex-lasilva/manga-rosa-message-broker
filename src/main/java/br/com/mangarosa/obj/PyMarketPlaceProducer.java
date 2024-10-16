package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Producer;
import redis.clients.jedis.Jedis;

/**
 * Produtor responsável por enviar mensagens para o tópico "queue/long-distance-items".
 */
public class PyMarketPlaceProducer implements Producer {

    private final Jedis jedis;

    /**
     * Construtor que recebe uma instância do Jedis.
     * @param jedis Cliente Jedis para conectar ao Redis
     */
    public PyMarketPlaceProducer(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void produce(String topic, String message) {

    }

    /**
     * Envia uma mensagem para o tópico "queue/long-distance-items".
     * @param message A mensagem a ser enviada
     */

    @Override
    public void produce(String message) {
        jedis.rpush("queue/long-distance-items", message);
        System.out.println("Mensagem produzida: " + message);
    }
}
