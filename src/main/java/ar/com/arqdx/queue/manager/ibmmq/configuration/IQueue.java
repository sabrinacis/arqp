package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.service.QueueManagerService;

public interface IQueue {

    void sendMessage(String message);

    void consume();

    void setQueueService(QueueManagerService queueService);

}
