package br.com.mangarosa.consumers;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;

/**
 * Implementação do consumidor LongDistance, que consome mensagens de longa distância.
 */
public class LongDistanceConsumer implements Consumer {
    private MessageRepository repository;

    public LongDistanceConsumer(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean consume(Message message) {
        if (message != null) {
            System.out.println("Consumindo mensagem de longa distância: " + message.getContent());
            // Aqui você pode adicionar lógica para marcar a mensagem como consumida, se necessário
            return true; // Retorna true para indicar que a mensagem foi consumida com sucesso
        } else {
            System.out.println("Nenhuma mensagem encontrada para consumo.");
            return false; // Retorna false se a mensagem não foi encontrada
        }
    }

    @Override
    public String name() {
        return "Long Distance Consumer"; // Nome do consumidor
    }
}
