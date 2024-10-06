package br.com.mangarosa;

import br.com.mangarosa.messages.MessageBroker;

public class Main {
    public static void main(String[] args) {
        MessageBroker broker = new MessageBroker();
        String topic = "delivery";

        // Produzindo várias mensagens
        for (int i = 1; i <= 5; i++) {
            String message = "Pedido " + i + " de entrega em andamento";
            broker.pushMessage(topic, message);
            System.out.println("Mensagem produzida: " + message);
        }

        // Consumindo várias mensagens
        for (int i = 1; i <= 5; i++) {
            String message = broker.popMessage(topic);
            if (message != null) {
                System.out.println("Mensagem recebida: " + message);
            } else {
                System.out.println("Não há mais mensagens para receber.");
                break; // Para evitar tentar consumir mais mensagens do que foram produzidas
            }
        }
    }
}
