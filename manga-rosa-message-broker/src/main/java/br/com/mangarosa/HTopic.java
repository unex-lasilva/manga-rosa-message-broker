package br.com.mangarosa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;

public class HTopic implements Topic{

    private String name;
    private List<Consumer> consumers;
    private MessageRepository repository;
    private RedisMessageRepository Rrepository;
    private Message message;
    
    /**
     * Construtor que inicializa o tópico com um nome.
     * @param name Nome do tópico
     */
    public HTopic(String name){
        this.name = name;
        this.consumers = new ArrayList<>();
    }

    /**
     * Adiciona uma mensagem ao tópico.
     * @param message Mensagem a ser adicionada
     */
    @Override
    public void addMessage(Message message) {
        if (repository == null) {
            throw new IllegalStateException("Repository não foi configurado para este tópico.");
        }
        repository.append(name, message);
    }

    /**
     * Retorna a lista de consumidores inscritos no tópico.
     * @return Lista de consumidores
     */
    @Override
    public List<Consumer> consumers() {
        return new ArrayList<>(consumers);
    }

    /**
     * Retorna o repositório de mensagens associado ao tópico.
     * @return Repositório de mensagens
     */
    @Override
    public MessageRepository getRepository() {
        return repository;
    }

    /**
     * Retorna o nome do tópico.
     * @return Nome do tópico
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * Notifica todos os consumidores inscritos sobre uma nova mensagem.
     * @param message Mensagem a ser notificada
     */
    @Override
    public void notifyConsumers(Message message) {
        for (Consumer consumer : consumers) {
            boolean consumed = consumer.consume(message);
            if (consumed && consumer instanceof HConsumer && Rrepository != null) {
                UUID uuid = UUID.fromString(message.getId());
                Rrepository.consumeMessage(name, uuid);            
            }
        }
    }

    /**
     * Inscreve um consumidor no tópico.
     * @param consumer Consumidor a ser inscrito
     */
    @Override
    public void subscribe(Consumer consumer) {
        if (!consumers.contains(consumer)) {
            consumers.add(consumer);
            if (consumer instanceof HConsumer) {
                ((HConsumer) consumer).setTopic(this);
            }
            if (Rrepository != null) {
                Rrepository.subscribe(name, consumer);
            }
        }
    }

    /**
     * Cancela a inscrição de um consumidor no tópico.
     * @param consumer Consumidor a ter a inscrição cancelada
     */
    @Override
    public void unsubscribe(Consumer consumer) {
        consumers.remove(consumer);
        if (consumer instanceof HConsumer) {
            ((HConsumer) consumer).setTopic(this);
        }
        if (Rrepository != null) {
            Rrepository.unsubscribe(name, consumer);
        }
    }

    /**
     * Define o repositório de mensagens para o tópico.
     * @param repository Repositório de mensagens a ser definido
     */
    public void setRepository(MessageRepository repository) {
        this.repository = Objects.requireNonNull(repository, "Repository não pode ser nulo");
    }

    /**
     * Retorna uma representação em string do tópico.
     * @return String representando o tópico
     */
    @Override
    public String toString() {
        return "HTopic [name=" + name + "| Consumed=" + message.isConsumed() + ", consumers=" + consumers.size() + "]";
    }
}