package br.com.mangarosa.interfaces;

import java.io.Serializable;

/**
 * Interface que representa um produtor responsável por gerar mensagens para serem consumidas.
 */
public interface Producer extends Serializable {

    /**
     * Adiciona um tópico à lista de tópicos.
     *
     * @param topic O tópico a ser adicionado.
     */
    void addTopic(Topic topic);

    /**
     * Remove um tópico da lista de tópicos.
     *
     * @param topic O tópico a ser removido.
     */
    void removeTopic(Topic topic);

    /**
     * Envia uma mensagem para todos os consumidores do tópico.
     *
     * @param message A mensagem a ser processada.
     */
    void sendMessage(String message);

    /**
     * Retorna o nome do tópico, que deve ser único em um broker.
     *
     * @return O nome do tópico.
     */
    String name();
}
