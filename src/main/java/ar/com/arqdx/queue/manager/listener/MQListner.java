package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.DxAnnotationConsumer;
import ar.com.arqdx.queue.manager.annotation.DxAnnotationJmsListener;
import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
@DxAnnotationConsumer
@Component
public class MQListner {

    @Autowired
    @Qualifier("broker0.queue0")
    private IQueueIBMMQ queue0;

    @Autowired
    @Qualifier("broker0.queue1")
    private IQueueIBMMQ queue1;

 //   @JmsListener(destination = "DEV.QUEUE.VALUES")
    @DxAnnotationJmsListener(destination = "DEV.QUEUE.VALUES")
    public void receive1(Message msg) throws JMSException {
        System.out.println("MENSAJE RECIBIDO DE " + queue0.getQueueName() + " --> " + msg);

        Object command = ((ObjectMessage) msg).getObject();
        System.out.println("MENSAJE " + queue0.getQueueName() + " --> " + command);

    }

    @DxAnnotationJmsListener(destination = "DEV.QUEUE.VALUES2")
    public void receive2(Message msg) throws JMSException {
        System.out.println("MENSAJE RECIBIDO DE " + queue1.getQueueName() + " --> " + msg);

        Object command = ((ObjectMessage) msg).getObject();
        System.out.println("MENSAJE " + queue1.getQueueName() + " --> " + command);

    }


}