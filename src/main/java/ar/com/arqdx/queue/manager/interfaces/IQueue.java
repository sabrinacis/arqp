package ar.com.arqdx.queue.manager.interfaces;

import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

public interface IQueue {

    String getQueueName();

    void sendMessage(String message) throws JMSException;

    void consume() throws JMSException;

    QueueManagerService getQueueManagerService();

    void setQueueManagerService(QueueManagerService queueManagerService);

    void setiBMMQManagerService(IBMMQManagerService iBMMQManagerService);

    void setSession(Session session);

    Session getSession();

    IMQMessageProducer getMessageProducer();

    void setMessageProducer(IMQMessageProducer messageProducer);

    IMQMessageConsumer getMessageConsumer();

    void setMessageConsumer(IMQMessageConsumer messageConsumer);

    Connection getConnection();

    void setConnection(Connection connection);

}
