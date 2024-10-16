package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Producer;

/**
 * Classe que representa um produtor de mensagens para o sistema de entrega de alimentos.
 */
public class FoodDeliveryProducer implements Producer {
    /**
     * Implementa o método para produzir uma mensagem.
     *
     * @param topic Tópico para o qual a mensagem será produzida
     * @param message Conteúdo da mensagem
     */
    @Override
    public void produce(String topic, String message) {
        // Aqui você pode definir como deseja produzir a mensagem
        System.out.println("Produzindo mensagem no tópico: " + topic + " com mensagem: " + message);
    }

    @Override
    public void produce(String message) {
    }
}
