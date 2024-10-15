package br.com.mangarosa.messages;

import br.com.mangarosa.connection.RedisConnection;
import br.com.mangarosa.consumers.FoodDeliveryConsumer;
import br.com.mangarosa.interfaces.IMessageRepository;
import io.lettuce.core.Consumer;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MessageRepository implements IMessageRepository {

    private final RedisCommands<String, String> syncCommands = RedisConnection.connect();

    @Override
    public void append(String topic, Message message) {

        String json = message.toJson();

        Map<String, String> map = new HashMap<>();
        // Cria um mapa para armazenar o JSON da mensagem com a chave "messageJson".
        map.put("messageJson", json);

        String idMessage = syncCommands.xadd(topic, map);
        // Adiciona a mensagem no stream do Redis associado ao tópico.
        message.setId(idMessage);

        System.out.println("Mensagem Enviada para o topico " + topic + " com ID: " + idMessage);
    }

    @Override
    public void consumeMessage(String topic, Message myMessage) {

        String messageId = myMessage.getId();
        // Obtém o ID da mensagem que será consumida.
        List<StreamMessage<String, String>> messages = syncCommands.xread(
        // Lê todas as mensagens no stream do Redis associadas ao tópico a partir do ID "0".
                XReadArgs.StreamOffset.from(topic, "0")
        );

        System.out.println("Consume Message List: " + messages.isEmpty());
        System.out.println(messages.size());

        for(int i = 0; i < messages.size(); i++) {
            StreamMessage<String, String> message = messages.get(i);

            String jsonMessage = message.getBody().get("messageJson");
            Message messageObj = Message.fromJson(jsonMessage);

            for(String m : myMessage.getConsumptionList()) {
            // Adiciona os consumidores da mensagem recebida na lista de consumidores da mensagem original.
                messageObj.getConsumptionList().add(m);
            }

            if(message.getId().equals(messageId)) {
                messageObj.setConsumed(true);
                syncCommands.xack(topic, "application_1", message.getId());
                syncCommands.xdel(topic, message.getId());
                syncCommands.xadd(topic, Map.of("messageJson", messageObj.toJson()));
            }

        }
    }

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
    // Método que retorna todas as mensagens que ainda não foram consumidas de um tópico.

        List<Message> result = new ArrayList<>();

        List<StreamMessage<String, String>> messages = syncCommands.xread(
                XReadArgs.StreamOffset.from(topic, "0")
        );
        System.out.println(messages.isEmpty());

        for(StreamMessage<String, String> message : messages) {
            System.out.println(message.getBody());
            String jsonMessage = message.getBody().get("messageJson");
            Message messageObj = Message.fromJson(jsonMessage);

            if(!messageObj.isConsumed()) {
                messageObj.setId(message.getId());
                result.add(messageObj);
            }
        }

        return result;
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        return List.of();
    }
}
