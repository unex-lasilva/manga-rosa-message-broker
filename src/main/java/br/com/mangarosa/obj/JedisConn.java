package br.com.mangarosa.obj;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

public class JedisConn
{
    private static final JedisPooled jedisPool = new JedisPooled("localhost", 6379);;

    public static JedisPooled getJedisPool() {
        return jedisPool;
    }
}
