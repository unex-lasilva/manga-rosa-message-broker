package br.com.mangarosa;

import br.com.mangarosa.consumers.FastDeliveryConsumer;
import br.com.mangarosa.consumers.LongDistanceConsumer;
import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.producers.FastDeliveryProducer;
import br.com.mangarosa.producers.FoodDeliveryProducer;
import br.com.mangarosa.producers.PhysicPersonDeliveryProducer;
import br.com.mangarosa.producers.PyMarketPlaceProducer;
import br.com.mangarosa.topics.FastDeliveryItemsTopic;
import br.com.mangarosa.topics.LongDistanceItemsTopic;

public class Main {
    public static void main(String[] args) {
        // Tópicos
        FastDeliveryItemsTopic fastDeliveryTopic = new FastDeliveryItemsTopic();
        LongDistanceItemsTopic longDistanceTopic = new LongDistanceItemsTopic();

        // Produtores para fast-delivery-items
        Producer foodProducer = new FoodDeliveryProducer(fastDeliveryTopic);
        Producer physicPersonProducer = new PhysicPersonDeliveryProducer(fastDeliveryTopic);

        // Produtores para long-distance-items
        Producer pyMarketProducer = new PyMarketPlaceProducer(longDistanceTopic);
        Producer fastDeliveryProducer = new FastDeliveryProducer(longDistanceTopic);

        // Enviando mensagens
        foodProducer.sendMessage("Pedido de comida rápida");
        physicPersonProducer.sendMessage("Entrega de pessoa física");

        pyMarketProducer.sendMessage("Item de marketplace para longa distância");
        fastDeliveryProducer.sendMessage("Item de entrega rápida para longa distância");

        // Consumidores
        Consumer fastConsumer = new FastDeliveryConsumer();
        Consumer longConsumer = new LongDistanceConsumer();

        fastConsumer.consume("Pedido de comida rápida consumido");
        longConsumer.consume("Item de marketplace consumido");
    }
}
