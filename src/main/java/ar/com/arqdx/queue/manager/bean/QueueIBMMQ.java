package ar.com.arqdx.queue.manager.bean;

import ar.com.arqdx.queue.manager.consumer.IMQMessageConsumer;
import ar.com.arqdx.queue.manager.message.IArqDxMessage;
import ar.com.arqdx.queue.manager.producer.IMQMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@Component
@Slf4j
public class QueueIBMMQ implements IQueueIBMMQ {

    private String queueName;

    private String listenerName;

    private Connection connection;

    private JmsListenerContainerFactory jmsListenerContainerFactory;

    private Session session;

    private IMQMessageProducer messageProducer;
    private IMQMessageConsumer messageConsumer;

    public QueueIBMMQ() {
    }

    public QueueIBMMQ(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void sendMessage(IArqDxMessage msg1) throws JMSException {
        log.info("Se envía mensaje");
        ObjectMessage message = session.createObjectMessage();
        message.setObject(msg1);
        messageProducer.sendMessage(message);
    }

    @Override
    public void consume() throws JMSException {
        log.info("Se envía mensaje");
        this.messageConsumer.consume();
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
    @Override
    public String getListenerName() {
        return listenerName;
    }
    @Override
    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public IMQMessageProducer getMessageProducer() {
        return messageProducer;
    }

    @Override
    public void setMessageProducer(IMQMessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @Override
    public IMQMessageConsumer getMessageConsumer() {
        return messageConsumer;
    }

    @Override
    public void setMessageConsumer(IMQMessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public JmsListenerContainerFactory getJmsListenerContainerFactory() {
        return jmsListenerContainerFactory;
    }

    @Override
    public void setJmsListenerContainerFactory(JmsListenerContainerFactory jmsListenerContainerFactory) {
        this.jmsListenerContainerFactory = jmsListenerContainerFactory;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "queueName='" + queueName + '\'' +
                '}';
    }
}
