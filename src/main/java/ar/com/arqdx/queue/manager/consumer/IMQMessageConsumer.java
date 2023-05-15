package ar.com.arqdx.queue.manager.consumer;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface IMQMessageConsumer {
    void consume() throws JMSException;

    MessageListener getMessageListener() throws JMSException;

    DefaultMessageListenerContainer getMessageListenerContainer();
}
