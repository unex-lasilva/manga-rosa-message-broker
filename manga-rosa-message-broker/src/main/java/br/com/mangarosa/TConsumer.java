package br.com.mangarosa;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;

public class TConsumer implements Consumer {

    private String name;

    TConsumer(String name) {
        this.name = name;
    }

    /**
     * Consome uma mensagem colocando-a como processada. No final, você deve
     * adicionar o consumo.
     *
     * @param message mensagem para ser consumida
     * @return true - se a mensagem foi consumida adequadamente
     */
    @Override
    public boolean consume(Message message) {
        //TODO: SÓ DEUS SABE
        message.addConsumption(this);
        message.setConsumed(true);

        return message.isConsumed();
    }


    /**
     * Retorna o nome do consumer
     *
     * @return nome do consumidor
     */
    @Override
    public String name() {
        return this.name;
    }

}
