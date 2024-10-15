package br.com.mangarosa.consumers;

import br.com.mangarosa.connection.RedisConnection;
import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageRepository;
import io.lettuce.core.api.sync.RedisCommands;

public class LongDistanceItemsConsumer implements Consumer {
// Declara a classe `LongDistanceItemsConsumer`, que implementa a interface `Consumer`

    private MessageRepository repository;
    // Define um repositório de mensagens que será usado para processar as mensagens recebidas

    private final String topicName = "long-distance-items";
    // Define o nome do tópico "long-distance-items" como constante. Esse consumidor estará associado a esse tópico específico

    private final RedisCommands<String, String> syncCommands = RedisConnection.connect();
    // Cria uma conexão síncrona com o Redis para interagir com o banco de dados Redis

    public LongDistanceItemsConsumer() {
    // Construtor da classe. É responsável por inicializar o repositório de mensagens

        this.repository = new MessageRepository();
        // Instancia o `MessageRepository`, que será usado para consumir mensagens

    }

    @Override
    public boolean consume(Message message) {
    // Implementação do método `consume` da interface `Consumer`, que recebe uma mensagem e a processa

        repository.consumeMessage(topicName, message);
        // Chama o método `consumeMessage` no repositório, passando o nome do tópico e a mensagem a ser processada

        return false;
        // Retorna `false`, indicando que a mensagem não foi consumida

    }

    @Override
    public String name() {
    // Implementação do método `name` da interface `Consumer`, que retorna o nome do consumidor

        return "LongDistanceItems";
        // Retorna o nome do consumidor, que é "LongDistanceItems"

    }
}
