package br.com.mangarosa;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;
import io.lettuce.core.RedisClient;

import java.util.HashSet;
import java.util.Set;

public class TProducer implements Producer {

    private Set<Topic> topicSet; // Conjunto de tópicos associados ao produtor
    private String producerIdentifier; // Identificador do produtor

    public TProducer(RedisClient redisConnection, String producerIdentifier) {
        this.topicSet = new HashSet<>();
        this.producerIdentifier = producerIdentifier;
    }

    @Override
    public void addTopic(Topic topic) {
        // Adiciona um tópico ao conjunto de tópicos associados ao produtor
        topicSet.add(topic);
    }

    @Override
    public void removeTopic(Topic topic) {
        // Remove um tópico do conjunto de tópicos associados ao produtor
        topicSet.remove(topic);
    }

    @Override
    public void sendMessage(String messageContent) {
        // Cria e envia uma nova mensagem para todos os tópicos associados
        for (Topic topic : topicSet) {
            Message message = new Message(this, messageContent);
            topic.addMessage(message);
        }
    }

    @Override
    public String name() {
        // Retorna o identificador do produtor
        return this.producerIdentifier;
    }

    @Override
    public String toString() {
        // Retorna uma representação em string do nome do produtor
        return name();
    }
}
