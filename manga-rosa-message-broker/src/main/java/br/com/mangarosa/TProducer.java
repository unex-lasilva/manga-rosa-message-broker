package br.com.mangarosa;

import java.util.ArrayList;
import java.util.List;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class TProducer implements Producer {

    private String name;
    private List<Topic> topics;

    TProducer(String name) {
        this.name = name;
        this.topics = new ArrayList<>();
    }

    /**
     * Adiciona o tópico na lista de tópicos
     *
     * @param topic topico
     */
    @Override
    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    /**
     * Remove o tópico da lista de tópicos
     *
     * @param topic topico
     */
    @Override
    public void removeTopic(Topic topic) {
        this.topics.remove(topic);
    }

    /**
     * Envia a messagem para todos os consumidores do tópico
     *
     * @param message mensagem para ser processada
     */
    @Override
    public void sendMessage(String message) {
        topics.forEach(topic -> {
            Message mess = new Message(this, message);
            topic.addMessage(mess);
            topic.getRepository().append(topic.name(), mess);            
        });
    }

    /**
     * Retorna o nome do tópico que deve ser único em um broker
     *
     * @return nome do tópico
     */
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return "{"
                + "\"name\":" + name()
                + "}";
    }
}
