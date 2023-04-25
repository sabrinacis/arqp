package ar.com.arqdx.queue.manager.ibmmq.configuration;

public interface IQueue {

    void sendMessage(String message);

    void consume();

    void setQueueService(QueueManagerService queueService);

}
