package br.com.mangarosa;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.messages.MessageBroker;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        //Criando Repo
        TMessageRepository messageRepository = new TMessageRepository();
        MessageBroker messageBroker = new MessageBroker(messageRepository);

        Producer fastDeliveryProducer = new TProducer("FastDelivery");
        Producer pyMarketPlaceProducer = new TProducer("PyMarketPlace");

        Producer foodDeliverProducer = new TProducer("FoodDeliver");
        Producer physicPersonDeliveryProducer = new TProducer("PhysicPersonDelivery");

        TTopic topic1 = new TTopic("queue/long-distance-items", messageRepository);
        TTopic topic2 = new TTopic("queue/fast-delivery-items", messageRepository);

        Consumer longDistanceConsumer = new TConsumer("LongDistance");
        Consumer fastDeliveryConsumer = new TConsumer("FastDelivery");

        messageBroker.createTopic(topic1);
        messageBroker.createTopic(topic2);

        messageBroker.subscribe(topic1.name(), longDistanceConsumer);
        messageBroker.subscribe(topic2.name(), fastDeliveryConsumer);

        fastDeliveryProducer.addTopic(topic1);
        pyMarketPlaceProducer.addTopic(topic1);

        foodDeliverProducer.addTopic(topic2);
        physicPersonDeliveryProducer.addTopic(topic2);

        fastDeliveryProducer.sendMessage("Chapeuzinho Vermelho");
        pyMarketPlaceProducer.sendMessage("Pica Pau");

        topic1.mensagens().forEach(mensagem -> {
            System.out.println("Consumindo mensagem: " + mensagem.getId());
            System.out.println("Consumindo mensagem: " + mensagem.getMessage());
            //System.out.println("UUID: " + UUID.nameUUIDFromBytes(mensagem.getId().getBytes()));
            messageRepository.consumeMessage(topic1.name(), mensagem.getId());
            mensagem.setConsumed(true);

            System.out.println("--------------------------- FIM ---------------------------");
        });

        System.out.println(messageRepository.getAllConsumedMessagesByTopic(topic1.name()));

    }
}
