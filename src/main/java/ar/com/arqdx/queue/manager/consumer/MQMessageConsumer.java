package ar.com.arqdx.queue.manager.consumer;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

public class MQMessageConsumer implements IMQMessageConsumer {

    private MessageConsumer consumer;

    public MQMessageConsumer(MessageConsumer c1) {
        setConsumer(c1);
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
}
