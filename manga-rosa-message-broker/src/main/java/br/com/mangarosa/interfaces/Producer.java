package br.com.mangarosa.interfaces;

public interface Producer {
    void addTopic(Topic topic);
    void removeTopic(Topic topic);
    void sendMessage(String message);
    String name();
}
