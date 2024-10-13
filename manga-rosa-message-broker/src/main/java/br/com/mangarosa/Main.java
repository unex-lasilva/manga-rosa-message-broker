package br.com.mangarosa;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.MessageBroker;
import br.com.mangarosa.interfaces.Consumer;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        // criando o message broker
        MessageRepository repository = new TMessageRepository();
        MessageBroker messageBroker = new MessageBroker(repository);
        // criando os producers
        Producer fastDeliveryProducer = new TProducer("FastDelivery");
        Producer pyMarketPlaceProducer = new TProducer("PyMarketPlace");
        Producer foodDeliverProducer = new TProducer("FoodDeliver");
        Producer physicPersonDeliveryProducer = new TProducer("PhysicPersonDelivery");
        // criando os tópicos
        Topic longDistance = new TTopic("queue/long-distance-items", repository);
        Topic fastDelivery = new TTopic("queue/fast-delivery-items", repository);
        // criando os consumidores
        Consumer longDistanceConsumer = new TConsumer("LongDistance");
        Consumer fastDeliveryConsumer = new TConsumer("FastDelivery");
        // associando os consumidores aos tópicos
        messageBroker.createTopic(longDistance);
        messageBroker.createTopic(fastDelivery);
        // associando os tópicos aos producers
        messageBroker.subscribe(longDistance.name(), longDistanceConsumer);
        messageBroker.subscribe(fastDelivery.name(), fastDeliveryConsumer);
        // associando os producers aos tópicos
        fastDeliveryProducer.addTopic(fastDelivery);
        physicPersonDeliveryProducer.addTopic(fastDelivery);
        // associando os tópicos aos producers
        pyMarketPlaceProducer.addTopic(longDistance);
        foodDeliverProducer.addTopic(longDistance);
        // enviando as mensagens
        fastDeliveryProducer.sendMessage("Este é um item de entrega rápida");
        pyMarketPlaceProducer.sendMessage("Este é um item do pyMarketPlace");
        foodDeliverProducer.sendMessage("Este é um item de entrega de alimentos");
        physicPersonDeliveryProducer.sendMessage("Este é um item de entrega para pessoa física");
        
        
    }
}