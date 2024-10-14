package br.com.mangarosa.services;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.producer.BaseProducer;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageRepositoryImpl implements MessageRepository {
    private final RedisClient redisClient;
    private final RedisCommands<String, String> redisCommands;

    public MessageRepositoryImpl(RedisClient redisClient, StatefulRedisConnection<String, String> connection) {
        this.redisClient = redisClient;
        this.redisCommands = connection.sync();
    }

    @Override
    public void append(String topic, Message message) {
        try {
            // Adiciona mensagem ao stream
            Map<String, String> messageMap = message.toMap();
            String streamId = redisCommands.xadd(topic, messageMap);

            // Definindo o ID da mensagem
            message.setId(streamId);
            System.out.println("Mensagem adicionada ao stream com ID: " + streamId);

            // Definindo a expiração da mensagem em 5 minutos (300 segundos)
            long expirationTimeInSeconds = 300;
            redisCommands.expire(topic, expirationTimeInSeconds); 
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar a mensagem ao Redis Stream", e);
        }
    }
    

    @Override
    public void consumeMessage(String topic, String messageId) {
        try {
            // Lê todas as mensagens do stream
            List<StreamMessage<String, String>> messages = redisCommands.xrange(topic, Range.create("-", "+"));

            // Itera sobre as mensagens
            for (StreamMessage<String, String> messageData : messages) {
                Message message = mapToMessage(messageData); // Mapeia para objeto Message

                if (message.getId().equals(messageId.toString())) { // Verifica ID da mensagem
                    if (message.isConsumed()) { // Checa se já foi consumida
                        throw new RuntimeException("Mensagem já foi consumida."); // Lança exceção se sim
                    }
                    message.setConsumed(true); // Marca como consumida
                    redisCommands.xadd(topic, message.toMap());  // Adiciona mensagem atualizada ao stream
                    redisCommands.xdel(topic, messageId); // Remove mensagem original do stream
                    return; 
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consumir a mensagem", e); 
        }
    }

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        // Lê todas as mensagens do stream
        List<StreamMessage<String, String>> messages = redisCommands.xrange(topic, Range.create("-", "+"));
        List<Message> notConsumedMessages = new ArrayList<>();
        // Itera sobre as mensagens
        for (StreamMessage<String, String> messageData : messages) {
            Message message = mapToMessage(messageData);
            // Checa se a mensagem ainda não foi consumida
            if (!message.isConsumed()) {
                notConsumedMessages.add(message);
            }
        }

        return notConsumedMessages;
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        // Lê todas as mensagens do stream
        List<StreamMessage<String, String>> messages = redisCommands.xrange(topic, Range.create("-", "+"));
        List<Message> consumedMessages = new ArrayList<>();
        // Itera sobre as mensagens
        for (StreamMessage<String, String> messageData : messages) {
            Message message = mapToMessage(messageData);
            // Checa se a mensagem foi consumida
            if (message.isConsumed()) {
                consumedMessages.add(message);
            }
        }

        return consumedMessages;
    }

    private Message mapToMessage(StreamMessage<String, String> messageData) {
        // Mapeia para objeto Message
        String id = (String) messageData.getId();
        String producerData = messageData.getBody().get("producer");
        String messageText = messageData.getBody().get("message");
        Producer producer = new BaseProducer(redisClient, producerData);
        // Cria objeto Message
        Message message = new Message(producer, messageText);
        message.setId(id);
        message.setConsumed(Boolean.parseBoolean((String) messageData.getBody().get("isConsumed")));
        // Retorna objeto Message
        return message;
    }

}
