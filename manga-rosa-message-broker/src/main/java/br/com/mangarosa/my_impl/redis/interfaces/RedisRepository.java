package br.com.mangarosa.my_impl.redis.interfaces;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.my_impl.redis.utils.RedisMessagesUtils;
import br.com.mangarosa.my_impl.redis.utils.RedisTopicUtils;

import com.fasterxml.uuid.Generators;

import redis.clients.jedis.Jedis;

/**
 * Esta classe representa um repositório de mensagens que utiliza o Redis como armazenamento.
 * Ela é uma interface que conecta a aplicação ao Redis.
 * Assim que o repositório é instanciado, a conexão com o Redis é iniciada.
 * Além disso, periodicamente o repositório verificará se há mensagens expiradas e as removerá.
 * Esta classe permite que a conexão possa ser configurada
 * (Implementa a interface genérica MessageRepository).
 * @author Denilson Santos
 */

public class RedisRepository implements MessageRepository{
    /** Endereço aonde o Redis está hospedado. */
    final private String host;
    /** Porta de conexão com o Redis. */
    final private int port;
    /** Tempo (em segundos) que as mensagens poderão ficar no Redis antes de serem dadas como expiradas. */
    final private int messagesTTL = 300;
    /** Cliente Redis. Permite a realização de operações com o banco de dados por meio de acesso direto a ele. */
    final private Jedis controller;
    /** Quantidade de tempo em minutos entre cada verificação dentro do repositório por mensagens expiradas. */
    final private int checkMinutesInterval = 1;

    // ------------------------------------------------ //
    // ----------------- Construtores ----------------- //
    // ------------------------------------------------ //

    /**
     * Construtor da classe RedisRepository.
     * Inicializa o repositório com os atributos padrão de conexão.
     */
    public RedisRepository(){
        this.host = "localhost";
        this.port = 6379;
        this.controller = new Jedis(this.host, this.port);
        // removeExpiredMessagesRecurrently(checkMinutesInterval);
    }

    /**
     * Construtor da classe RedisRepository.
     * Inicializa o repositório utilizando a porta padrão de conexão.
     * @param host endereço aonde o banco de dados Redis está hospedado (padrão é "localhost")
     */
    public RedisRepository(String host){
        this.host = host;
        this.port = 6379;
        this.controller = new Jedis(host, this.port);
        // removeExpiredMessagesRecurrently(checkMinutesInterval);
    }

    /**
     * Construtor da classe RedisRepository.
     * Inicializa o repositório utilizando o host padrão de conexão.
     * @param port porta que utilizará para conectar ao Redis (o padrão do Redis é 6379)
     */
    public RedisRepository(int port){
        this.host = "localhost";
        this.port = port;
        this.controller = new Jedis(this.host, port);
        // removeExpiredMessagesRecurrently(checkMinutesInterval);
    }

    /**
     * Construtor da classe RedisRepository.
     * Inicializa o repositório configurando os parâmetros de conexão.
     * @param host endereço aonde o banco de dados Redis está hospedado (padrão é "localhost")
     * @param port porta que utilizará para conectar ao Redis (o padrão do Redis é 6379)
     */
    public RedisRepository(String host, int port){
        this.host = host;
        this.port = port;
        this.controller = new Jedis(this.host, port);
        // removeExpiredMessagesRecurrently(checkMinutesInterval);
    }

    // ------------------------------------------------ //
    // -------------------- Getters ------------------- //
    // ------------------------------------------------ //

    /**
     * @return o host ao qual este repositório está conectado
     */
    public String getHost(){
        return this.host;
    }

    /**
     * @return a porta que este repositório utilizou para se conectar ao Redis
     */
    public int getPort(){
        return this.port;
    }

    /**
     * @return o tempo (em segundos) que as mensagens poderão ficar no Redis antes de serem dados como expiradas
     */
    public int getMessagesTTL() {
        return this.messagesTTL;
    }

