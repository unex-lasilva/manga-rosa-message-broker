package br.com.mangarosa.interfaces;

 //Interface que representa um produtor de mensagens.
 //Qualquer classe que produzir mensagens deve implementar essa interface.

public interface Producer {
    // Produz uma mensagem para um tópico específico
    void produce(String topic, String message);

    // Produz uma mensagem sem especificar o tópico
    void produce(String message);
}

