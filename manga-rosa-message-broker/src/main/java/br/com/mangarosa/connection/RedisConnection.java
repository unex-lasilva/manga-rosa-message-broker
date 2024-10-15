package br.com.mangarosa.connection;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisConnection {

    private static final String REDIS_URI = "redis://localhost:6379";

    public static RedisCommands<String, String> connect() {
        RedisClient redisClient = RedisClient.create(REDIS_URI);
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        return syncCommands;
    }

    public static void close(StatefulRedisConnection<String, String> connection) {
        connection.close();
    }
}