    /**
     * @return (Cliente Redis) o controlador que permite realizar as operações no banco de dados por meio de acesso direto
     */
    public Jedis getController(){
        return this.controller;
    }
    
    // ------------------------------------------------ //
    // -------------------- Métodos ------------------- //
    // ------------------------------------------------ //

    /**
     * Adiciona uma nova mensagem no final da fila do tópico.
     * Se o tópico não existir, ele será criado e a mensagem adicionada.
     * @param topic (String) nome do tópico (que deve ser único em um broker)
     * @param message (Message) mensagem a ser adicionada (será armazenada no formato json string dentro do Redis)
     */
    @Override
    public void append(String topic, Message message){
        removeExpiredMessages();
        final String id = Generators.timeBasedGenerator().generate().toString();
        final String key = RedisTopicUtils.getTopicKey(topic);
        message.setId(id);
        this.controller.rpush(key, message.toJsonString());
    }

    /**
     * Grava uma mensagem como consumida.
     * Se o tópico não existir ou nenhuma mensagem com o messageId passado for encontrada no tópico, uma exceção será lançada.
     * @param topic (String) nome do tópico (deve existir)
     * @param messageId identificador da mensagem
     */
    @Override
    public void consumeMessage(String topic, UUID messageId) {
        removeExpiredMessages();
        final String topicKey = RedisTopicUtils.getTopicKey(topic);
        if (! this.controller.exists(topicKey))
            throw new UnsupportedOperationException("Topic \"" + topic + "\" not found on database");
        final List<Message> messages = getAllMessagesFromTopic(topicKey);
        final String id = messageId.toString();
        int index = 0;
        for (Message message : messages){
            if (id.equals(message.getId())){
                message.setConsumed(true);
                this.controller.lset(topicKey, index, message.toJsonString());
                return;}
        } throw new UnsupportedOperationException("Message \"" + messageId + "\" not found on topic \"" + topic + "\" on database");
    }

