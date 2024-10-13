package br.com.mangarosa.queue.longdistanceitems;

// Importa as classes necessárias
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

public class PyMarketPlaceProducer {
    // Armazena o repositório de mensagens
    private final MessageRepository messageRepository;

    // Construtor que inicializa o repositório de mensagens
    public PyMarketPlaceProducer(MessageRepository messageRepository) {
        this.messageRepository = messageRepository; // Inicializa o repositório de mensagens
    }

    // Produz mensagem
    public void produce(String messageContent) {
        // Cria uma nova mensagem com o conteúdo fornecido e define o produtor inicial como "FoodDeliveryProducer"
        Message message = new Message(messageContent, "FoodDeliveryProducer");
        // Ajusta o produtor para "PyMarketPlaceProducer"
        message.setProducer("PyMarketPlaceProducer");
        // Adiciona a mensagem ao repositório
        messageRepository.append(message);
    }
}
