package br.com.mangarosa;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;

public class TConsumer implements Consumer {
    private String nomeConsumidor;

    public TConsumer(String nomeConsumidor) {
        this.nomeConsumidor = nomeConsumidor;
    }

    @Override
    public boolean consume(Message mensagem) {
        // Adiciona o consumidor à lista de consumos da mensagem
        mensagem.addConsumption(this);
        
        // Marca a mensagem como consumida
        mensagem.setConsumed(true);
        
        // Imprime no console que a mensagem foi consumida
        System.out.println("Mensagem consumida por " + this.nomeConsumidor + ": " + mensagem.getMessage());
        
        // Retorna true se a mensagem foi processada com sucesso
        return mensagem.isConsumed();
    }

    @Override
    public String name() {
        // Retorna o nome do consumidor
        return this.nomeConsumidor;
    }
}
