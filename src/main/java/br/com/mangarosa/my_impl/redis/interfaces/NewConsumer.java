package br.com.mangarosa.my_impl.redis.interfaces;

import java.util.List;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageConsumption;

/**
 * Esta classe é uma interface genérica de consumidor que utiliza o Redis.
 * Ela representa um Consumer que consome mensagens a partir de lá.
 */

public class NewConsumer implements Consumer {
    public String name;

    public NewConsumer(String consumerName) {
        this.name = consumerName;
    }

    /**
     * Consome uma mensagem no Redis, colocando-a como processada.
     * O consumo feito é adicionado aos metadados da mensagem.
     * A operação não é realizada se:
     * (1) A mensagem com o ID não é localizado no banco de dados ou
     * (2) A mensagem já foi consumida por este consumidor.
     * @param message (Message) mensagem a ser consumida
     * @return true: se a mensagem foi consumida adequadamente
     * @return false: se a mensagem não pôde ser consumida, por algum motivo
     */
    @Override
    public boolean consume(Message message) {
        final RedisRepository repository = new RedisRepository();
        final String messageId = message.getId();
        final String topicKey = repository.getTopic(message);
        final List<Message> topicMessages = repository.getAllMessagesFromTopic(topicKey);
        int topicMessageIndex = -1;
        for (Message topicMessage : topicMessages) {
            topicMessageIndex++;
            String topicMessageId = topicMessage.getId();
            boolean messageFound = topicMessageId.equals(messageId);
            boolean messageNotFound = !messageFound;
            boolean messageConsumedByThis = this.consumedTheMessage(message);
            if ( messageNotFound ) continue;
            if ( messageConsumedByThis ) continue;
            message.setConsumed(true);
            message.addConsumption(this);
            repository.getController().lset(topicKey, topicMessageIndex, message.toJsonString());
            return true;
        } return false;
    }

    /**
     * @return o nome deste consumidor
     */
    @Override
    public String name() {
        return this.name;
    }

    /**
     * Este consumidor já consumiu a mensagem passada?
     * @param message (Message) a mensagem a ser verificada
     * @return true/false
     */
    public boolean consumedTheMessage(Message message) {
        for (MessageConsumption consumption : message.getConsumptionList()) {
            if (consumption.getConsumer().name().equals(this.name())) {
                return true;
            }
        } return false;
    }
}