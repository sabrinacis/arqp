package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.message.QueueMessageTX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

@Component
public class MQListner {

    @Autowired
    @Qualifier("broker0.queue0")
    private IQueueIBMMQ queueIBMMQ;

    @JmsListener(destination = "DEV.QUEUE.VALUES")
    public void receive1(Message msg) throws JMSException {
        System.out.println("MENSAJE RECIBIDO DE " + queueIBMMQ.getQueueName() + " --> " + msg);

        Object command = ((ObjectMessage) msg).getObject();
        System.out.println("MENSAJE " + queueIBMMQ.getQueueName() + " --> " + command);

    }


}