package ar.com.arqdx.queue.manager.ibmmq.configuration;

import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class ContainerChecker {

   // @Autowired
    SimpleMessageListenerContainer queueContainer;

    @Scheduled(fixedRate = 300000)
    public void reportContainerStatus() throws  Exception{
        if(!queueContainer.isActive()) {
            System.out.println("--> queueContainer isActive: NO " );

        } else {
            System.out.println("--> queueContainer isActive: YES " );
            queueContainer.getConnectionFactory().createConnection();
        }
    }
}