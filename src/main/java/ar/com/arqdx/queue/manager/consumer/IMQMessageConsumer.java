package ar.com.arqdx.queue.manager.consumer;

import javax.jms.JMSException;

public interface IMQMessageConsumer {
    void consume() throws JMSException;
}
