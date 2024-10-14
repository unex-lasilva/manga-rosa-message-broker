package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    /**
     * Adiciona uma nova mensagem no final da fila do tópico.
     * Se o tópico não existir, ele deve ser criado e a mensagem adicionada.
     * @param topic nome do tópico que deve ser único
     * @param message mensagem - json
     */
    void append(String topic, Message message);

    /**
     * Grava uma mensagem como consumida.
     * Se o tópico não existir ou a mensagem com messageId também não existir no
     * tópico, uma exceção deve ser lançada
     * @param topic nome do tópico que deve existir
     * @param messageId código da mensagem
     */
    void consumeMessage(String topic, UUID messageId);

    /**
     * Retorna todas as mensagens ainda não consumidas e não expiradas que estão num tópico.
     * Se um tópico não existir, uma exceção deve ser lançada.
     * @param topic nome do tópico
     * @return uma lista com todas as mensagens não consumidas
     */
    List<Message> getAllNotConsumedMessagesByTopic(String topic);

    /**
     * Retorna todas as mensagens consumidas em um tópico e que ainda não foram expiradas.
     * Se um tópico não existir, uma exceção deve ser lançada.
     * @param topic nome do tópico
     * @return uma lista com todoas as mensagens consumidas.
     */
    List<Message> getAllConsumedMessagesByTopic(String topic);


}
