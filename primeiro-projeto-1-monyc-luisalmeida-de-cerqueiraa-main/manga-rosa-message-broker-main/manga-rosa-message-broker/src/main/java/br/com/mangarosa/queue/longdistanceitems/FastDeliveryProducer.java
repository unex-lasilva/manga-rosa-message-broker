package br.com.mangarosa.queue.longdistanceitems;

// Importa as classes necessárias para o funcionamento
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class FastDeliveryProducer {
    // Armazena o repositório de mensagens e o tópico onde as mensagens serão enviadas
    private final MessageRepository messageRepository;
    private final Topic topic;

    // Inicializa o repositório de mensagens e o tópico
    public FastDeliveryProducer(MessageRepository messageRepository, Topic topic) {
        this.messageRepository = messageRepository; // Inicializa o repositório de mensagens
        this.topic = topic; // Inicializa o tópico onde as mensagens serão enviadas
    }

    // Método para produzir mensagens
    public void produce(String messageContent) {
        // Cria uma nova mensagem com o conteúdo e define o produtor: "FoodDeliveryProducer"
        Message message = new Message(messageContent, "FoodDeliveryProducer");
        // Adiciona a mensagem ao tópico indicado
        messageRepository.addMessageToTopic(topic.name(), message);
        // Exibe uma confirmação de que a mensagem foi produzida
        System.out.println("Mensagem produzida: " + messageContent);
    }

}
