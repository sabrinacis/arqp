package ar.com.arqdx.queue.manager.service;

import javax.jms.JMSException;

public interface IQueueManagerService {

    void send(String queueName, String message) throws JMSException;

    void consume(String queueName) throws JMSException;
}
