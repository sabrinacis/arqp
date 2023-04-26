package ar.com.arqdx.queue.manager.service;

import ar.com.arqdx.queue.manager.ibmmq.configuration.IBMMQFactoryList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;

@Service
@EnableJms
@Slf4j
public class IBMMQManagerService implements IQueueManagerService {


    @Autowired
    IBMMQFactoryList iBMMQFactoryList;

    @Override
    public void send(String queueName, String message) throws JMSException {
        try {
            iBMMQFactoryList.getFactoryList().get(0).createConnection();

        //    jmsTemplate.convertAndSend(queueName, message);

            log.info("Mensaje enviado");

        } catch (JmsException ex) {
            log.info("*** Error en envío de mensaje ***");
            ex.printStackTrace();
        }
    }

    @Override
    public void consume(String queueName) throws JMSException {
        try {

            iBMMQFactoryList.getFactoryList().get(0).createConnection();
        //    jmsTemplate.receive(queueName);

            log.info("Mensaje desencolado");

        } catch (JmsException ex) {
            log.info("*** Error en envío de mensaje ***");
            ex.printStackTrace();
        }
    }

}
