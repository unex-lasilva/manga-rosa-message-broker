package br.com.mangarosa;

import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.interfaces.Topic;

public abstract class TProducer extends Producer {
    // Construtor que recebe um ID do produtor e chama o construtor da classe pai
    public TProducer(String producerId) {
        super(producerId); // Chama o construtor da classe base Producer
    }

    // Método para adicionar um tópico ao produtor (não implementado)
    @Override
    public void addTopic(Topic topic) {
        // Implementação a ser definida, caso o produtor precise adicionar tópicos
    }

    // Método para remover um tópico do produtor (não implementado)
    @Override
    public void removeTopic(Topic topic) {
        // Implementação a ser definida, caso o produtor precise remover tópicos
    }

    // Método para enviar uma mensagem (não implementado)
    @Override
    public void sendMessage(String message) {
        // Implementação a ser definida para enviar uma mensagem para o tópico
    }

    // Método que retorna o nome do produtor
    @Override
    public String name() {
        return "Teste"; // Retorna um nome fixo para o produtor
    }

    // Método toString que formata a saída do produtor como JSON
    @Override
    public String toString() {
        return "{" +
                "\"name\":" + name() // Formata o nome do produtor em formato JSON
                + "}";
    }
}
