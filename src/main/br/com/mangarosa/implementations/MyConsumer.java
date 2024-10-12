package main.br.com.mangarosa.implementations;

import main.br.com.mangarosa.interfaces.Consumer;
import main.br.com.mangarosa.messages.Message;
import redis.clients.jedis.JedisPooled;
import org.slf4j.Logger; // resolvi usar o Logback para ter um monitoramento mais detalhado da execução do sistema
import org.slf4j.LoggerFactory;

public class MyConsumer implements Consumer
{
    private final JedisPooled jedis = JedisRedis.getJedisPool();
    private final String name;
    private static final Logger logger = LoggerFactory.getLogger(MyConsumer.class);

    public MyConsumer(String name) {
        this.name = name;
    }

    @Override
    public boolean consume(Message message) {
        jedis.get(message.getId());
        message.setConsumed(true);
        message.addConsumption(this);
        logger.info("Consumindo mensagem: {}", message);
        jedis.setex(message.getId(), 20, String.valueOf(message));
        return true;
    }

    @Override
    public String name() {
        return name;
    }
}
