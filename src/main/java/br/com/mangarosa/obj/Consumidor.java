package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;
import redis.clients.jedis.JedisPooled;
import br.com.mangarosa.obj.JedisConn;

public class Consumidor implements Consumer
{
    private final JedisPooled jedis = JedisConn.getJedisPool();
    private final String name;

    public Consumidor(String name) {
        this.name = name;
    }

    @Override
    public boolean consume(Message message) {
        jedis.get(message.getId());
        message.setConsumed(true);
        message.addConsumption(this);
//        System.out.println(message);
        jedis.setex(message.getId(), 20, String.valueOf(message));
//        jedis.(message.getId(), 20);
        return true;
    }

    @Override
    public String name() {
        return name;
    }
}
