package br.com.mangarosa.consumers;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;

/**
 * Implementação do consumidor FastDelivery, que consome mensagens específicas.
 */
public class FastDeliveryConsumer implements Consumer {

    public FastDeliveryConsumer() {
        // Construtor sem parâmetros
    }

    @Override
    public boolean consume(Message message) {
        // Implementação da lógica de consumo de mensagens
        System.out.println("Consumindo mensagem de entrega rápida: " + message.getContent());
        return true; // Retorna true para indicar que a mensagem foi consumida com sucesso
    }

    @Override
    public String name() {
        return "Fast Delivery Consumer"; // Nome do consumidor
    }
}
