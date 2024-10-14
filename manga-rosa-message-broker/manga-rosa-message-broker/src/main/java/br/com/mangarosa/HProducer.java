package br.com.mangarosa;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageBroker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.mangarosa.RedisMessageRepository;
import br.com.mangarosa.interfaces.Topic;

public class HProducer implements Producer {

    private String name;
    private List<Topic> topics;
    
    RedisMessageRepository repository = new RedisMessageRepository("localhost", 6379);
    MessageBroker messageBroker = new MessageBroker(repository);

    /**
     * Construtor padrão que inicializa a lista de tópicos.
     */
    public HProducer() {
        this.topics = new ArrayList<>();
    }

    /**
     * Construtor que inicializa o produtor com um nome.
     * @param name Nome do produtor
     */
    public HProducer(String name) {
        this();
        this.name = name;
    }

    /**
     * Adiciona um tópico à lista de tópicos do produtor.
     * Se o tópico não existir, ele é adicionado e criado no message broker.
     * @param topic Tópico a ser adicionado
     */
    @Override
    public void addTopic(Topic topic) {
        if (!topics.contains(topic)) {
            topics.add(topic);
            messageBroker.createTopic(topic);
        }
    }

    /**
     * Remove um tópico da lista de tópicos do produtor.
     * @param topic Tópico a ser removido
     */
    @Override
    public void removeTopic(Topic topic) {
        topics.remove(topic);
    }

    /**
     * Envia uma mensagem para todos os tópicos associados ao produtor.
     * @param message Conteúdo da mensagem a ser enviada
     */
    @Override
    public void sendMessage(String message) {
        for (Topic topic : topics) {
            Message messages = new Message(this, message);
            topic.notifyConsumers(messages);
        }
    }

    /**
     * Retorna o nome do produtor.
     * @return Nome do produtor
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Retorna uma representação em string do produtor.
     * @return String no formato JSON com o nome do produtor
     */
    @Override
    public String toString() {
        return "{" +
                "\"name\":" + name()
                + "}";
    }

    
}
