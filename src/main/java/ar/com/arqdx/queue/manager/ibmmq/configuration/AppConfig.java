package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.properties.BrokerLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;

import java.util.Map;

@Configuration
@EnableJms
@Slf4j
public class AppConfig implements JmsListenerConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

        BrokerLoader bean1 = (BrokerLoader) applicationContext.getBean("brokerLoader");
        Map<String, IQueueIBMMQ> queues = bean1.getQueues();

        int i = 0;
        IQueueIBMMQ q1 = queues.get("broker0.queue0");

        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId("myJmsEndpoint-" + i++);
        endpoint.setDestination(q1.getQueueName());
        endpoint.setMessageListener(message -> {
            log.info("***********************************************receivedMessage: " + message);
        });
        registrar.registerEndpoint(endpoint);
        log.info("********** registered the endpoint for queue " + q1.getQueueName());
    }
}