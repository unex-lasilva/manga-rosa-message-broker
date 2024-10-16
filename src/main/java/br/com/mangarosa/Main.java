package br.com.mangarosa;

import br.com.mangarosa.obj.MessageConsumption;  // Import correto
import br.com.mangarosa.obj.LongDistanceConsumer;  // Outro exemplo de import do obj
import br.com.mangarosa.obj.PyMarketPlaceProducer;  // Import do novo produtor
import redis.clients.jedis.Jedis;  // Import da biblioteca Jedis para conectar ao Redis

public class Main {

    public static void main(String[] args) {
        // Conectando ao Redis
        Jedis jedis = new Jedis("localhost",6379);  // Usar o host e a porta correta

        // Criando o produtor para PyMarketPlace
        PyMarketPlaceProducer pyMarketPlaceProducer = new PyMarketPlaceProducer(jedis);

        // Produzindo uma mensagem"
        String mensagemMarketplace = "{\"type\": \"marketplace\", \"order\": \"#98765\"}";
        pyMarketPlaceProducer.produce(mensagemMarketplace);

        // Consumo de mensagem usando LongDistanceConsumer
        LongDistanceConsumer longDistanceConsumer = new LongDistanceConsumer();  // Instanciando o consumidor
        MessageConsumption messageConsumption = new MessageConsumption(longDistanceConsumer);  // Conectando o consumidor ao fluxo de processamento

        // Processando a mensagem (pode ser a mensagem que foi produzida acima)
        messageConsumption.processMessage(mensagemMarketplace);  // Você pode usar qualquer outra mensagem ou a mesma que foi produzida
    }
}




