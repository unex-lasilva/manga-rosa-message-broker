package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Consumer;

public class MessageConsumption {

    private Consumer consumer;

    public MessageConsumption(Consumer consumer) {
        this.consumer = consumer;
    }

    public void processMessage(String message) {
        // Processa a mensagem e captura o retorno do consumidor
        String result = consumer.consume(message);

        // Verifica o resultado e imprime "ok" se for o caso
        if ("ok".equals(result)) {
            System.out.println("Mensagem processada: Sucesso");
        } else {
            System.out.println("Erro ao processar a mensagem: " + message);
        }
    }
}

