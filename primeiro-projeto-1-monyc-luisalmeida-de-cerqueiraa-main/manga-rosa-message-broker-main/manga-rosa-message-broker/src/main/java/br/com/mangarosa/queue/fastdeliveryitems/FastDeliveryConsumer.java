package br.com.mangarosa.queue.fastdeliveryitems;

// Importa as classes necessárias para o funcionamento
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

import java.util.List;

public class FastDeliveryConsumer {
    // Armazena uma referência para o repositório de mensagens
    private final MessageRepository messageRepository;

    // Construtor da classe que recebe um repositório de mensagens
    public FastDeliveryConsumer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Método para consumir mensagens de um tópico específico
    public void consume(String topicName) {
        // Obtém todas as mensagens que ainda não foram consumidas desse tópico
        List<Message> messages = messageRepository.getAllNotConsumedMessagesByTopic(topicName);

        // Percorre cada mensagem recuperada
        for (Message message : messages) {
            // Verifica se a mensagem não expirou
            if (!message.isExpired()) {
                // Exibe o conteúdo da mensagem na tela
                System.out.println("Consumindo mensagem: " + message.getContent());
                // Marca a mensagem como consumida no repositório
                messageRepository.consumeMessage(topicName, message.getId());
            } else {
                // Informa que a mensagem já expirou
                System.out.println("Mensagem expirada: " + message.getContent());
            }
        }
    }
}
