package br.com.mangarosa.messages;


public class Message {
    private String topic;
    private String content;


    public Message(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }


    public String getContent() {
        return content; // Retorna o conteúdo da mensagem
    }


    public String getTopic() {
        return topic; // Retorna o tópico da mensagem
    }
}


