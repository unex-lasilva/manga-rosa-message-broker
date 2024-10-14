package br.com.mangarosa;

import java.util.UUID;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class HConsumer implements Consumer {

    private String name;
    private RedisMessageRepository repository;
    private Topic topic;
    private Message message;

    /**
     * Construtor que inicializa o consumidor com um nome.
     * @param name Nome do consumidor
     */
    public HConsumer(String name){
        this.name = name;
    };

    /**
     * Consome uma mensagem, marcando-a como consumida no Redis.
     * @param message Mensagem a ser consumida
     * @return true se a mensagem foi consumida com sucesso, false caso contrário
     */
    @Override
    public boolean consume(Message message) {
        // System.out.println("\n Consumer " + name + " consumed message: " + message.getMessage());
        
        if (topic != null && topic.getRepository() instanceof RedisMessageRepository) {
            RedisMessageRepository repository = (RedisMessageRepository) topic.getRepository();
            String topicName = topic.name();
            UUID uuid = UUID.fromString(message.getId());
            
            if (message.getId() != null) {
                try {
                    repository.consumeMessage(topicName, uuid);
                    message.setConsumed(true);
                    System.out.println("Mensagem marcada como consumida no Redis: " + message.getId());                    
                    return true;
                } catch (Exception e) {
                    System.err.println("Erro ao marcar mensagem como consumida: " + e.getMessage());
                    return false;
                }
            } else {
                System.err.println("ID da mensagem é nulo.");
                return false;
            }
        } else {
            System.err.println("Tópico não configurado ou repositório não é RedisMessageRepository.");
            return false;
        }
    }


      // Método para associar o consumidor a um tópico
    public void setTopic(Topic topic) {
        this.topic = topic;
    }



    /**
     * Retorna o nome do consumidor.
     * @return Nome do consumidor
     */
    @Override
    public String name() {
        return name;
    }
    
}
