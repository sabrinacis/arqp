package ar.com.arqdx.queue.manager.bean;

import ar.com.arqdx.queue.manager.interfaces.IMQMessageConsumer;
import ar.com.arqdx.queue.manager.interfaces.IMQMessageProducer;
import ar.com.arqdx.queue.manager.interfaces.IQueue;
import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Component
@Slf4j
public class Queue implements IQueue {

    private QueueManagerService queueManagerService;

    private IBMMQManagerService iBMMQManagerService;

    private String queueName;

    private Session session;

    private IMQMessageProducer messageProducer;
    private IMQMessageConsumer messageConsumer;

    private Connection connection;

    public Queue() {
    }

    public Queue(String queueName) {
        this.queueName = queueName;
    }

    public Queue(String qName, QueueManagerService qService) {
        this.queueName = qName;
        this.queueManagerService = qService;
    }

    public Queue(IBMMQManagerService iBMMQManagerService, String queueName) {
        this.iBMMQManagerService = iBMMQManagerService;
        this.queueName = queueName;
    }

    @Override
    public void sendMessage(String message) throws JMSException {
        log.info("Se envía mensaje");
        Message mess1 = session.createTextMessage(message);

        messageProducer.getProducer().send(mess1);
    }

    @Override
    public void consume() throws JMSException {
        log.info("Se envía mensaje");
        this.messageConsumer.getConsumer().receive();
    }

    @Override
    public QueueManagerService getQueueManagerService() {
        return queueManagerService;
    }

    public void setQueueManagerService(QueueManagerService queueManagerService) {
        this.queueManagerService = queueManagerService;
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
