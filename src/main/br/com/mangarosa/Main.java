package main.br.com.mangarosa;

import main.br.com.mangarosa.interfaces.Consumer;
import main.br.com.mangarosa.interfaces.MessageRepository;
import main.br.com.mangarosa.interfaces.Producer;
import main.br.com.mangarosa.implementations.*;
import main.br.com.mangarosa.producer.*;
import org.slf4j.Logger; // resolvi usar o Logback ao invés do System.out.println para ter um monitoramento mais detalhado da execução do sistema
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;

import java.util.*;

import static main.br.com.mangarosa.implementations.JedisRedis.getJedisPool;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        JedisPooled jedis = getJedisPool();
        Producer foodDeliveryProducer = new FoodDeliveryProducer("foodDeliveryProducer");
        Producer physicPersonDeliveryProducer = new PhysicPersonDeliveryProducer("physicPersonDeliveryProducer");
        Producer pyMarketPlaceProducer = new PyMarketPlaceProducer("pyMarketPlaceProducer");
        Producer fastDeliveryProducer = new FastDeliveryProducer("fastDeliveryProducer");
        Topico fastDelivery = new Topico("queue/fast-delivery-items"); // implementação do tópico dos produtores: FoodDeliveryProducer e PhysicPersonDeliveryProducer
        Topico longDistanceDelivery = new Topico("queue/long-distance-items"); // implementação do tópico dos produtores: PyMarketPlaceProducer e FastDeliveryProducer
        Consumer fastConsumer = new MyConsumer("fastConsumer"); // Consumidor do tópico queue/fast-delivery-items
        Consumer slowConsumer = new MyConsumer("slowConsumer"); // Consumidor do tópico queue/long-distance-items
        fastDelivery.subscribe(fastConsumer);
        longDistanceDelivery.subscribe(slowConsumer);
        MessageRepository messageRepository = new MyRepository(new HashMap<>() {
            {
                put(fastDelivery.name(), fastDelivery);
                put(longDistanceDelivery.name(), longDistanceDelivery);
            }
        });
        foodDeliveryProducer.generateMessage("Pedido de foodDelivery criado com sucesso.", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300); // expirando em 5 minutos
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
                logger.info("Mensagem de foodDelivery enviada e armazenada no Redis com ID: {}", k);
            } catch (Exception ex) {
                logger.error("Erro ao armazenar mensagem no Redis", ex);
            }
        });
        physicPersonDeliveryProducer.generateMessage("Pedido de PhysicPersonDelivery criado com sucesso.", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300); // expirando em 5 minutos
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
                logger.info("Mensagem de PhysicPersonDelivery enviada e armazenada no Redis com ID: {}", k);
            } catch (Exception ex) {
                logger.error("Erro ao armazenar mensagem no Redis", ex);
            }
        });
        pyMarketPlaceProducer.generateMessage("Pedido de MarketPlace criado.", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300); // expirando em 5 minutos
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
                logger.info("Mensagem de pyMarketPlaceProducer enviada e armazenada no Redis com ID: {}", k);
            } catch (Exception ex) {
                logger.error("Erro ao armazenar mensagem no Redis", ex);
            }
        });
        fastDeliveryProducer.generateMessage("Pedido de fastDelivery criado.", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300); // expirando em 5 minutos
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
                logger.info("Mensagem de fastDeliveryProducer enviada e armazenada no Redis com ID: {}", k);
            } catch (Exception ex) {
                logger.error("Erro ao armazenar mensagem no Redis", ex);
            }
        });
    }
}