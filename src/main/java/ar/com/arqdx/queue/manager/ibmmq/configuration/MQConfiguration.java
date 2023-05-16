package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.bean.QueueIBMMQ;
import ar.com.arqdx.queue.manager.client.ArqDXSessionAwareMessageListener;
import ar.com.arqdx.queue.manager.consumer.MQMessageConsumer;
import ar.com.arqdx.queue.manager.listener.ListenerSevice;
import ar.com.arqdx.queue.manager.producer.MQMessageProducer;
import ar.com.arqdx.queue.manager.properties.Broker;
import ar.com.arqdx.queue.manager.properties.BrokerLoader;
import ar.com.arqdx.queue.manager.properties.MQProperties;
import ar.com.arqdx.queue.manager.properties.Queue;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.*;

@Configuration
public class MQConfiguration  {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQConfiguration.class);

    private static final String BROKER = "broker";

    private static final String QUEUE = "_queue";

    private static final String MQ = "mq";
    @Autowired
    private static ApplicationContext applicationContext;


    private DestinationResolver destinationResolver(AbstractJmsListenerContainerFactory<AbstractMessageListenerContainer> containerFactory, Session session, String qName) throws JMSException {
        DestinationResolver destinationResolver = new DynamicDestinationResolver();
        destinationResolver.resolveDestinationName(session, qName, false);
        containerFactory.setDestinationResolver(destinationResolver);
        return destinationResolver;
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor(
            Environment environment) {
        return new BeanFactoryPostProcessor() {

            @Override
            public void postProcessBeanFactory(
                    ConfigurableListableBeanFactory beanFactory) throws BeansException {
                try {


                    BrokerLoader brokerLoader = new BrokerLoader();
                    beanFactory.registerSingleton("brokerLoader", brokerLoader);

                    MQProperties properties = getMQConnectionProperties(environment);
                    int i = 0;
                    for (Broker broker : properties.getBroker()) {
                        LOGGER.info("MQConnectionFactory broker: " + broker.toString());
                        // Genera Connection Factory
                        MQConnectionFactory factory = getMQConnectionFactory(broker);

                        Connection connection = factory.createConnection();

                        //AUTO_ACKNOWLEDGE: La sesión acusa recibo automáticamente de cada mensaje recibido por la aplicación.
                        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                        int j = 0;
                        for (Queue q1 : broker.getQueue()) {
                            String beanName = getBeanName(i, j).trim();
                            JmsListenerContainerFactory jmsListenerContainerFactory = jmsListenerContainerFactory(factory, session, q1.getName(), broker.getConcurrency());
                            beanFactory.registerSingleton(getJmsListenerContainerFactoryBeanName(beanName), jmsListenerContainerFactory);

                            IQueueIBMMQ ibean = getIQueueIBMMQ(q1.getName(), beanName, session, connection, jmsListenerContainerFactory, factory);
                            ibean.setListenerName(getJmsListenerContainerFactoryBeanName(beanName));
                            beanFactory.registerSingleton(beanName, ibean);

                            brokerLoader.getQueues().put(beanName, ibean);

                            j = j + 1;
                        }
                        i = i + 1;
                        connection.start();
                    }

                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }

            ;
        };
    }

    private static IQueueIBMMQ getIQueueIBMMQ(String qName,String beanName, Session session, Connection connection, JmsListenerContainerFactory jmsListenerContainerFactory, MQConnectionFactory factory) throws JMSException {
        // Genera los Beans que se usaran en los Productores/Consumidores, bean name = 'broker0queue0', 'broker0queue1', etc
        IQueueIBMMQ ibean = new QueueIBMMQ(qName.trim());
        ibean.setSession(session);
        ibean.setConnection(connection);
        Destination destination = session.createQueue(qName.trim());
        ibean.setJmsListenerContainerFactory(jmsListenerContainerFactory);
        ibean.setMessageProducer(new MQMessageProducer(session.createProducer(destination)));
        return ibean;
    }

    public static JmsListenerContainerFactory jmsListenerContainerFactory(MQConnectionFactory mqConnectionfactory, Session session, String qName, int concurrency) throws JMSException {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(mqConnectionfactory);
        containerFactory.setSessionTransacted(true);
        containerFactory.setConcurrency(String.valueOf(concurrency));
        containerFactory.setMessageConverter(new SimpleMessageConverter());
        DestinationResolver destinationResolver = new DynamicDestinationResolver();
        destinationResolver.resolveDestinationName(session, qName, false);
        containerFactory.setDestinationResolver(destinationResolver);
        containerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return containerFactory;
    }

    private static MQConnectionFactory getMQConnectionFactory(Broker broker) throws JMSException {
        // Genera Connection Factory
        MQConnectionFactory factory = new MQConnectionFactory();
        factory.setHostName(broker.getHost());
        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        factory.setChannel(broker.getChannel());
        factory.setPort(broker.getPort());
        factory.setQueueManager(broker.getQmgr());

        factory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
        factory.setStringProperty(WMQConstants.USERID, broker.getUser());
        factory.setStringProperty(WMQConstants.PASSWORD, broker.getPassword());
        return factory;
    }


    private static String getBeanName(int i, int j) {
        StringBuffer beanName = new StringBuffer();
        beanName.append(BROKER).append(i).append(QUEUE).append(j);
        return beanName.toString();
    }


    private static String getListenerBeanName(String beanName) {
        StringBuffer beanName2 = new StringBuffer();
        beanName2.append(beanName).append(".listener");
        return beanName2.toString();
    }


    private static String getJmsListenerContainerFactoryBeanName(String beanName) {
        StringBuffer beanName2 = new StringBuffer();
        beanName2.append(beanName).append(".jmsListenerContainerFactory");
        return beanName2.toString();
    }


    private static String getKey(String v1) {
        String sbuffer = replace(v1, 1) +
                "." +
                replace(v1, 2);
        return sbuffer;
    }

    private static String replace(String v1, int i) {
        return v1.split("\\.")[i].replaceAll("[^\\w+]", "").replaceAll("[sS]", "");
    }

    private static MQProperties getMQConnectionProperties(Environment environment) {
        BindResult<MQProperties> result = Binder.get(environment)
                .bind(MQ, MQProperties.class);
        return result.get();
    }

}
