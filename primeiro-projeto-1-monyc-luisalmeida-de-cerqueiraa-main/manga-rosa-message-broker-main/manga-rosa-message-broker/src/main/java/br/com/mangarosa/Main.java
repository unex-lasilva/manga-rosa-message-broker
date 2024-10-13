package br.com.mangarosa;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.queue.fastdeliveryitems.FoodDeliveryProducer;
import br.com.mangarosa.queue.fastdeliveryitems.FastDeliveryConsumer;

import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        MessageRepository messageRepository = new YourMessageRepositoryImplementation();
        String topicName = "queue/fast-delivery-items";

        // Cria  o produtor e produz mensagens
        FoodDeliveryProducer producer = new FoodDeliveryProducer(messageRepository, topicName);
        producer.produce("Pedido Espetin: 10 espetinhos de carne");
        producer.produce("Pedido Espetin: 5 espetinhos de frango");

        // Cria o consumidor e consome mensagens
        FastDeliveryConsumer consumer = new FastDeliveryConsumer(messageRepository);
        consumer.consume(topicName); // Aqui é onde consome as mensagens
    }

    private static class YourMessageRepositoryImplementation implements MessageRepository {
        @Override
        public void append(String topic, Message message) {

        }

        @Override
        public void consumeMessage(String topic, UUID messageId) {

        }

        @Override
        public List<Message> getAllNotConsumedMessagesByTopic(String topic) throws IllegalArgumentException {
            return List.of();
        }

        @Override
        public List<Message> getAllConsumedMessagesByTopic(String topic) {
            return List.of();
        }

        @Override
        public void append(Message message) {

        }

        @Override
        public void save(Message message) {

        }

        @Override
        public Message find(String id) {
            return null;
        }

        @Override
        public Message find(UUID id) {
            return null;
        }

        @Override
        public void addMessageToTopic(String name, Message message) {

        }

        @Override
        public Message getMessageFromTopic(String topicName) {
            return null;
        }
    }
}
