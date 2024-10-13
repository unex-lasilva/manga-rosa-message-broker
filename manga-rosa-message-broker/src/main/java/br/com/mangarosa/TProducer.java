package br.com.mangarosa;

import java.util.ArrayList;
import java.util.List;

import br.com.mangarosa.interfaces.Consumer;
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

  

    @Override
    public void addTopic(Topic topic) {
        // adiciona o tópico
        this.topics.add(topic);
    }

    
    @Override
    public void removeTopic(Topic topic) {
        // remove o tópico
        this.topics.remove(topic);
    }

   
    @Override
    public void sendMessage(String message) {
        this.topics.forEach(t -> {
            // cria a msg
            Message msg = new Message(this, message);
            // adiciona a msg na fila
            t.addMessage(msg);
            // adiciona a msg no repositorio
            t.getRepository().append(t.name(), msg);
        });
    }

    @Override
    public String name() {
        // retorna o nome do consumidor
        return this.name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":" + name()
                + "}";
    }
}
