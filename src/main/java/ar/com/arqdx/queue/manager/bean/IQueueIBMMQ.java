package ar.com.arqdx.queue.manager.bean;

import ar.com.arqdx.queue.manager.consumer.IMQMessageConsumer;
import ar.com.arqdx.queue.manager.message.IArqDxMessage;
import ar.com.arqdx.queue.manager.producer.IMQMessageProducer;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public interface IQueueIBMMQ {

    String getQueueName();

    void sendMessage(IArqDxMessage message) throws JMSException;

    void consume() throws JMSException;

    void setSession(Session session);

    Session getSession();

    IMQMessageProducer getMessageProducer();

    void setMessageProducer(IMQMessageProducer messageProducer);

    IMQMessageConsumer getMessageConsumer();

    void setMessageConsumer(IMQMessageConsumer messageConsumer);

    Connection getConnection();

    void setConnection(Connection connection);

    JmsListenerContainerFactory getJmsListenerContainerFactory();
    void setJmsListenerContainerFactory(JmsListenerContainerFactory jmsListenerContainerFactory);

}
