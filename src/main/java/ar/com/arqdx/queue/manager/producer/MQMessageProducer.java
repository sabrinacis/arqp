package ar.com.arqdx.queue.manager.producer;

import ar.com.arqdx.queue.manager.message.IArqDxMessage;
import ar.com.arqdx.queue.manager.producer.IMQMessageProducer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

public class MQMessageProducer implements IMQMessageProducer {
    private MessageProducer producer;

    public MQMessageProducer(MessageProducer producer) {
        setProducer(producer);
    }

    @Override
    public void sendMessage(Message message) throws JMSException {
        producer.send(message);
    }


    public MessageProducer getProducer() {
        return producer;
    }

    public void setProducer(MessageProducer producer) {
        this.producer = producer;
    }
}
