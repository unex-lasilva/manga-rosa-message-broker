git push origin primeiro-projeto-1/erica-araujo-de-jesuspackage br.com.mangarosa;

import br.com.mangarosa.consumers.FoodDeliveryConsumer;
import br.com.mangarosa.consumers.LongDistanceItemsConsumer;
import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.MessageBroker;
import br.com.mangarosa.messages.MessageExpire;
import br.com.mangarosa.producers.FastDeliveryProducer;
import br.com.mangarosa.producers.PyMarketPlaceProducer;
import br.com.mangarosa.topics.Topic;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        Topic topic = new Topic("fast-delivery-items");
        Topic topic2= new Topic("long-distance-items");

        FastDeliveryProducer producer = new FastDeliveryProducer();
        PyMarketPlaceProducer producer2 = new PyMarketPlaceProducer();

        producer.addTopic(topic);
        producer2.addTopic(topic2);

        producer.sendMessage("Entrega rapida");
        producer2.sendMessage("Entrega super rapida");

        Consumer consumer = new FoodDeliveryConsumer();
        Consumer consumer2 = new LongDistanceItemsConsumer();

        MessageBroker broker = new MessageBroker();

        broker.createTopic(topic);
        broker.createTopic(topic2);

        broker.subscribe(topic.name(), consumer);
        broker.subscribe(topic2.name(), consumer2);

        broker.notifyConsumers();

        producer.sendMessage("uma nova entrega");

        MessageExpire messageExpire = new MessageExpire(topic);
        MessageExpire messageExpire2 = new MessageExpire(topic2);

        try {
            Thread.sleep(300010); // Pausa a execução por 5 minutos
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        messageExpire.messageExpire();
        messageExpire2.messageExpire();
    }
}