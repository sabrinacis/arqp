package ar.com.arqdx.queue.manager.service;

public interface IIBMMQManagerService {

    void send(String queueName, String message);

    void consume(String queueName);
}
