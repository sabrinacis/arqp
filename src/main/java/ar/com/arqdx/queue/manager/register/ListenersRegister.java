package ar.com.arqdx.queue.manager.register;

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
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Configuration
@EnableJms
@Slf4j
public class ListenersRegister implements JmsListenerConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

        Map<String, IQueueIBMMQ> queueList2 = getIqueueIBMMQList();

        queueList2.forEach((k, v) -> {
            System.out.println("Key: " + k + ": Value: " + v);

            SimpleJmsListenerEndpoint endpoint = getJmsListenerEndpoint(v);

            try {
                endpoint.setMessageListener(message -> {
                    try {
                        log.info("Receieved ID: {} Destination {} - Mensaje escuchador: {} ", message.getJMSMessageID(), message.getJMSDestination(), v.getMethodListener().getName());
                        v.getMethodListener().invoke(v.getBeanListenerClassInfo(), message);

                    } catch (JMSException e) {
                        log.info("Exception while reading message - " + e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
                registrar.setContainerFactory(v.getJmsListenerContainerFactory());
            } catch (Exception e) {
                log.info("Exception - " + e);
            }
            registrar.registerEndpoint(endpoint);
        });

    }


    public Map<String, IQueueIBMMQ> getIqueueIBMMQList() {
        log.info("<< getIqueueIBMMQList >> ************* ");
        BrokerLoader bean1 = (BrokerLoader) applicationContext.getBean("brokerLoader");
        Map<String, IQueueIBMMQ> queues = bean1.getQueues();
        return queues;
    }

    private SimpleJmsListenerEndpoint getJmsListenerEndpoint(IQueueIBMMQ iq1) {

        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        MessageListener messageListener = new MessageListenerAdapter();
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId(iq1.getQueueName());
        endpoint.setDestination(iq1.getQueueName());
        //   endpoint.setSelector("foo = 'bar'");
        //   endpoint.setSubscription("mySubscription");
        //  endpoint.setConcurrency(String.valueOf(iq1.getConcurrency()));
        StringBuffer sb1 = new StringBuffer();
        sb1.append(iq1.getMinconcurrency()).append("-").append(iq1.getMaxconcurrency());
        endpoint.setConcurrency(sb1.toString());
        endpoint.setMessageListener(messageListener);
        endpoint.setupListenerContainer(container);


        return endpoint;
    }


}
