package br.com.mangarosa;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.obj.*;
import br.com.mangarosa.producer.FastDeliveryProducer;
import br.com.mangarosa.producer.FoodDeliveryProducer;
import br.com.mangarosa.producer.PhysicPersonDeliveryProducer;
import br.com.mangarosa.producer.PyMarketPlaceProducer;
import redis.clients.jedis.JedisPooled;

import java.util.*;

import static br.com.mangarosa.obj.JedisConn.getJedisPool;

public class Main {
    public static void main(String[] args) {
        JedisPooled jedis = getJedisPool();
        Producer foodDeliveryProducer = new FoodDeliveryProducer("foodDeliveryProducer");
        Producer physicPersonDeliveryProducer = new PhysicPersonDeliveryProducer("physicPersonDeliveryProducer");
        Producer pyMarketPlaceProducer = new PyMarketPlaceProducer("pyMarketPlaceProducer");
        Producer fastDeliveryProducer = new FastDeliveryProducer("fastDeliveryProducer");
        Topico fastDelivery = new Topico("queue/fast-delivery-items");
        Topico longDistanceDelivery = new Topico("queue/long-distance-items");
        Consumer fastConsumer = new Consumidor("fastConsumer");
        Consumer slowConsumer = new Consumidor("slowConsumer");
        fastDelivery.subscribe(fastConsumer);
        longDistanceDelivery.subscribe(slowConsumer);
        MessageRepository messageRepository = new MyRepository(new HashMap<>() {
            {
                put(fastDelivery.name(), fastDelivery);
                put(longDistanceDelivery.name(), longDistanceDelivery);
            }
        });
        foodDeliveryProducer.generateMessage("Gerado pedido de entrega de comida", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300);
//                jedis.setex(k, 10, v.toString());
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
            } catch (Exception ex) {
                System.out.println("Mensagem já gravada no banco de dados");
            }
        });
        physicPersonDeliveryProducer.generateMessage("Gerado pedido de entrega física de comida", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300);
//                jedis.setex(k, 10, v.toString());
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
            } catch (Exception ex) {
                System.out.println("Mensagem já gravada no banco de dados");
            }
        });
        pyMarketPlaceProducer.generateMessage("Gerado pedido", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300);
//                jedis.setex(k, 10, v.toString());
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
            } catch (Exception ex) {
                System.out.println("Mensagem já gravada no banco de dados");
            }
        });
        fastDeliveryProducer.generateMessage("Gerado pedido de entrega rápida", 1).forEach((k, v) -> {
            messageRepository.append(fastDelivery.name(), v);
            messageRepository.append(longDistanceDelivery.name(), v);
            try {
                jedis.set(k, v.toString());
                jedis.expire(k, 300);
//                jedis.setex(k, 10, v.toString());
                fastDelivery.notifyConsumers(v);
                longDistanceDelivery.notifyConsumers(v);
            } catch (Exception ex) {
                System.out.println("Mensagem já gravada no banco de dados");
            }
        });
    }
}

// Feito e disponibilizado por Jefferson Caio Mascarenhas