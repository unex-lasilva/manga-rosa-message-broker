package br.com.mangarosa.queue.fastdeliveryitems;

// Importa as classes necessárias para o funcionamento
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class PhysicPersonDeliveryProducer {
    // Armazena o repositório de mensagens e o tópico onde as mensagens serão enviadas
    private final MessageRepository messageRepository;
    private final Topic topic;

    // Recebe o repositório de mensagens e o tópico
    public PhysicPersonDeliveryProducer(MessageRepository messageRepository, Topic topic) {
        this.messageRepository = messageRepository; // Inicializa o repositório de mensagens
        this.topic = topic; // Inicializa o tópico onde as mensagens serão enviadas
    }

    // produz mensagem
    public void produce(String messageContent) {
        // Cria uma nova mensagem com o conteúdo fornecido e o produtor "PhysicPersonDeliveryProducer"
        Message message = new Message(messageContent, "PhysicPersonDeliveryProducer");
        // Adiciona a mensagem ao tópico no repositório
        messageRepository.addMessageToTopic(topic.getName(), message);
        // Exibe uma confirmação no console
        System.out.println("Mensagem produzida: " + messageContent);
    }
}
