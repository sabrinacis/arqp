package ar.com.arqdx.queue.manager.consumer;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;

public class MQMessageConsumer implements IMQMessageConsumer {
    private MessageConsumer consumer;

    public MQMessageConsumer(MessageConsumer consumer) {
        setConsumer(consumer);
    }

    @Override
    public void consume() throws JMSException {
         this.consumer.receive();
    }
    public MessageConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }
}
