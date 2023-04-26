package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.bean.IQueue;
import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.IIBMMQManagerService;
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

    @Autowired
    private IBMMQManagerService iBMMQManagerService;

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
                entry.getValue().setiBMMQManagerService(iBMMQManagerService);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
