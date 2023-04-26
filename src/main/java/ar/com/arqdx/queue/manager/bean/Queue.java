package ar.com.arqdx.queue.manager.bean;

import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Queue implements IQueue {

    private QueueManagerService queueService;

    private IBMMQManagerService iBMMQManagerService;

    private String queueName;

    public Queue() {
    }

    public Queue(String qName, QueueManagerService qService) {
        this.queueName = qName;
        this.queueService = qService;
    }

    public Queue(IBMMQManagerService iBMMQManagerService, String queueName) {
        this.iBMMQManagerService = iBMMQManagerService;
        this.queueName = queueName;
    }

    @Override
    public void sendMessage(String message) {
        log.info("Se envía mensaje");
        this.queueService.send(queueName, message);
    }

    @Override
    public void consume() {
        log.info("Se envía mensaje");
        this.queueService.consume(queueName);
    }


    public QueueManagerService getQueueService() {
        return queueService;
    }

    public void setQueueService(QueueManagerService queueService) {
        this.queueService = queueService;
    }

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
    public String toString() {
        return "Queue{" +
                "queueName='" + queueName + '\'' +
                '}';
    }
}
