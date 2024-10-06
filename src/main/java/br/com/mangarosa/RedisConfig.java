package br.com.mangarosa;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisConfig {
    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;

    static {
        redisClient = RedisClient.create("redis://localhost:6379");
        connection = redisClient.connect();
    }

    public static RedisCommands<String, String> getSyncCommands() {
        return connection.sync();
    }

    public static void shutdown() {
        connection.close();
        redisClient.shutdown();
    }
}
