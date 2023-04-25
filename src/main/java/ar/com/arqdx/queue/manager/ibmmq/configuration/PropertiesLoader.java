package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.service.QueueManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Configuration
public class PropertiesLoader {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QueueManagerService queueManagerService;

    public BrokerLoader getBrokerLoader() throws IOException {
        return new BrokerLoader();
    }

    @PostConstruct
    public void init() {

        try {
            BrokerLoader beanBrokerLoader = (BrokerLoader) applicationContext.getBean("BrokerLoader");

            System.out.println("-->>>>>> bean: " + beanBrokerLoader.toString());

            for (Map.Entry<String, IQueue> entry : beanBrokerLoader.getQueues().entrySet()) {
                entry.getValue().setQueueService(queueManagerService);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
