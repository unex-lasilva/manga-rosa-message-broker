package br.com.mangarosa.interfaces;

import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class TopicImpl extends Topic {
    private final String name;
    private final List<Message> messages = new ArrayList<>();

    public TopicImpl(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        // Lógica para manipular a mensagem, se necessário
    }

    // Outros métodos conforme necessário
}
