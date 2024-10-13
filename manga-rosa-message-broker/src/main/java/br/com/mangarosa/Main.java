package br.com.mangarosa;

import java.util.List;
import java.util.Scanner;

import br.com.mangarosa.messages.Message;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class Main {
    public static void main(String[] args) {
        // Inicialização do cliente Redis
        RedisClient redis = RedisClient.create("redis://localhost:6379");

        // Conexão com o Redis
        StatefulRedisConnection<String, String> redisConectado = redis.connect();

        try {
            // Repositório de mensagens
            TMessageRepository messageRepository = new TMessageRepository(redis, redisConectado);

            // Criação dos tópicos
            TTopic fastDeliveryTopic = new TTopic("queue/fast-delivery-items", messageRepository);
            TTopic longDeliveryTopic = new TTopic("queue/long-distance-items", messageRepository);

            // Criação do producer e inscrição no tópico
            TProducer fastDeliveryProducer = new TProducer(redis, "FastDelivery");
            fastDeliveryProducer.addTopic(longDeliveryTopic);

            TProducer pyMarketPlaceProducer = new TProducer(redis, "PyMarketPlace");
            pyMarketPlaceProducer.addTopic(longDeliveryTopic);

            TProducer physicPersonDeliveryProducer = new TProducer(redis, "PhysicPersonDelivery");
            physicPersonDeliveryProducer.addTopic(fastDeliveryTopic);

            TProducer foodDeliveryProducer = new TProducer(redis, "FoodDelivery");
            foodDeliveryProducer.addTopic(fastDeliveryTopic);

            // Criação de consumidores e inscrição nos tópicos
            TConsumer fastConsumer = new TConsumer("ConsumidorRapido");
            fastDeliveryTopic.subscribe(fastConsumer);

            TConsumer longDistanceConsumer = new TConsumer("ConsumidorLongo");
            longDeliveryTopic.subscribe(longDistanceConsumer);

            // Envio de mensagens com conteúdo variado
            fastDeliveryProducer.sendMessage("Item para entrega rápida em área urbana.");
            pyMarketPlaceProducer.sendMessage("Pedido de entrega para uma região distante.");
            physicPersonDeliveryProducer.sendMessage("Entrega rápida para cliente pessoa física.");
            foodDeliveryProducer.sendMessage("Entrega de refeição para cliente local.");

            // Imprime as mensagens consumidas do tópico fast-delivery-items
            List<Message> consumedMessagesFast = messageRepository
                    .getAllConsumedMessagesByTopic("queue/fast-delivery-items");
            System.out.println("╔════════════════════════════════════════════════════════════");
            System.out.println("║ Mensagens consumidas do tópico: queue/fast-delivery-items");
            System.out.println("╚════════════════════════════════════════════════════════════");
            if (consumedMessagesFast.isEmpty()) {
                System.out.println("║ Não há mensagens consumidas.");
            } else {
                for (Message message : consumedMessagesFast) {
                    System.out.println("╔═══════════════════════════════════════════════════════════╗");
                    System.out.println("║ ID: " + message.getId());
                    System.out.println("║ Mensagem: " + message.getMessage());
                    System.out.println("║ Consumido: " + (message.isConsumed() ? "Sim" : "Não"));
                    System.out.println("╚═══════════════════════════════════════════════════════════╝");
                }
            }

            // Imprime as mensagens não consumidas do tópico fast-delivery-items
            List<Message> notConsumedMessagesFast = messageRepository
                    .getAllNotConsumedMessagesByTopic("queue/fast-delivery-items");
            System.out.println("╔════════════════════════════════════════════════════════════");
            System.out.println("║ Mensagens não consumidas do tópico: queue/fast-delivery-items");
            System.out.println("╚════════════════════════════════════════════════════════════");
            if (notConsumedMessagesFast.isEmpty()) {
                System.out.println("║ Não há mensagens não consumidas.");
            } else {
                for (Message message : notConsumedMessagesFast) {
                    System.out.println("╔═══════════════════════════════════════════════════════════╗");
                    System.out.println("║ ID: " + message.getId());
                    System.out.println("║ Mensagem: " + message.getMessage());
                    System.out.println("║ Consumido: " + (message.isConsumed() ? "Sim" : "Não"));
                    System.out.println("╚═══════════════════════════════════════════════════════════╝");
                }
            }

            // Imprime as mensagens consumidas do tópico long-distance
            List<Message> consumedMessagesLong = messageRepository
                    .getAllConsumedMessagesByTopic("queue/long-distance-items");
            System.out.println("╔════════════════════════════════════════════════════════════");
            System.out.println("║ Mensagens consumidas do tópico: queue/long-distance-items");
            System.out.println("╚════════════════════════════════════════════════════════════");
            if (consumedMessagesLong.isEmpty()) {
                System.out.println("║ Não há mensagens consumidas.");
            } else {
                for (Message message : consumedMessagesLong) {
                    System.out.println("╔═══════════════════════════════════════════════════════════╗");
                    System.out.println("║ ID: " + message.getId());
                    System.out.println("║ Mensagem: " + message.getMessage());
                    System.out.println("║ Consumido: " + (message.isConsumed() ? "Sim" : "Não"));
                    System.out.println("╚═══════════════════════════════════════════════════════════╝");
                }
            }

            // Imprime as mensagens não consumidas do tópico long-distance
            List<Message> notConsumedMessagesLong = messageRepository
                    .getAllNotConsumedMessagesByTopic("queue/long-distance-items");
            System.out.println("╔════════════════════════════════════════════════════════════");
            System.out.println("║ Mensagens não consumidas do tópico: queue/long-distance-items");
            System.out.println("╚════════════════════════════════════════════════════════════");
            if (notConsumedMessagesLong.isEmpty()) {
                System.out.println("║ Não há mensagens não consumidas.");
            } else {
                for (Message message : notConsumedMessagesLong) {
                    System.out.println("╔═══════════════════════════════════════════════════════════╗");
                    System.out.println("║ ID: " + message.getId());
                    System.out.println("║ Mensagem: " + message.getMessage());
                    System.out.println("║ Consumido: " + (message.isConsumed() ? "Sim" : "Não"));
                    System.out.println("╚═══════════════════════════════════════════════════════════╝");
                }
            }

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nEscolha um produtor para enviar uma mensagem:");
                System.out.println("1 - FastDelivery");
                System.out.println("2 - PyMarketPlace");
                System.out.println("3 - PhysicPersonDelivery");
                System.out.println("4 - FoodDelivery");
                System.out.println("0 - Sair");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer

                if (choice == 0) {
                    break; // Sai do loop
                }

                System.out.println("Digite sua mensagem:");
                String messageContent = scanner.nextLine();

                // Envio de mensagens conforme o produtor selecionado
                switch (choice) {
                    case 1:
                        fastDeliveryProducer.sendMessage(messageContent);
                        break;
                    case 2:
                        pyMarketPlaceProducer.sendMessage(messageContent);
                        break;
                    case 3:
                        physicPersonDeliveryProducer.sendMessage(messageContent);
                        break;
                    case 4:
                        foodDeliveryProducer.sendMessage(messageContent);
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }

        } finally {
            // Fechamento seguro da conexão com Redis e encerramento do cliente
            redisConectado.close();
            redis.shutdown();
        }
    }
}
