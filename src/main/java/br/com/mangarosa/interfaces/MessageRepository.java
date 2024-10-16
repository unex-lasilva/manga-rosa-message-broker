package br.com.mangarosa.interfaces;

public interface MessageRepository {
    // Salva uma mensagem no repositório
    void saveMessage(String message);

    // Recupera uma mensagem a partir do repositório
    String getMessage(String id);
}
