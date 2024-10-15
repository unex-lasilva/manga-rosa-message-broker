package br.com.mangarosa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.params.XReadParams;
import redis.clients.jedis.resps.StreamEntry;

public class TMessageRepository implements MessageRepository {

    private final JedisPool pool;

    TMessageRepository() {
        this.pool = new JedisPool("localhost", 6379);
    }

    /**
     * Adiciona uma nova mensagem no final da fila do tópico. Se o tópico não
     * existir, ele deve ser criado e a mensagem adicionada.
     *
     * @param topic nome do tópico que deve ser único
     * @param message mensagem - json
     */
    @Override
    public void append(String topic, Message message) {
        try (Jedis jedis = this.pool.getResource()) {
            System.out.println("-------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------");
            System.out.printf("Adicionando Topic: %s | Message: %s", topic, message.toMap());
            StreamEntryID res1 = jedis.xadd(topic, message.toMap(), XAddParams.xAddParams());
            message.setId(res1.toString());
            System.out.printf("Adicionando Mensagem: %s", res1);
            System.out.println("-------------------------------------------------------------");
            System.out.println("-------------------------------------------------------------");
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Grava uma mensagem como consumida. Se o tópico não existir ou a mensagem
     * com messageId também não existir no tópico, uma exceção deve ser lançada
     *
     * @param topic nome do tópico que deve existir
     * @param messageId código da mensagem
     */
    @Override
    public void consumeMessage(String topic, UUID messageId) {
        try (Jedis jedis = this.pool.getResource()) {

            Map<String, StreamEntryID> stream = new HashMap<>();
            System.out.println("MESSAGE ID:" + messageId.toString());
            stream.put(topic, new StreamEntryID(messageId.toString()));
            System.out.println("MESSAGE ID:" + messageId.toString());
            List<Map.Entry<String, List<StreamEntry>>> res18 = jedis.xread(XReadParams.xReadParams().count(1), stream);
            System.out.println(res18);

        }
    }

    public void consumeMessage(String topic, String messageId) {
        try (Jedis jedis = this.pool.getResource()) {
            //Coletando a stream
            List<StreamEntry> stream = jedis.xrange(topic, messageId, messageId, 1);

            //Pegando instancia da mensagem
            Message msg = mapToMessage(stream.get(0).getFields());
            msg.setConsumed(true);

            //Gravando ela no history
            StreamEntryID res1 = jedis.xadd(topic + "-history", msg.toMap(), XAddParams.xAddParams());
            msg.setId(res1.toString());

            //Apagando ela do topico
            jedis.xdel(topic, stream.get(0).getID());

        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retorna todas as mensagens ainda não consumidas e não expiradas que estão
     * num tópico. Se um tópico não existir, uma exceção deve ser lançada.
     *
     * @param topic nome do tópico
     * @return uma lista com todas as mensagens não consumidas
     */
    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        List<Message> messages = new ArrayList<>();
        try (Jedis jedis = this.pool.getResource()) {
            Map<String, StreamEntryID> stream = new HashMap<>();
            stream.put(topic, new StreamEntryID());

            List<Map.Entry<String, List<StreamEntry>>> mensagemStream = jedis.xread(
                    XReadParams.xReadParams().count(0), stream);

            if (mensagemStream == null) {
                throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
            }

            messages = mensagemStream.get(0).getValue().stream()
                    .map((msg -> mapToMessage(msg.getFields()))).toList();
        }
        return messages;
    }

    /**
     * Retorna todas as mensagens consumidas em um tópico e que ainda não foram
     * expiradas. Se um tópico não existir, uma exceção deve ser lançada.
     *
     * @param topic nome do tópico
     * @return uma lista com todoas as mensagens consumidas.
     */
    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        List<Message> messages = new ArrayList<>();
        try (Jedis jedis = this.pool.getResource()) {
            Map<String, StreamEntryID> stream = new HashMap<>();
            stream.put(topic + "-history", new StreamEntryID());

            List<Map.Entry<String, List<StreamEntry>>> mensagemStream = jedis.xread(
                    XReadParams.xReadParams().count(0), stream);

            if (mensagemStream == null) {
                throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
            }

            messages = mensagemStream.get(0).getValue().stream()
                    .map((msg -> mapToMessage(msg.getFields()))).toList();
        }
        return messages;
    }

    private Message mapToMessage(Map<String, String> value) {
        String producer = value.get("producer");
        producer = producer.substring(producer.indexOf("\"name\":") + 7, producer.length() - 1);

        Message msg = new Message(new TProducer(producer), value.get("message"));
        return msg;
    }

}
