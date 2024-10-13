package br.com.mangarosa;

import java.util.UUID;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Producer;
import br.com.mangarosa.messages.Message;
import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.StreamMessage;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TMessageRepository implements MessageRepository {
    private final RedisClient clienteRedis;
    private final RedisCommands<String, String> comandosRedis;
    // Construtor
    public TMessageRepository(RedisClient clienteRedis, StatefulRedisConnection<String, String> conexao) {
        this.clienteRedis = clienteRedis;
        this.comandosRedis = conexao.sync();
    }

    @Override
    public void append(String topico, Message mensagem) {
        try {
            // Define um UUID para a mensagem
            UUID uuid = UUID.randomUUID();
            mensagem.setId(uuid);

            // Converte a mensagem para um mapa e adiciona o UUID como um campo
            Map<String, String> mapaMensagem = mensagem.toMap();
            mapaMensagem.put("id", uuid.toString());

            // Adiciona a mensagem ao stream
            comandosRedis.xadd(topico, mapaMensagem);

            // Imprime o UUID da mensagem
            System.out.println("Mensagem adicionada ao stream com UUID: " + uuid);

            // Define a expiração da mensagem para 5 minutos (300 segundos)
            long tempoExpiracaoSegundos = 300;
            comandosRedis.expire(topico, tempoExpiracaoSegundos); 
        } catch (Exception e) {
            throw new RuntimeException("Erro ao adicionar a mensagem ao Redis Stream", e);
        }
    }

    @Override
    public void consumeMessage(String topico, UUID idMensagem) {
        try {
            // Lê todas as mensagens disponíveis no stream
            List<StreamMessage<String, String>> mensagens = comandosRedis.xrange(topico, Range.create("-", "+"));

            // Percorre as mensagens do stream
            for (StreamMessage<String, String> dadosMensagem : mensagens) {
                String id = dadosMensagem.getBody().get("id");
                String dadosProdutor = dadosMensagem.getBody().get("producer");
                String textoMensagem = dadosMensagem.getBody().get("message");
                Producer produtor = new TProducer(clienteRedis, dadosProdutor);

                // Cria a mensagem a partir dos dados
                Message mensagem = new Message(produtor, textoMensagem);
                mensagem.setId(UUID.fromString(id));
                mensagem.setConsumed(Boolean.parseBoolean(dadosMensagem.getBody().get("isConsumed")));

                // Verifica se a mensagem corresponde ao UUID desejado
                if (mensagem.getId().equals(idMensagem)) {
                    // Verifica se a mensagem foi consumida
                    if (mensagem.isConsumed()) {
                        throw new RuntimeException("Mensagem já foi consumida."); 
                    }
                    // Marca a mensagem como consumida
                    mensagem.setConsumed(true); 

                    // Atualiza a mensagem como consumida no stream
                    Map<String, String> mapaMensagem = mensagem.toMap();
                    comandosRedis.xadd(topico, mapaMensagem);
                    
                    // Remove a mensagem original do stream
                    comandosRedis.xdel(topico, dadosMensagem.getId()); 
                    return; 
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consumir a mensagem", e); 
        }
    }

    @Override
    public List<Message> getAllNotConsumedMessagesByTopic(String topico) {
        // Lê todas as mensagens do stream para o tópico fornecido
        List<StreamMessage<String, String>> mensagens = comandosRedis.xrange(topico, Range.create("-", "+"));
        List<Message> mensagensNaoConsumidas = new ArrayList<>();
        
        // Adiciona mensagens não consumidas à lista
        for (StreamMessage<String, String> dadosMensagem : mensagens) {
            String id = dadosMensagem.getBody().get("id");
            String dadosProdutor = dadosMensagem.getBody().get("producer");
            String textoMensagem = dadosMensagem.getBody().get("message");
            Producer produtor = new TProducer(clienteRedis, dadosProdutor);

            // Cria a mensagem a partir dos dados
            Message mensagem = new Message(produtor, textoMensagem);
            mensagem.setId(UUID.fromString(id));
            mensagem.setConsumed(Boolean.parseBoolean(dadosMensagem.getBody().get("isConsumed")));

            // Verifica se a mensagem não foi consumida
            if (!mensagem.isConsumed()) {
                mensagensNaoConsumidas.add(mensagem);
            }
        }

        return mensagensNaoConsumidas;
    }

    @Override
    public List<Message> getAllConsumedMessagesByTopic(String topico) {
        // Lê todas as mensagens do stream para o tópico fornecido
        List<StreamMessage<String, String>> mensagens = comandosRedis.xrange(topico, Range.create("-", "+"));
        List<Message> mensagensConsumidas = new ArrayList<>();
        
        // Adiciona mensagens consumidas à lista
        for (StreamMessage<String, String> dadosMensagem : mensagens) {
            String id = dadosMensagem.getBody().get("id");
            String dadosProdutor = dadosMensagem.getBody().get("producer");
            String textoMensagem = dadosMensagem.getBody().get("message");
            Producer produtor = new TProducer(clienteRedis, dadosProdutor);

            // Cria a mensagem a partir dos dados
            Message mensagem = new Message(produtor, textoMensagem);
            mensagem.setId(UUID.fromString(id));
            mensagem.setConsumed(Boolean.parseBoolean(dadosMensagem.getBody().get("isConsumed")));

            // Verifica se a mensagem foi consumida
            if (mensagem.isConsumed()) {
                mensagensConsumidas.add(mensagem);
            }
        }

        return mensagensConsumidas;
    }
}
