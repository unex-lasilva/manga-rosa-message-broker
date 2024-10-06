package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.util.List;
import java.util.UUID;

/**
 * Interface que representa um repositório de mensagens, responsável por armazenar e gerenciar mensagens.
 */
public interface MessageRepository {

    /**
     * Adiciona uma nova mensagem no final da fila do tópico.
     * Se o tópico não existir, deve ser criado e a mensagem adicionada.
     *
     * @param topic O nome do tópico (deve ser único).
     * @param message A mensagem a ser adicionada.
     */
    void append(String topic, Message message);

    /**
     * Marca uma mensagem como consumida.
     * Se o tópico ou a mensagem com o messageId não existirem, deve ser lançada uma exceção.
     *
     * @param topic O nome do tópico (deve existir).
     * @param messageId O código da mensagem a ser consumida.
     */
    void consumeMessage(String topic, UUID messageId);

    /**
     * Retorna todas as mensagens ainda não consumidas e não expiradas de um tópico.
     * Se o tópico não existir, deve ser lançada uma exceção.
     *
     * @param topic O nome do tópico.
     * @return Uma lista com todas as mensagens não consumidas.
     */
    List<Message> getAllNotConsumedMessagesByTopic(String topic);

    /**
     * Retorna todas as mensagens consumidas de um tópico que ainda não foram expiradas.
     * Se o tópico não existir, deve ser lançada uma exceção.
     *
     * @param topic O nome do tópico.
     * @return Uma lista com todas as mensagens consumidas.
     */
    List<Message> getAllConsumedMessagesByTopic(String topic);
}
