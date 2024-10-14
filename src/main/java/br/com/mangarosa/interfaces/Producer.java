package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Produz mensagens para serem consumidas
 */
public interface Producer extends Serializable {

    /**
     * Adiciona o tópico na lista de tópicos
     * @param topic topico
     */
    void addTopic(Topic topic);

    /**
     * Remove o tópico da lista de tópicos
     * @param topic topico
     */
    void removeTopic(Topic topic);

    /**
     * Envia a messagem para todos os consumidores do tópico
     * @param message mensagem para ser processada
     */
    void sendMessage(String message);

    /**
     * Retorna o nome do tópico que deve ser único em um broker
     * @return nome do tópico
     */
    String name();

    HashMap<String, Message> generateMessage(String message, int count);
}
