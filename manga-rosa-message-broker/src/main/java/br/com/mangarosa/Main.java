package br.com.mangarosa;

import java.util.UUID;

import br.com.mangarosa.messages.Message;
import br.com.mangarosa.messages.MessageBroker;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {

        /**
         * Cria uma conexão com o Redis para armazenar as mensagens
         */
        RedisMessageRepository repository = new RedisMessageRepository("127.0.0.1", 6379);

        /**
         * Cria tópicos para diferentes tipos de entregas
         */
        HTopic topicoEntregaRapida = new HTopic("queue/fast-delivery-items");
        HTopic topicoLongaDistancia = new HTopic("queue/long-distance-items");

        /**
         * Configura o repositório Redis para cada tópico
         */
        topicoEntregaRapida.setRepository(repository);
        topicoLongaDistancia.setRepository(repository);

        /**
         * Cria produtores para diferentes tipos de serviços
         */
        HProducer produtorEntregaComida = new HProducer("FoodDeliveryProducer");
        HProducer produtorEntregaPessoa = new HProducer("PhysicalPersonDeliveryProducer");
        HProducer produtorMarketplace = new HProducer("PyMarketPlaceProducer");
        HProducer produtorEntregaRapida = new HProducer("FastDeliveryProducer");

        /**
         * Cria consumidores para diferentes tipos de entregas
         */
        HConsumer consumidorEntregaRapida = new HConsumer("FastDeliveryConsumer");
        HConsumer consumidorLongaDistancia = new HConsumer("LongDistanceConsumer");

        /**
         * Associa produtores aos tópicos correspondentes
         */
        produtorEntregaComida.addTopic(topicoEntregaRapida);
        produtorEntregaPessoa.addTopic(topicoEntregaRapida);
        produtorMarketplace.addTopic(topicoLongaDistancia);
        produtorEntregaRapida.addTopic(topicoLongaDistancia);

        /**
         * Inscreve consumidores nos tópicos correspondentes
         */
        topicoEntregaRapida.subscribe(consumidorEntregaRapida);
        topicoLongaDistancia.subscribe(consumidorLongaDistancia);

        /**
         * Cria mensagens para diferentes tipos de entregas
         */
        Message messageEntregaComida = new Message(produtorEntregaRapida, "Pedido de pizza para entrega rápida");
        Message messageEntregaPessoa = new Message(produtorEntregaRapida, "Entrega de documento urgente");
        Message messageMarketplace = new Message(produtorEntregaRapida, "Pedido de eletrônico para entrega em outra cidade");
        Message messageEntregaRapida = new Message(produtorEntregaRapida, "Pacote expresso para entrega imediata");

        /**
         * Adiciona mensagens aos tópicos correspondentes
         */
        topicoEntregaRapida.addMessage(messageEntregaComida);
        topicoEntregaRapida.addMessage(messageEntregaPessoa);
        topicoLongaDistancia.addMessage(messageMarketplace);
        topicoLongaDistancia.addMessage(messageEntregaRapida);

        /**
         * Inicia o temporizador para remoção de mensagens expiradas
         */
        repository.Time(topicoEntregaRapida.name());
        repository.Time(topicoLongaDistancia.name());

        /**
         * Imprime mensagens não consumidas para cada tópico
         */
        System.out.println("\nMenssagem Não Consumidas:");
        System.out.println("\nMensagens no tópico de entrega rápida:");
        for (Message msg : repository.getAllNotConsumedMessagesByTopic(topicoEntregaRapida.name())) {
            System.out.println(msg.getMessage());
        }

        System.out.println("\nMensagens no tópico de longa distância:");
        for (Message msg : repository.getAllNotConsumedMessagesByTopic(topicoLongaDistancia.name())) {
            System.out.println(msg.getMessage());
        }

        /**
         * Consome as mensagens
         */
        // consumidorEntregaRapida.consume(messageEntregaComida);
        // consumidorEntregaRapida.consume(messageEntregaPessoa);
        // consumidorLongaDistancia.consume(messageMarketplace);
        // consumidorLongaDistancia.consume(messageEntregaRapida);

        /**
         * Imprime mensagens consumidas para cada tópico
         */
        System.out.println("\nMenssagem Consumidas:");
        System.out.println("\nMensagens no tópico de entrega rápida:");
        for (Message msg : repository.getAllConsumedMessagesByTopic(topicoEntregaRapida.name())) {
            System.out.println(msg.getMessage());
            System.out.println(msg.isConsumed());
        }

        System.out.println("\nMensagens no tópico de longa distância:");
        for (Message msg : repository.getAllConsumedMessagesByTopic(topicoLongaDistancia.name())) {
            System.out.println(msg.getMessage());
        }

        /**
         * Mantém o programa em execução
         */
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
