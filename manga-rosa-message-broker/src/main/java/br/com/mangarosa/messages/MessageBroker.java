package br.com.mangarosa.messages;

import br.com.mangarosa.topics.Topic;
import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.ITopic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MessageBroker {

    private final Map<String, Topic> topics;   
    // Mapa que armazena todos os tópicos, utilizando o nome do tópico como chave.
    private final MessageRepository repository;    
    // Repositório responsável por gerenciar as mensagens não consumidas.
    private final ScheduledExecutorService scheduleAtFixedRate;
    // Serviço executor agendado para tarefas periódicas com um pool de 5 threads.

    public MessageBroker(){
    // Construtor da classe, inicializa o mapa de tópicos, o repositório e o executor de tarefas.

        this.topics = new HashMap<>();
        this.repository = new MessageRepository();
        this.scheduleAtFixedRate = Executors.newScheduledThreadPool(5);
    }

    public void createTopic(Topic topic){
        if(topics.containsKey(topic.name()))  // Verifica se o tópico já existe no mapa de tópicos.
            throw new IllegalArgumentException("The topic name already exists");
        this.topics.put(topic.name(), topic);
        // Adiciona o tópico ao mapa de tópicos, associando o nome do tópico à instância.

    }

    public void removeTopic(String topic){
        if(!topics.containsKey(topic)) 
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        ITopic t = this.topics.get(topic);
        t.consumers().forEach(t::unsubscribe);
        // Desinscreve todos os consumidores desse tópico.
        topics.remove(topic);
        // Remove o tópico do mapa de tópicos.

    }

    public void subscribe(String topic, Consumer consumer){
        if(!topics.containsKey(topic)) 
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        ITopic t = this.topics.get(topic);
        t.subscribe(consumer);
        // Inscreve o consumidor no tópico.

    }

    public void unsubscribe(String topic, Consumer consumer){
        if(!topics.containsKey(topic))
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        ITopic t = this.topics.get(topic);
        t.unsubscribe(consumer);
        // Desinscreve o consumidor do tópico.

    }

    public ITopic getTopicByName(String topic){
        if(!topics.containsKey(topic))
            throw new IllegalArgumentException("The topic name does not exist, please make sure you are sending the correct key name");
        return this.topics.get(topic);
    }

    public void notifyConsumers(){
              
            for (String topicName : topics.keySet()) {
            // Itera sobre todos os tópicos no mapa.
                System.out.println(topicName);

                List<Message> messages = repository.getAllNotConsumedMessagesByTopic(topicName);
                // Busca todas as mensagens não consumidas do tópico no repositório.

                System.out.println(messages.isEmpty());
                System.out.println(messages.size());

                if (!messages.isEmpty()) {
                    for (Message message : messages) {
                        // Obtém o tópico correspondente ao nome
                        Topic topic = topics.get(topicName);

                        // Para cada consumidor do tópico, consome a mensagem
                        for (Consumer consumer : topic.consumers()) {
                            System.out.println(consumer.name());
                            message.getConsumptionList().add(consumer.name());
                            consumer.consume(message);

                        }
                    }
                }
            }
    }
}
