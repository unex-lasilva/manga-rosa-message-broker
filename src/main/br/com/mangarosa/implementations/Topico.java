package main.br.com.mangarosa.implementations;

import main.br.com.mangarosa.interfaces.Consumer;
import main.br.com.mangarosa.interfaces.MessageRepository;
import main.br.com.mangarosa.interfaces.Topic;
import main.br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger; // resolvi usar o Logback para ter um monitoramento mais detalhado da execução do sistema
import org.slf4j.LoggerFactory;

public class Topico implements Topic
{
    private final List<Message> messages;
    private final String topicName;
    private final boolean created;
    private final List<Consumer> consumers;
    private static final Logger logger = LoggerFactory.getLogger(Topico.class);

    /**
     *
     * @param name nome do tópico
     */
    public Topico(String name) {
        topicName = name;
        messages = new ArrayList<>();
        consumers = new ArrayList<>();
        created = true;
    }

    public boolean isCreated() {
        return created;
    }

    @Override
    public String name() {
        return topicName;
    }

    @Override
    public void addMessage(Message m) {
        try {
            messages.add(m);
        } catch (Exception e) {
            logger.error("Erro ao adicionar a mensagem: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    @Override
    public void subscribe(Consumer consumer) {
        try {
            consumers.add(consumer);
        } catch (Exception e) {
            logger.error("Erro ao inscrever o consumidor: {}", e.getMessage(), e);
        }
    }

    @Override
    public void unsubscribe(Consumer consumer) {
        try {
            consumers.remove(consumer);
        } catch (Exception e) {
            logger.error("Erro ao desinscrever o consumidor: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<Consumer> consumers() {
        return consumers;
    }

    @Override
    public MessageRepository getRepository() {
        return null;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
