package br.com.mangarosa;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;

/**
 * Implementação do produtor de mensagens.
 */
public class TProducer implements Producer {

    @Override
    public void addTopic(Topic topic) {
        // Implementação para adicionar um tópico
        System.out.println("Tópico adicionado: " + topic.name());
    }

    @Override
    public void removeTopic(Topic topic) {
        // Implementação para remover um tópico
        System.out.println("Tópico removido: " + topic.name());
    }

    @Override
    public void sendMessage(String message) {
        // Implementação para enviar uma mensagem
        System.out.println("Mensagem enviada: " + message);
    }

    @Override
    public String name() {
        return "Teste"; // Nome do produtor
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":" + name() +
                "}";
    }
}
