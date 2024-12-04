package br.com.mangarosa.queue.longdistanceitems;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

import java.util.List;

public class LongDistanceConsumer {
    private final MessageRepository messageRepository;

    // Construtor que inicializa o repositório de mensagens
    public LongDistanceConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Método para consumir mensagens do tópico "queue/long-distance-items"
    public void consume() {
        // Obtém todas as mensagens não consumidas do tópico "queue/long-distance-items"
        List<Message> messages = messageRepository.getAllNotConsumedMessagesByTopic("queue/long-distance-items");

        // Lista de mensagens
        for (Message message : messages) {
            // Verifica se a mensagem não expirou
            if (!message.isExpired()) {
                // Processa a mensagem
                System.out.println("Consuming message: " + message.getContent());
                // Marca a mensagem como consumida
                message.setConsumed(true);
            } else {
                // Informa que a mensagem expirou
                System.out.println("Message expired: " + message.getContent());
            }
        }
    }
}
