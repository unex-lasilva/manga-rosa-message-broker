package br.com.mangarosa.messages;

import br.com.mangarosa.interfaces.Consumer;

import java.time.LocalDateTime;

/**
 * Esta classe representa o consumo de uma mensagem por um consumer.
 * As instâncias criadas são imutáveis.
 * Assim que os valores são inicializados, eles não podem mais ser editados, somente recuperados.
 *
 * Esta classe adiciona um novo construtor em cima da base criada pelo professor.
 * O novo construtor permite a atribuição manual do horário em que a mensagem foi consumida.
 * (Útil para trabalhar com mensagens obtidas a partir do Redis).
 * @author Denilson Santos (fez modificações em cima da classe-base)
 * @author Professor Lucas Oliveira (criou a classe-base)
 */

public class MessageConsumption {
    /** Quem consumiu a mensagem. */
    private final Consumer consumer;
    /** Horário em que a mensagem foi consumida. */
    private final LocalDateTime consumedAt;

    // ------------------------------------------------ //
    // ----------------- Construtores ----------------- //
    // ------------------------------------------------ //

    
    /**
     * Construtor da classe MessageConsumption.
     * Atribui automaticamente o horário de consumo da mensagem
     * (utiliza o horário em que a instanciação foi feita)
     * @param consumer (Consumer) Quem consumiu a mensagem.
     */
    public MessageConsumption(Consumer consumer){
        this.consumer = consumer;
        this.consumedAt = LocalDateTime.now();
    }

    /**
     * Construtor da classe MessageConsumption.
     * Permite o fornecimento manual do horário de consumo da mensagem
     * @param consumer (Consumer) Quem consumiu a mensagem.
     * @param consumedAt Horário em que a mensagem foi consumida pelo consumer.
     */
    public MessageConsumption(Consumer consumer, LocalDateTime consumedAt){
        this.consumer = consumer;
        this.consumedAt = consumedAt;
    }

    // ------------------------------------------------ //
    // ------------------- Getters -------------------- //
    // ------------------------------------------------ //

    /**
     * @return (Consumer) quem consumiu a mensagem
     */
    public Consumer getConsumer() {
        return consumer;
    }

/**
     * @return o horário em que a mensagem foi consumida
     */
    public LocalDateTime getConsumedAt() {
        return consumedAt;
    }
}
