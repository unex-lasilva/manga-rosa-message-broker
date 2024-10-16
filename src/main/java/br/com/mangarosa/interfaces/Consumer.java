package br.com.mangarosa.interfaces;

public interface Consumer {
    // Consome uma mensagem e retorna o conteúdo consumido
    String consume(String message);

    // Retorna o nome do consumidor
    String getName();
}

