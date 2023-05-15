package ar.com.arqdx.queue.manager.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.concurrent.CountDownLatch;

public class ArqDXMessageListener  implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArqDXMessageListener.class);
    private String id;

    private CountDownLatch latch = new CountDownLatch(1);

    public ArqDXMessageListener(String id) {
        super();
        this.id = id;
    }
    @Override
    public void onMessage(Message message) {
        try {
            Object command = ((ObjectMessage) message).getObject();

            LOGGER.info("--> onMessage(" + command.toString()+ ")");

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
