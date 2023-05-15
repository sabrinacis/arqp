package ar.com.arqdx.queue.manager.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

//@Service
public class ArqDXMessageListener  implements MessageListener {//implements IArqDXMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArqDXMessageListener.class);

    @Override
    public void onMessage(Message message) {
        try {
            Object command = ((ObjectMessage) message).getObject();

            LOGGER.info("onMessage(" + command.toString()+ ")");

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }


}
