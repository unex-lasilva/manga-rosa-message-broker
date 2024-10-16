package br.com.mangarosa.config;

import br.com.mangarosa.interfaces.MessageRepository;
import redis.clients.jedis.Jedis; // Importação da biblioteca Jedis para conectar ao Redis


    /**
    * Implementação de MessageRepository usando Redis.
    * Utiliza Jedis para salvar e buscar mensagens em uma lista no Redis.
    */

public class RedisMessageRepository implements MessageRepository {

    private final Jedis jedis; // Cliente Jedis para interação com Redis
    private static final int MESSAGE_EXPIRATION_SECONDS = 60; // Tempo de expiração da mensagem (60 segundos)


    /**
     * Construtor que recebe uma instância do Jedis para interação com Redis..
     * @param jedis Cliente Jedis para conectar ao Redis
     */

    public RedisMessageRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Salva a mensagem no Redis e define um tempo de expiração.
     * @param message Mensagem a ser salva no Redis.
     */
    @Override
    public void saveMessage(String message) {
        String messageKey = "messages"; // Chave onde a lista de mensagens será armazenada
        jedis.rpush(messageKey, message); // Adiciona a mensagem na lista "messages"
        jedis.expire(messageKey, MESSAGE_EXPIRATION_SECONDS); // Define o tempo de expiração para a lista de mensagens
        System.out.println("Mensagem salva no Redis com expiração de " + MESSAGE_EXPIRATION_SECONDS + " segundos.");
    }

    /**
     * Recupera uma mensagem a partir do índice.
     * @param id Índice da mensagem
     * @return Mensagem armazenada no índice especificado
     */
    @Override
    public String getMessage(String id) {
        return jedis.lindex("messages", Integer.parseInt(id)); // Recupera a mensagem pelo índice usando lindex
    }
}

