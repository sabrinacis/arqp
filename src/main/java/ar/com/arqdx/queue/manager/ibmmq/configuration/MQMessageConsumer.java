package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.interfaces.IMQMessageConsumer;

import javax.jms.MessageConsumer;

public class MQMessageConsumer implements IMQMessageConsumer {
    private MessageConsumer consumer;

    public MQMessageConsumer(MessageConsumer consumer) {
        setConsumer(consumer);
    }


    public MessageConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(MessageConsumer consumer) {
        this.consumer = consumer;
    }
}
