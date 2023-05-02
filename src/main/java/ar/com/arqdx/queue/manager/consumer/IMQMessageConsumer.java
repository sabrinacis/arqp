package ar.com.arqdx.queue.manager.interfaces;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

public interface IMQMessageConsumer {
    void consume() throws JMSException;
}
