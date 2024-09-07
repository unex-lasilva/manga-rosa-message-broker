package br.com.mangarosa.interfaces;

import br.com.mangarosa.messages.Message;

public interface Consumer {
    void consume(Message message);
}
