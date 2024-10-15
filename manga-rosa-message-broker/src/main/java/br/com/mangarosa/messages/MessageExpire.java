package br.com.mangarosa.messages;

import br.com.mangarosa.topics.Topic;
import br.com.mangarosa.connection.RedisConnection;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class MessageExpire {
    private final RedisCommands<String, String> syncCommands = RedisConnection.connect();
    private Topic topic;

    public MessageExpire(Topic topic) {
        this.topic = topic;
    }

    public void messageExpire() {
        // Lê todas as mensagens no stream do Redis associadas ao tópico a partir do ID "0".
        List<StreamMessage<String, String>> messages = syncCommands.xread(
                XReadArgs.StreamOffset.from(topic.name(), "0")
        );

        System.out.println(messages.isEmpty());
        for(StreamMessage<String, String> message : messages) {
        // Loop que percorre cada mensagem retornada pelo Redis.

            String jsonMessage = message.getBody().get("messageJson");
            // Extrai o campo "messageJson" do corpo da mensagem e o converte para JSON.
            Message messageObj = Message.fromJson(jsonMessage);
            // Converte a string JSON de volta para um objeto Message.


            if(!messageObj.isConsumed()) {
                System.out.println(message.getBody());

                LocalDateTime createdAt = messageObj.getCreatedAt();
                LocalDateTime now = LocalDateTime.now();

                long duration = Duration.between(createdAt, now).toMinutes();

                System.out.println("Tempo: " + duration);

                if(duration >= 5) {
                    syncCommands.xdel(topic.name(), message.getId());
                }
            }
        }
    }
}
