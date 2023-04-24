package ar.com.arqdx.queue.manager.ibmmq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Queue implements IQueue {

    private QueueManagerService queueService;

    private String queueName;

    public Queue() {}
    public Queue(String qName, QueueManagerService qService) {
        this.queueName = qName;
        this.queueService = qService;
    }

    @Override
    public void sendMessage(String message) {
        log.info("Se envía mensaje");
        this.queueService.send(queueName, message);
    }

    @Override
    public void consume( ){
 log.info("Se envía mensaje");
        this.queueService.consume(queueName);
}
    @Override
    public String toString() {
        return "Queue{" +
                "queueName='" + queueName + '\'' +
                '}';
    }
}
