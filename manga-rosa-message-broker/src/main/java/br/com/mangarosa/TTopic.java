package br.com.mangarosa;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class TTopic implements Topic {
    private final String topicName;
    private final List<Consumer> subscriberList;
    private final MessageRepository messageRepo;

    public TTopic(String topicName, MessageRepository messageRepo) {
        this.topicName = topicName;
        this.messageRepo = messageRepo;
        this.subscriberList = new ArrayList<>();
    }

    @Override
    public String name() {
        // Retorna o nome do tópico
        return topicName;
    }

    @Override
    public void addMessage(Message message) {
        // Adiciona a mensagem ao repositório usando o nome do tópico
        messageRepo.append(topicName, message);

        // Marca a mensagem como processada após ser enviada para os consumidores
        messageRepo.consumeMessage(topicName, message.getId());

        // Chama os consumidores para receberem a nova mensagem
        notifyConsumers(message);
    }

    @Override
    public void subscribe(Consumer consumer) {
        // Adiciona um novo consumidor à lista de inscritos
        subscriberList.add(consumer);
    }

    @Override
    public void unsubscribe(Consumer consumer) {
        // Remove um consumidor da lista de inscritos
        subscriberList.remove(consumer);
    }

    @Override
    public List<Consumer> consumers() {
        // Retorna a lista de consumidores inscritos
        return subscriberList;
    }

    @Override
    public MessageRepository getRepository() {
        // Retorna o repositório de mensagens associado ao tópico
        return messageRepo;
    }

    // Método para notificar todos os consumidores de uma nova mensagem
    @Override
    public void notifyConsumers(Message message) {
        for (Consumer consumer : subscriberList) {
            consumer.consume(message);
        }
    }
}
