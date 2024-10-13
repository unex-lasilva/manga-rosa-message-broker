package br.com.mangarosa;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

import java.util.HashMap;

public class TProducer implements Producer {
    @Override
    public void addTopic(Topic topic) {

    }

    @Override
    public void removeTopic(Topic topic) {

    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String name() {
        return "Teste";
    }

    @Override
    public HashMap<String, Message> generateMessage(String message, int count) {
        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":" + name()
                + "}";
    }
}
