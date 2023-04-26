package ar.com.arqdx.queue.manager.bean;

import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;

public interface IQueue {

    void sendMessage(String message);

    void consume();

    void setQueueService(QueueManagerService queueService);

    void setiBMMQManagerService(IBMMQManagerService iBMMQManagerService);

}
