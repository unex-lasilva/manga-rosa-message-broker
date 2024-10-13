package br.com.mangarosa;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.params.XAddParams;
import redis.clients.jedis.resps.StreamEntry;

public class TMessageRepository implements MessageRepository {

    private JedisPool pool;
    private final Gson gson = new Gson();

    TMessageRepository() {
        this.pool = new JedisPool("localhost", 6379);
    }

    @Override
    public void append(String topic, Message message) {
        try (Jedis jedis = pool.getResource()) {
            // Adiciona a mensagem no final do stream do tópico
            StreamEntryID streamID = jedis.xadd(topic, message.toMap(), XAddParams.xAddParams());
            message.setId(streamID.toString());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar uma mensagem no Redis", e);
        }
    }

    @Override
    public void consumeMessage(String topic, UUID messageId) {
        try (Jedis jedis = pool.getResource()) {
            String consumedKey = "consumed:" + topic;
            // Recupera as mensagens do stream
            List<StreamEntry> entries = jedis.xrange(topic, StreamEntryID.UNRECEIVED_ENTRY, StreamEntryID.LAST_ENTRY);
            if (entries.isEmpty()) {
                throw new RuntimeException("O tópico não existe ou está vazio.");
            }

            boolean found = false;
            for (StreamEntry entry : entries) {
                Message message = gson.fromJson(entry.getFields().get("message"), Message.class);
                if (message.getId().equals(messageId.toString())) {
                    // Marca como consumida no stream de consumidas
                    jedis.xadd(topic, entry.getFields(), XAddParams.xAddParams().maxLen(1000));
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new RuntimeException("Mensagem com ID " + messageId + " não encontrada no tópico " + topic);
            }
        }
    }

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        try (Jedis jedis = pool.getResource()) {
            // Recupera todas as mensagens não consumidas de um tópico específico
            List<StreamEntry> entries = jedis.xrange(topic, StreamEntryID.UNRECEIVED_ENTRY, StreamEntryID.LAST_ENTRY);

            if (entries.isEmpty()) {
                throw new RuntimeException("Tópico " + topic + " não existe.");
            }

            return entries.stream()
                    .map(entry -> gson.fromJson(entry.getFields().get("message"), Message.class))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        try (Jedis jedis = pool.getResource()) {
            String consumedKey = "consumed:" + topic;
            // Recupera todas as mensagens consumidas de um tópico específico
            List<StreamEntry> entries = jedis.xrange(topic, StreamEntryID.UNRECEIVED_ENTRY, StreamEntryID.LAST_ENTRY);

            if (entries.isEmpty()) {
                throw new RuntimeException("Tópico " + topic + " não existe ou não possui mensagens consumidas.");
            }

            return entries.stream()
                    .map(entry -> gson.fromJson(entry.getFields().get("message"), Message.class))
                    .collect(Collectors.toList());
        }
    }
}
