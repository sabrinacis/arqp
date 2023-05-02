package ar.com.arqdx.queue.manager.producer;

import javax.jms.JMSException;
import javax.jms.Message;

public interface IMQMessageProducer {

    void sendMessage(Message message) throws JMSException;
}
