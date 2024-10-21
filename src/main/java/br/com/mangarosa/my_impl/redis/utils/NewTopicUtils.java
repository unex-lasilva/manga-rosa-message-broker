package br.com.mangarosa.my_impl.redis.utils;

/**
 * Este é um pacote de ferramentas para trabalhar com os tópicos e chaves do Redis.
 * Aqui você encontra funcionalidades que te ajudam a encontrar chaves específicas no banco de dados.
 * Não pode ser instanciado.
 */

public class NewTopicUtils {
    /** Parte da chave composta dentro do Redis referente aos tópicos. Primeiro termo da chave composta. */

    final public static String rootKey = "topic";
    /** Parte da chave composta dentro do Redis referente às mensagens. */
    final public static String messagesPartialKey = "messages";
    /** Parte da chave composta dentro do Redis referente aos consumers. */
    final public static String consumersPartialKey = "consumers";
    /** Parte da chave composta dentro do Redis referente aos producers. */
    final public static String producersPartialKey = "producers";
    /** Delimitador que separa os termos dentro da chave composta. */
    final public static String delimiter = ":";

    private NewTopicUtils(){}

    /**
     * Obtém a chave do Redis responsável por armazenar as mensagens enviadas
     * @param topicName nome do tópico que se deseja obter a chave de mensagens dentro do Redis
     * @return a chave dentro do Redis pertencente ao tópico informado
     */
    public static String getTopicKey(String topicName) {
        String[] terms = {rootKey, topicName, messagesPartialKey};
        return String.join(delimiter, terms);
    }

    /**
     * Obtém a chave do Redis responsável por armazenar os consumidores de um tópico específico
     * @param topicName nome do tópico que se deseja obter a chave de consumidores dentro do Redis
     * @return a chave dentro do Redis pertencente aos consumidores do tópico informado
     */
    public static String getConsumersKey(String topicName) {
        String[] terms = {rootKey, topicName, consumersPartialKey};
        return String.join(delimiter, terms);
    }

    /**
     * Obtém a chave do Redis responsável por armazenar os produtores de um tópico específico
     * @param topicName nome do tópico que se deseja obter a chave de produtores dentro do Redis
     * @return a chave dentro do Redis pertencente aos produtos do tópico informado
     */
    public static String getProducersKey(String topicName) {
        String[] terms = {rootKey, topicName, producersPartialKey};
        return String.join(delimiter, terms);
    }

    /**
     * Obtém um pattern que permite localizar todos os tópicos de mensagens dentro do Redis.
     * @return o pattern para localizar todas as chaves a partir de uma busca (ex: keys [PATTERN])
     */
    public static String getMessagesPattern() {
        String[] terms = {rootKey, "*", messagesPartialKey};
        return String.join(delimiter, terms);
    }
}