package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Consumer;

public class LongDistanceConsumer implements Consumer {

    @Override
    public String consume(String message) {
        // Processa a mensagem de longa distância
        System.out.println("Consumindo mensagem: " + message);

        // Retorna "ok" para indicar sucesso
        return "ok";
    }

    @Override
    public String getName() {
        return "Long Distance Consumer";
    }
}





