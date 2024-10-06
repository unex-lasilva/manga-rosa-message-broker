package br.com.mangarosa.messages;

import redis.clients.jedis.Jedis;

public class MessageBroker {
    private Jedis jedis; // Conexão com o Redis

    // Construtor que estabelece a conexão com o Redis
    public MessageBroker() {
        // Conectando ao Redis usando o URI adequado
        this.jedis = new Jedis("redis://localhost:6379");
    }

    // Método para adicionar uma mensagem a um tópico
    public void pushMessage(String topic, String message) {
        jedis.lpush(topic, message); // Adiciona a mensagem no início da lista do tópico
    }

    // Método para recuperar uma mensagem de um tópico
    public String popMessage(String topic) {
        return jedis.rpop(topic); // Remove e retorna a última mensagem da lista do tópico
    }
}
