package ar.com.arqdx.queue.manager.client;


import ar.com.arqdx.queue.manager.ibmmq.configuration.IbmConfiguration;
import ar.com.arqdx.queue.manager.message.ArqDxQueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.jms.*;

@Service
public class ArqDXMessageListener implements MessageListener {//implements IArqDXMessageListener {

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


    private void receiveMessage(MessageConsumer consumer) throws JMSException {

        // Begin to wait for messages.
        final Message consumerMessage = consumer.receive(1000);

        // Receive the message when it arrives.

        Object command = ((ObjectMessage) consumerMessage).getObject();
        System.out.println("Message received: " + command.toString());

        // Clean up the consumer.
        consumer.close();
    }
}
