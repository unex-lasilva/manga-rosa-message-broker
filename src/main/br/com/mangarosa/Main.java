package br.com.mangarosa;

import br.com.mangarosa.messages.Message;
import io.lettuce.core.Consumer;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.XReadArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        TProducer tProducer = new TProducer();
        Message message = new Message(tProducer, "Teste");
        Message message2 = new Message(tProducer, "Teste");
        Map<String, String> m = message2.toMap();

        System.out.println(m);
        System.out.println(message2.getId());
        System.out.println(message.getId());


        RedisClient redisClient = RedisClient.create("redis://localhost:6379"); // change to reflect your environment
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> syncCommands = connection.sync();

        String messageId = syncCommands.xadd(
                tProducer.name(),
                m);
        message.setId(messageId);

        System.out.println( String.format("Message %s : %s posted", messageId, m) );

        List<StreamMessage<String, String>> messages = syncCommands.xreadgroup(
                Consumer.from("application_1", "consumer_1"),
                XReadArgs.StreamOffset.lastConsumed(tProducer.name())
        );

        if (!messages.isEmpty()) {
            for (StreamMessage<String, String> message1 : messages) {
                System.out.println(message1);
                // Confirm that the message has been processed using XACK
                syncCommands.xack("application_1", "application_1",  message1.getId());
            }
        }

        connection.close();
        redisClient.shutdown();
    }
}