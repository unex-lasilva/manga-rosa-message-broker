package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

/**
 * Um consumidor é responsável por processar as mensagens enviadas
 * por um produtor.
 */
public interface Consumer {
    /**
     * Consome uma mensagem colocando-a como processada.
     * @param message
     */
    void consume(Message message);
}
