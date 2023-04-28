package ar.com.arqdx.queue.manager.interfaces;

import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;

import javax.jms.Connection;
import javax.jms.Session;

public interface IQueue {

    String getQueueName();

    void sendMessage(String message);

    void consume();

    QueueManagerService getIbmMQQueueService();

    void setIbmMQQueueService(QueueManagerService ibmMQQueueService);

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
