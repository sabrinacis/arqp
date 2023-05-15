package ar.com.arqdx.queue.manager.consumer;

import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class MQMessageConsumer implements IMQMessageConsumer {

    private MessageConsumer consumer;

    private DefaultMessageListenerContainer messageListenerContainer;

    public MQMessageConsumer(MessageConsumer c1) {
        setConsumer(c1);
    }

    public MQMessageConsumer(MessageConsumer consumer, DefaultMessageListenerContainer messageListenerContainer) {
        this.consumer = consumer;
        this.messageListenerContainer = messageListenerContainer;
    }

    @Override
    public void consume() throws JMSException {
        this.consumer.receive();
    }

    @Override
    public MessageListener getMessageListener() throws JMSException {
        return this.consumer.getMessageListener();
    }


    public MessageConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }
    @Override
    public DefaultMessageListenerContainer getMessageListenerContainer() {
        return messageListenerContainer;
    }

    public void setMessageListenerContainer(DefaultMessageListenerContainer messageListenerContainer) {
        this.messageListenerContainer = messageListenerContainer;
    }
}
