package br.com.mangarosa.queue.fastdeliveryitems;

// Importa as classes necessárias para o funcionamento
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

public class FoodDeliveryProducer {
    // Armazena o repositório de mensagens e o tópico onde as mensagens serão enviadas
    private final MessageRepository messageRepository;
    private final String topic;

    //Recebe o repositório de mensagens e o tópico
    public FoodDeliveryProducer(MessageRepository messageRepository, String topic) {
        this.messageRepository = messageRepository; // Inicializa o repositório de mensagens
        this.topic = topic; // Inicializa o tópico onde as mensagens serão enviadas
    }

    // Método para produzir mensagens
    public void produce(String messageContent) {
        // Cria uma nova mensagem com o conteúdo fornecido e o produtor "FoodDeliveryProducer"
        Message message = new Message(messageContent, "FoodDeliveryProducer");
        // Adiciona a mensagem ao tópico no repositório
        messageRepository.addMessageToTopic(topic, message);
        // Exibe uma confirmação no console
        System.out.println("Mensagem produzida: " + messageContent);
    }
}
