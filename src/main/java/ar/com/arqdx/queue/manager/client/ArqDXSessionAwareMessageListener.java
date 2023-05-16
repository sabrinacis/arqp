package ar.com.arqdx.queue.manager.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;
@Service
public class ArqDXSessionAwareMessageListener implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArqDXSessionAwareMessageListener.class);
    private String id;

    private CountDownLatch latch = new CountDownLatch(1);

    public ArqDXSessionAwareMessageListener(String id) {
        super();
        this.id = id;
    }


    @Override
    public void onMessage(Message message) {

        try {
            Object command = ((ObjectMessage) message).getObject();

            LOGGER.info("ArqDXSessionAwareMessageListener --> onMessage(" + command.toString() + ")");

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public ArqDXSessionAwareMessageListener() {
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}