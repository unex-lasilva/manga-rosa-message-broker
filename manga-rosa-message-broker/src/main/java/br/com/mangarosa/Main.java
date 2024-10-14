package br.com.mangarosa;

import java.util.List;
import java.util.UUID;

import br.com.mangarosa.consumer.BaseConsumer;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.producer.BaseProducer;
import br.com.mangarosa.services.MessageRepositoryImpl;
import br.com.mangarosa.services.TopicImpl;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        // Criando cliente Redis
        RedisClient redisClient = RedisClient.create("redis://localhost:6379");
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        // Criando o repositório e o tópico
        MessageRepositoryImpl messageRepository = new MessageRepositoryImpl(redisClient, connection);
        TopicImpl topicFast = new TopicImpl("queue/fast-delivery-items", messageRepository);
        TopicImpl topicLong = new TopicImpl("queue/long-distance-items", messageRepository);

        // Criando o produtor e associando o tópico
        BaseProducer fastDeliveryProducer = new BaseProducer(redisClient, "FastDelivery");
        fastDeliveryProducer.addTopic(topicLong);

        BaseProducer pyMarketPlaceProducer = new BaseProducer(redisClient, "PyMarketPlace");
        pyMarketPlaceProducer.addTopic(topicLong);

        BaseProducer physicPersonDeliveryProducer = new BaseProducer(redisClient, "PhysicPersonDelivery");
        physicPersonDeliveryProducer.addTopic(topicFast);

        BaseProducer foodDeliveryProducer = new BaseProducer(redisClient, "FoodDelivery");
        foodDeliveryProducer.addTopic(topicFast);

        // Criando os consumidores e inscrevendo nos tópicos
        BaseConsumer fastConsumer = new BaseConsumer("FastConsumer");
        topicFast.subscribe(fastConsumer);

        BaseConsumer longConsumer = new BaseConsumer("LongConsumer");
        topicLong.subscribe(longConsumer);

        // Enviando uma nova mensagem pelo produtor
        String messageContent = "Nova mensagem de entrega rápida";
        fastDeliveryProducer.sendMessage(messageContent);

        String messageContent2 = "Nova mensagem de entrega longa";
        pyMarketPlaceProducer.sendMessage(messageContent2);

        String messageContent3 = "Nova mensagem de entrega raíz";
        physicPersonDeliveryProducer.sendMessage(messageContent3);

        String messageContent4 = "Nova mensagem de entrega de alimentos";
        foodDeliveryProducer.sendMessage(messageContent4);

        // Teste: Obtendo mensagens não consumidas de um tópico
        List<Message> notConsumedMessages = messageRepository
                .getAllNotConsumedMessagesByTopic("queue/fast-delivery-items");
        System.out.println("Mensagens não consumidas:");
        for (Message msg : notConsumedMessages) {
            System.out.println(
                    "ID: " + msg.getId() + ", Mensagem: " + msg.getMessage() + ", Consumido: " + msg.isConsumed());
        }

        // Teste: Obtendo mensagens consumidas de um tópico
        List<Message> consumedMessages = messageRepository.getAllConsumedMessagesByTopic("queue/fast-delivery-items");
        System.out.println("Mensagens consumidas:");
        for (Message msg : consumedMessages) {
            System.out.println(
                    "ID: " + msg.getId() + ", Mensagem: " + msg.getMessage() + ", Consumido: " + msg.isConsumed());
        }

        // Fechando a conexão Redis
        connection.close();
        redisClient.shutdown();
    }
}
