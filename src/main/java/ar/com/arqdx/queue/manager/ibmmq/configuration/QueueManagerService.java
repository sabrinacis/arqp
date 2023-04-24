package ar.com.arqdx.queue.manager.ibmmq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.Properties;
@Service
@EnableJms
@Slf4j
public class QueueManagerService {
    @Autowired
    Properties properties;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String queueName, String message){
        try{
            jmsTemplate.convertAndSend(queueName, message);

            log.info("Mensaje enviado");

        }catch(JmsException ex){
            log.info("*** Error en envío de mensaje ***");
            ex.printStackTrace();
        }
    }

    public void consume(String queueName){
        try{
            jmsTemplate.receive(queueName);

            log.info("Mensaje desencolado");

        }catch(JmsException ex){
            log.info("*** Error en envío de mensaje ***");
            ex.printStackTrace();
        }
    }

}
