package ar.com.arqdx.queue.manager.bean;

import ar.com.arqdx.queue.manager.interfaces.IMQMessageConsumer;
import ar.com.arqdx.queue.manager.interfaces.IMQMessageProducer;
import ar.com.arqdx.queue.manager.interfaces.IQueue;
import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.Session;

@Component
@Slf4j
public class Queue implements IQueue {

    private QueueManagerService ibmMQQueueService;

    private IBMMQManagerService iBMMQManagerService;

    private String queueName;

    private Session session;

    private IMQMessageProducer messageProducer;
    private IMQMessageConsumer messageConsumer;

    private Connection connection;

    public Queue() {
    }

    public Queue(String qName, QueueManagerService qService) {
        this.queueName = qName;
        this.ibmMQQueueService = qService;
    }

    public Queue(IBMMQManagerService iBMMQManagerService, String queueName) {
        this.iBMMQManagerService = iBMMQManagerService;
        this.queueName = queueName;
    }

    @Override
    public void sendMessage(String message) {
        log.info("Se envía mensaje");
        this.ibmMQQueueService.send(queueName, message);
    }

    @Override
    public void consume() {
        log.info("Se envía mensaje");
        this.ibmMQQueueService.consume(queueName);
    }

    @Override
    public QueueManagerService getIbmMQQueueService() {
        return ibmMQQueueService;
    }

    public void setIbmMQQueueService(QueueManagerService ibmMQQueueService) {
        this.ibmMQQueueService = ibmMQQueueService;
    }
    @Override
    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public IBMMQManagerService getiBMMQManagerService() {
        return iBMMQManagerService;
    }

    public void setiBMMQManagerService(IBMMQManagerService iBMMQManagerService) {
        this.iBMMQManagerService = iBMMQManagerService;
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
    public String toString() {
        return "Queue{" +
                "queueName='" + queueName + '\'' +
                '}';
    }
}
