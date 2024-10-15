package br.com.mangarosa.consumers;

import br.com.mangarosa.connection.RedisConnection;
import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageRepository;
import io.lettuce.core.api.sync.RedisCommands;

public class FoodDeliveryConsumer implements br.com.mangarosa.interfaces.Consumer {
// Declara a classe `FoodDeliveryConsumer` que implementa a interface `Consumer`

    private MessageRepository repository;
    // Declara um repositório de mensagens que será usado para consumir as mensagens recebidas

    private final String topicName = "fast-delivery-items";
    // Define o nome do tópico de mensagens como uma constante (final), indicando que esse consumidor está associado a esse tópico específico

    private final RedisCommands<String, String> syncCommands = RedisConnection.connect();
    // Cria uma conexão síncrona com o Redis para interagir com o banco de dados Redis

    public FoodDeliveryConsumer() {
    // Construtor da classe, inicializa o repositório de mensagens

        this.repository = new MessageRepository();
        // Instancia um novo `MessageRepository` para consumir as mensagens

    }

    @Override
    public boolean consume(Message message) {
    // Implementação do método `consume` da interface `Consumer`. Ele recebe uma mensagem do tipo `Message` e processa ela.

        repository.consumeMessage(topicName, message);
        // Chama o método `consumeMessage` do repositório, passando o nome do tópico e a mensagem a ser processada

        return false;
        // Retorna `false`, indicando que a mensagem não foi consumida

    }

    @Override
    public String name() {
    // Implementação do método `name` da interface `Consumer`, que retorna o nome do consumidor

        return "FoodDeliveryConsumer";
        // Retorna o nome do consumidor, que é "FoodDeliveryConsumer"

    }
}

