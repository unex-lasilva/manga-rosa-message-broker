package br.com.mangarosa;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageBroker;
import br.com.mangarosa.my_impl.redis.interfaces.RedisConsumer;
import br.com.mangarosa.my_impl.redis.interfaces.RedisProducer;
import br.com.mangarosa.my_impl.redis.interfaces.RedisRepository;
import br.com.mangarosa.my_impl.redis.interfaces.RedisTopic;


public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        // Inicialização do programa
        final String separador = String.join("", Collections.nCopies(100, "-"));
        final String cabecalho = "\t\t\tDEMONSTRAÇÃO DE FUNCIONAMENTO DO MESSAGE BROKER";
        System.out.println(separador);
        System.out.println(cabecalho);
        System.out.println(separador);

        // Inicialização do repositório Redis
        // (assim que é iniciado, ele começa a monitorar periodicamente por mensagens expiradas, removendo-as)
        final RedisRepository repository = new RedisRepository();

        // Inicialização do MessageBroker
        final MessageBroker messageBroker = new MessageBroker(repository);

        // Inicialização dos novos tópicos implementados
        final RedisTopic fastTopic = new RedisTopic("FastDeliveryItemsTopic"); //especialização do tópico Redis genérico
        final RedisTopic longTopic = new RedisTopic("LongDistanceItemsTopic"); //especialização do tópico Redis genérico

        // Inicialização dos novos producers implementados
        final RedisProducer foodProducer = new RedisProducer("FoodDeliveryProducer"); //especialização do producer genérico, para o tópico fast-delivey-items
        final RedisProducer physicProducer = new RedisProducer("PhysicPersonDeliveryProducer"); //especialização do producer genérico, para o tópico fast-delivey-items
        final RedisProducer fastProducer = new RedisProducer("FastDeliveryProducer"); //especialização do producer genérico, para o tópico long-distance-items
        final RedisProducer marketProducer = new RedisProducer("PyMarketPlaceProducer"); //especialização do producer genérico, para o tópico long-distance-items

        // Inicialização dos novos consumers implementados
        final RedisConsumer fastConsumer = new RedisConsumer("FastDeliveryItemsConsumer"); //especialização do consumer genérico, para o tópico fast-delivery-items
        final RedisConsumer longConsumer = new RedisConsumer("LongDistanceItemsConsumer"); //especialização do consumer genérico, para o tópico long-distance-items

        // Linkagem do tópico "fast-delivery-items" aos Producers "FoodDeliveryProducer" e "PhysicPersonDelivery"
        foodProducer.addTopic(fastTopic);
        physicProducer.addTopic(fastTopic);

        // Linkagem do tópico "long-distance-items" aos Producers "PyMarketPlaceProducer" e "FastDeliveryProducer"
        fastProducer.addTopic(longTopic);
        marketProducer.addTopic(longTopic);

        // Inscrição do Consumidor no tópico "fast-delivery-items"
        fastTopic.subscribe(fastConsumer);

        // Inscrição do Consumidor no tópico "long-distance-items"
        longTopic.subscribe(longConsumer);

        // Produção de 10 Mensagens de forma aleatória
        for (int i = 1; i <= 10; i++) {
            RedisProducer[] producers = {foodProducer, physicProducer, fastProducer, marketProducer};
            RedisTopic[] topics = {fastTopic, longTopic};
            int producerIndex = new Random().nextInt(producers.length);
            int topicIndex = new Random().nextInt(topics.length);
            RedisProducer producer = producers[producerIndex];
            RedisTopic topic = topics[topicIndex];
            String randString = UUID.randomUUID().toString().replaceAll("-", "");
            Message message = new Message(producer, randString);
            topic.addMessage(message);
        }

        System.out.println("- Mensagens publicadas de forma aleatória no repositório Redis!");
        System.out.println("- A mensagens expiradas serão automaticamente removidas em cada interação com o repositório!");



   }
}