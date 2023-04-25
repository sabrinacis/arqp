package ar.com.arqdx.queue.manager.service;

public interface IQueueManagerService {

    void send(String queueName, String message);

    void consume(String queueName);
}
