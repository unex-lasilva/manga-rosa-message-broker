package br.com.mangarosa.obj;

import br.com.mangarosa.interfaces.Consumer;
import br.com.mangarosa.interfaces.MessageRepository;
import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Topico implements Topic
{
    private final List<Message> messages;
    private final String topicName;
    private final boolean created;
    private final List<Consumer> consumers;

    /**
     *
     * @param name nome do tópico
     */
    public Topico(String name) {
        topicName = name;
        messages = new ArrayList<>();
        consumers = new ArrayList<>();
        created = true;
    }

    public boolean isCreated() {
        return created;
    }

    @Override
    public String name() {
        return topicName;
    }

    @Override
    public void addMessage(Message m) {
        try {
            messages.add(m);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void subscribe(Consumer consumer) {
        try {
            consumers.add(consumer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void unsubscribe(Consumer consumer) {
        try {
            consumers.remove(consumer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Consumer> consumers() {
        return consumers;
    }

    @Override
    public MessageRepository getRepository() {
        return null;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
