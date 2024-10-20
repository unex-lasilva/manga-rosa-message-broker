package br.com.mangarosa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.resps.StreamEntry;


// Base feita com Matheus Kaick em Call juntos, mas houve uma alteração pois o dele está como List já o meu se comporta como Stream 

public class RedisMessageRepository implements MessageRepository {

    private final JedisPool jedisPool;
    private static final int MESSAGE_EXPIRATION_SECONDS = 300; // 5 minutos
    private final Map<String, JedisPubSub> subscriptions;

    /**
     * Construtor que inicializa a conexão com o Redis e as inscrições.
     * @param host Endereço do servidor Redis
     * @param port Porta do servidor Redis
     */
    public RedisMessageRepository(String host, int port) {
        this.jedisPool = new JedisPool(host, port);
        this.subscriptions = new HashMap<>();
    }

    /**
     * Inscreve um consumidor em um tópico.
     * @param topic Nome do tópico
     * @param consumer Consumidor a ser inscrito
     */
    public void subscribe(String topic, Consumer consumer) {
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                Message msg = Message.fromString(message);
                consumer.consume(msg);
            }
        }; 

        subscriptions.put(topic + ":" + consumer.name(), jedisPubSub);

        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.subscribe(jedisPubSub, topic);
            }
        }).start();
    }

    /**
     * Cancela a inscrição de um consumidor em um tópico.
     * @param topic Nome do tópico
     * @param consumer Consumidor a ter a inscrição cancelada
     */
    public void unsubscribe(String topic, Consumer consumer) {
        String key = topic + ":" + consumer.name();
        JedisPubSub jedisPubSub = subscriptions.get(key);
        if (jedisPubSub != null) {
            jedisPubSub.unsubscribe();
            subscriptions.remove(key);
        }
    }

    /**
     * Adiciona uma nova mensagem ao final da fila do tópico.
     * @param topic Nome do tópico
     * @param message Mensagem a ser adicionada
     */
    @Override
    public void append(String topic, Message message) {
        try (Jedis jedis = jedisPool.getResource()) {
            String messageId = UUID.randomUUID().toString();
            message.setId(messageId);

            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("message", message.toString());

            jedis.xadd(topic, StreamEntryID.NEW_ENTRY, messageMap);
            System.out.println("Mensagem adicionada ao tópico: " + topic + ", ID: " + messageId);
        }
    }


    /**
     * Marca uma mensagem como consumida.
     * @param topic Nome do tópico
     * @param messageId ID da mensagem a ser marcada como consumida
     */
    @Override
    public void consumeMessage(String topic, UUID messageId) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<StreamEntry> entries = jedis.xrange(topic, "-", "+");
            for (StreamEntry entry : entries) {
                if (entry.getFields().get("message").contains(messageId.toString())) {
                    jedis.xdel(topic, entry.getID());
                    jedis.xadd(topic + ":consumed", StreamEntryID.NEW_ENTRY, entry.getFields());
                    System.out.println("Mensagem consumida do tópico: " + topic);
                    return;
                }
            }
            throw new IllegalArgumentException("Mensagem não encontrada no tópico: " + topic);
        }
    }

    /**
     * Retorna todas as mensagens não consumidas de um tópico.
     * @param topic Nome do tópico
     * @return Lista de mensagens não consumidas
     */
    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<StreamEntry> entries = jedis.xrange(topic, "-", "+");
            return entries.stream()
                    .map(entry -> Message.fromString(entry.getFields().get("message")))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retorna todas as mensagens consumidas de um tópico.
     * @param topic Nome do tópico
     * @return Lista de mensagens consumidas
     */
    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        try (Jedis jedis = jedisPool.getResource()) {
            List<StreamEntry> entries = jedis.xrange(topic + ":consumed", "-", "+");
            return entries.stream()
                    .map(entry -> Message.fromString(entry.getFields().get("message")))
                    .collect(Collectors.toList());
        }
    }

    /**
     * Remove mensagens expiradas de um tópico.
     * @param topic Nome do tópico
     */
    public void removeExpiredMessages(String topic) {
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = System.currentTimeMillis();
            long cutoffTime = currentTime - MESSAGE_EXPIRATION_SECONDS * 1000;
            
            // Obter todas as mensagens do stream
            List<StreamEntry> entries = jedis.xrange(topic, "-", "+");
            
            // Identificar e remover mensagens expiradas
            for (StreamEntry entry : entries) {
                StreamEntryID id = entry.getID();
                long messageTimestamp = id.getTime();
                if (messageTimestamp < cutoffTime) {
                    jedis.xdel(topic, id);
                } else {
                    // As mensagens estão ordenadas por tempo, então podemos parar aqui
                    break;
                }
            }
        }
    }

    /**
     * Agenda a limpeza periódica de mensagens expiradas.
     * @param Queue Nome do tópico a ser limpo
     */
    public void Time(String Queue){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            this.removeExpiredMessages(Queue);
        }, 5, 5, TimeUnit.MINUTES);
    }

}