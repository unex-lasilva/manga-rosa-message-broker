package br.com.mangarosa.config;

import redis.clients.jedis.Jedis;

public class RedisConfig {

    // Configurações do Redis
    private static final String REDIS_HOST = "localhost"; // Endereço do servidor Redis
    private static final int REDIS_PORT = 6379;           // Porta do servidor Redis

    // Método que retorna uma conexão Jedis
    public static Jedis getConnection() {
        return new Jedis(REDIS_HOST, REDIS_PORT);
    }
}

