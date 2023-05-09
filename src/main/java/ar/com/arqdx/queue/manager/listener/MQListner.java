package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Component
public class MQListner   {

    @Autowired
    @Qualifier("broker0.queue0")
    private IQueueIBMMQ queueIBMMQ;


    public MQListner(IQueueIBMMQ queueIBMMQ) {
        this.queueIBMMQ = queueIBMMQ;

    }

  // @JmsListener(destination = "DEV.QUEUE.VALUES")
  //  @JmsListener(destination = "DEV.QUEUE.VALUES", containerFactory = "#{@queueIBMMQ.getJmsListenerContainerFactory()}")
    @JmsListener(destination = "#{@queueIBMMQ.getQueueName()}", containerFactory = "#{@queueIBMMQ.getJmsListenerContainerFactory()}")
    public void receive1(Message msg) throws JMSException {
        System.out.println("MENSAJE RECIBIDO DE "+queueIBMMQ.getQueueName()+" --> " + msg );
     }
  //  @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

 //   @JmsListener(destination = "DEV.QUEUE.VALUES2")
    public boolean receive2(String msg) throws JMSException {
        System.out.println("DEV.QUEUE.VALUES2 msg2 :: " + msg);
        return msg != null;
    }

  //  @JmsListener(containerFactory = "jmsListenerContainerFactory",            destination = "${messaging.destinations.example}")
    public boolean listen(String message) {
        return message != null;
    }
}