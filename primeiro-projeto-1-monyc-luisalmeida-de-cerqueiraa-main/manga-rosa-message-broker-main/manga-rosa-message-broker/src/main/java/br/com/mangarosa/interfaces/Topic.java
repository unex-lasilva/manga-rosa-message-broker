package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String name; // Nome do tópico
    private List<Message> messages; // Lista de mensagens associadas ao tópico

    // Construtor que inicializa o nome e a lista de mensagens
    public Topic(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public Topic() {

    }

    // Método para adicionar uma mensagem ao tópico
    public void addMessage(Message message) {
        messages.add(message);
    }

    // Método para obter todas as mensagens não consumidas
    public List<Message> getNotConsumedMessages() {
        List<Message> notConsumed = new ArrayList<>();
        for (Message message : messages) {
            if (!message.isConsumed()) {
                notConsumed.add(message);
            }
        }
        return notConsumed;
    }

    // Método para obter todas as mensagens consumidas
    public List<Message> getConsumedMessages() {
        List<Message> consumed = new ArrayList<>();
        for (Message message : messages) {
            if (message.isConsumed()) {
                consumed.add(message);
            }
        }
        return consumed;
    }

    public String getName() {
        return name; // Retorna o nome do tópico
    }

    public List<Message> getMessages() {
        return messages; // Retorna todas as mensagens do tópico
    }

    public String name() {
        return null;
    }
}
