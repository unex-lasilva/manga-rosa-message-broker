package br.com.mangarosa;

import redis.clients.jedis.Jedis;

public class RedisConfig {
    // Gerencia a conexão com o Redis
    private static Jedis jedis;

    static {
        // Inicializa a conexão com o Redis
        jedis = new Jedis("localhost"); // Conecta ao Redis em localhost.
    }

    // Método estático para retornar a instância Jedis
    public static Jedis getInstance() {
        return jedis; // Retorna a instância única de Jedis
    }
}