    /**
     * Retorna todas as mensagens ainda não consumidas e não expiradas que estão num tópico.
     * Se um tópico não existir, uma exceção será lançada.
     * @param topic (String) nome do tópico (deve existir)
     * @return uma lista com todas as mensagens não consumidas
     */
    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topic) {
        removeExpiredMessages();
        String topicKey = RedisTopicUtils.getTopicKey(topic);
        if (! this.controller.exists(topicKey))
            throw new UnsupportedOperationException("Topic \"" + topic + "\" not found on database");
        List<Message> messages = new ArrayList<>();
        List<String> messagesStr = this.controller.lrange(topicKey, 0, -1);
        for (String messageStr : messagesStr ){
            Message message = new Message(messageStr);
            if ( message.isConsumed() ) 
                continue;
            messages.add(message);
        } return messages;
    }

    /**
     * Retorna todas as mensagens consumidas em um tópico e que ainda não foram expiradas.
     * Se um tópico não existir, uma exceção será lançada.
     * @param topic (String) nome do tópico (deve existir)
     * @return uma lista com todas as mensagens consumidas
     */
    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topic) {
        removeExpiredMessages();
        String topicKey = RedisTopicUtils.getTopicKey(topic);
        if (! this.controller.exists(topicKey))
            throw new UnsupportedOperationException("Topic \"" + topic + "\" not found on database");
        List<Message> messages = new ArrayList<>();
        List<String> messagesStr = this.controller.lrange(topicKey, 0, -1);
        for (String messageStr : messagesStr ){
            Message message = new Message(messageStr);
            if ( ! message.isConsumed() ) 
                continue;
            messages.add(message);
        } return messages;
    }

    // ------------------------------------------------ //
    // -------------- Métodos Adicionais -------------- //
    // ------------------------------------------------ //

    /**
     * Retorna todos os tópicos criados e ativos no repositório.
     * @return um conjunto com todos os tópicos presentes no repositório
     */
    public Set<String> getCreatedTopics() {
        removeExpiredMessages();
        final String messagesKeysPattern = RedisTopicUtils.getMessagesPattern();
        return this.controller.keys(messagesKeysPattern);
    }

    /**
     * Retorna todos os elementos dentro de uma estrutura de lista armazenada no Redis.
     * @return uma lista com todos os seus elementos armazenados no Redis.
     */
    public List<String> getListElements(String listKey) {
        return this.controller.lrange(listKey, 0, -1);
    }

    /**
     * Verifica a qual tópico no repositório uma mensagem pertence.
     * @param message (Message) a mensagem que se deseja descobrir a qual tópico está atrelada
     * @return o tópico no repositório aonde a mensagem está armazenada
     */
    public String getTopic(Message message) {
        removeExpiredMessages();
        final String jsonStringMessage = message.toJsonString();
        final Set<String> topics = this.getCreatedTopics();
        for (String topic : topics) {
            final List<String> topicStringMessages = this.getListElements(topic);
            if (topicStringMessages.contains(jsonStringMessage)) return topic;
        } return null;
    }
    
    /**
     * Busca uma mensagem no repositório a partir de um id.
     * Se encontrada uma mensagem batendo com o id passado, a mensagem é instanciada como um objeto Java e retornada.
     * @param id (UUID) identificador único da mensagem que deseja obter a partir do repositório
     * @return (Message) a mensagem já convertida para um objeto Java
     */
    public Message getMessageById(UUID id) {
        removeExpiredMessages();
        final Set<String> topics = this.getCreatedTopics();
        for (String topic : topics) {
            final List<String> stringMessages = this.getListElements(topic);
            for (String jsonStringMessage : stringMessages) {
                final Message message = new Message(jsonStringMessage);
                final UUID topicMessageId = UUID.fromString(message.getId());
                boolean messageFound = topicMessageId.equals(id);
                if (messageFound) return message;
            }
        } return null;
    }

    /**
     * Retorna todas as mensagens presentes em um tópico no repositório.
     * @param topicKey a chave do tópico no Redis
     * @return uma lista com todas as mensagens encontradas
     */
    public List<Message> getAllMessagesFromTopic(String topicKey) {
        removeExpiredMessages();
        final List<String> stringMessages = getListElements(topicKey);
        final List<Message> messages = new ArrayList<>();
        for (String stringMessage : stringMessages) {
            messages.add(new Message(stringMessage));
        } return messages;
    }

    /**
     * Remove todas as mensagens que passaram mais tempo no banco de dados do que o desejado.
     * O tempo que a mensagens podem ficar no repositório antes de serem consideradas como expiradas
     * é controlado pelo atributo "messagesTTL" deste repositório.
     */
    public void removeExpiredMessages() {
        final Set<String> topics = this.getCreatedTopics();
        for (String topic : topics) {
            List<String> stringMessages = this.getListElements(topic);
            for (String stringMessage : stringMessages) {
                LocalDateTime timeCreation = RedisMessagesUtils.getJsonCreatedAt(stringMessage);
                int durationOnRepo = RedisMessagesUtils.getDurationOfMessage(timeCreation);
                boolean isExpired = durationOnRepo >= messagesTTL;
                if (isExpired)
                    this.controller.lrem(topic, 1, stringMessage);
            };
        }
    }

    /**
     * Recorrentemente remove as mensagens expiradas no repositório.
     * @param waitMinutes Quantidade de tempo em minutos entre cada verificação no repositório.
     */
    public void removeExpiredMessagesRecurrently(int waitMinutes) {
        Thread secundaryThread = new Thread(() -> {
            while (true) {
                try {
                    removeExpiredMessages();
                    Thread.sleep(waitMinutes * 60 * 1000);
                } catch (InterruptedException  erro) {
                    erro.printStackTrace();
                }
            }
        });
        secundaryThread.setDaemon(true);
        secundaryThread.start();
    }

}