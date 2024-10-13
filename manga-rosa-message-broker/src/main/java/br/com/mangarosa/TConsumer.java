package br.com.mangarosa;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;

public  class TConsumer implements Consumer{
    private String name;
    TConsumer(String name) {
        this.name = name;
    }

    @Override
    public boolean consume(Message message) {
            // adiciona a msg como consumida
            message.addConsumption(this); 
            // troca o status da msg para consumida     
            message.setConsumed(true);
            // retorna se a msg foi consumida
            return message.isConsumed();
    }

    @Override
    public String name() {
        // retorna o nome do consumidor
        return this.name;
    }

    
}
