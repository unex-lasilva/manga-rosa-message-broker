package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Consumer;

public class FastDeliveryConsumer implements Consumer {


    @Override
    public String consume(String message) {
        System.out.println("Consumindo mensagem no FastDeliveryConsumer: " + message);
        return message;
    }


    @Override
    public String getName() {
        return "Fast Delivery Consumer";  // Nome do consumidor
    }
}






