package br.com.mangarosa.consumer;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;

public class BaseConsumer implements Consumer {
    private String consumerName;
    public BaseConsumer(String consumerName) {
        this.consumerName = consumerName;
    }
    @Override
    public boolean consume(Message message) {
        // Marca a mensagem como consumida
        message.addConsumption(this);
        message.setConsumed(true);
        System.out.println("Mensagem Consumida por " + this.consumerName + ": " + message.getMessage());
        return message.isConsumed(); // Retorna true se a mensagem foi processada com sucesso
    }

    @Override
    public String name() {
        return this.consumerName;
    }
}
