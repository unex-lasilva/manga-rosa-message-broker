package main.br.com.mangarosa.implementations;

import redis.clients.jedis.JedisPooled;

public class JedisRedis
{
    private static final JedisPooled jedisPool = new JedisPooled("localhost", 6379);;

    public static JedisPooled getJedisPool() {
        return jedisPool;
    }
}
