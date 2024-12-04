package br.com.mangarosa.queue.longdistanceitems;

import br.com.mangarosa.interfaces.Topic;
import br.com.mangarosa.interfaces.TopicImpl;
import br.com.mangarosa.messages.Message;

// Classe que estende TopicImpl
abstract class TopicImplConcrete extends TopicImpl {

    // Recebe o nome do tópico e chama o construtor da classe pai (TopicImpl)
    public TopicImplConcrete(String name) {
        super(name);
    }


}
