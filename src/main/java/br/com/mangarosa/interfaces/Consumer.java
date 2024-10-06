package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.io.Serializable;

/**
 * Interface que representa um consumidor responsável por processar mensagens enviadas por um produtor.
 */
public interface Consumer extends Serializable {

    /**
     * Consome uma mensagem, marcando-a como processada.
     *
     * @param message A mensagem a ser consumida.
     * @return true se a mensagem foi consumida com sucesso, false caso contrário.
     */
    boolean consume(Message message);

    /**
     * Retorna o nome do consumidor.
     *
     * @return O nome do consumidor.
     */
    String name();
}
