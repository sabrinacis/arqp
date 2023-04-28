package ar.com.arqdx.queue.manager.interfaces;

import javax.jms.MessageConsumer;

public interface IMQMessageConsumer {
    MessageConsumer getConsumer();
}
