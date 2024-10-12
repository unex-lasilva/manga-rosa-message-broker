package main.br.com.mangarosa.producer;

import main.br.com.mangarosa.interfaces.Producer;
import main.br.com.mangarosa.interfaces.Topic;
import main.br.com.mangarosa.messages.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger; // resolvi usar o Logback para ter um monitoramento mais detalhado da execução do sistema
import org.slf4j.LoggerFactory;

public class FastDeliveryProducer implements Producer
{
    private final Map<String, Topic> topicList;
    private final String producerName;
    private static final Logger logger = LoggerFactory.getLogger(FastDeliveryProducer.class);
    public FastDeliveryProducer(String name) {
        topicList = new HashMap<>();
        producerName = name;
    }

    @Override
    public void addTopic(Topic topic) {
        try {
            if (topic.isCreated()) {
                if (!topicList.containsKey(topic.name())) {
                    topicList.put(topic.name(), topic);
                    logger.info("O tópico \"{}\" foi adicionado com sucesso!", topic.name());
                } else
                    logger.warn("Este tópico já existe na fila");
            } else
                throw new Exception("Tópico não criado");
        } catch (Exception e) {
            logger.error("Erro ao adicionar o tópico: {}", e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void removeTopic(Topic topic) {
        try {
            if (topicList.containsKey(topic.name())) {
                topicList.remove(topic.name());
                logger.info("O tópico \"{}\" foi removido com sucesso!", topic.name());
            } else
                logger.warn("Tópico não encontrado");
        } catch (Exception e) {
            logger.error("Erro ao remover o tópico: {}", e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void sendMessage(String message) {
        if (topicList.isEmpty()) {
            logger.warn("Nenhum tópico adicionado ao produtor \"{}\". A mensagem não será enviada.", producerName);
            return;
        }
        HashMap<String, Message> messages = generateMessage(message, 1);
        for (Message m : messages.values()) {
            for (Map.Entry<String, Topic> entry : topicList.entrySet()) {
                String topicName = entry.getKey();
                Topic topic = entry.getValue();
                try {
                    topic.addMessage(m);
                    logger.info("Mensagem enviada pelo produtor \"{}\" para o tópico \"{}\": {}", producerName, topicName, m.getMessage());
                } catch (Exception e) {
                    logger.error("Erro ao enviar mensagem para o tópico \"{}\": {}", topicName, e.getMessage());
                }
            }
        }
    }

    @Override
    public String name() {
        return producerName;
    }

    public HashMap<String, Message> generateMessage(String message, int count) {
        HashMap<String, Message> messages = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String messageId = UUID.randomUUID().toString();
            Message m = new Message()
                    .setMessage(message)
                    .setId(messageId)
                    .setProducer(this);
            messages.put(messageId, m);
        }
        return messages;
    }
}