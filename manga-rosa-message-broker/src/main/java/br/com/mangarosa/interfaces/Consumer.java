package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.io.Serializable;

/**
 * Um consumidor é responsável por processar as mensagens enviadas
 * por um produtor.
 */
public interface Consumer extends Serializable {
    /**
     * Consome uma mensagem colocando-a como processada.
     * No final, você deve adicionar o consumo.
     * @param message mensagem para ser consumida
     * @return true - se a mensagem foi consumida adequadamente
     */
    boolean consume(Message message);

    /**
     * Retorna o nome do consumer
     * @return nome do consumidor
     */
    String name();
}
