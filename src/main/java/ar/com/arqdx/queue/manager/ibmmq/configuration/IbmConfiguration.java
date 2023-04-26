package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.bean.IQueue;
import ar.com.arqdx.queue.manager.bean.Queue;
import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;
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

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import java.io.IOException;
import java.util.*;


@Configuration
public class IbmConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(IbmConfiguration.class);

    private static final String BROKER = "broker";

    private static final String QUEUE = ".queue";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Broker brokers;

    @Autowired
    IBMMQFactoryList iBMMQFactoryList;

    @Autowired
    private QueueManagerService queueManagerService;

    @Autowired
    private IBMMQManagerService iBMMQManagerService;


    @PostConstruct
    public List<MQConnectionFactory> factoryList() throws JMSException {

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

// List<Broker> list = List.copyOf(mqProperties.getBrokers());
// List<Broker> brokersList = Collections.unmodifiableList(new ArrayList<>(mqProperties.getBrokers()));
// List<MQConnectionFactory> factoryList = new ArrayList<>();


// Connection connection = factory.createConnection();
// Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

// Borrar el close
// connection.close();
// session.close();


// producer.initApp();

        return iBMMQFactoryList.getFactoryList();
    }

    @Bean
    public BrokerLoader getBrokerLoader() throws IOException {
        return new BrokerLoader();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor(
            Environment environment) {
        return new BeanFactoryPostProcessor() {

            @Override
            public void postProcessBeanFactory(
                    ConfigurableListableBeanFactory beanFactory) throws BeansException {
                try {
                    BrokerLoader iBrokerLoader = new BrokerLoader();
                    beanFactory.registerSingleton("BrokerLoader", iBrokerLoader);

                    IBMMQFactoryList iBMMQFactoryList = new IBMMQFactoryList();
                    beanFactory.registerSingleton("iBMMQFactoryList", iBMMQFactoryList);

                    System.out.println("-->>>> contains Bean IBMMQFactoryList :: " + beanFactory.containsBean("iBMMQFactoryList"));

                    BindResult<MQConnectionProperties> result = Binder.get(environment)
                            .bind("mq", MQConnectionProperties.class);
                    MQConnectionProperties properties = result.get();
                    // Use the properties to post-process the bean factory as needed
                    int i = 0;
                    for (Broker broker : properties.getBroker()) {
                        LOGGER.info("broker: " + broker.toString());

                        int j = 0;
                        for (ar.com.arqdx.queue.manager.ibmmq.configuration.Queue q1 : broker.getQueue()) {
                            StringBuffer beanName = new StringBuffer();
                            beanName.append(BROKER).append(i).append(QUEUE).append(j);

                            // Genera los Beans que se usaran en los Productores/Consumidores, bean name = 'broker0queue0', 'broker0queue1', etc
                            System.out.println("--> broker: " + broker + " queue name: " + broker.getQueue() + " --> BeanName: " + beanName);
                            IQueue ibean = new Queue(q1.getName(), null);

                            beanFactory.registerSingleton(beanName.toString(), ibean);

                            iBrokerLoader.getQueues().put(beanName.toString(),ibean);
                            j = j + 1;
                        }


                        // Genera Connection Factory
                        MQConnectionFactory factory = new MQConnectionFactory();
                        factory.setHostName(broker.getHost());

                        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

                        factory.setChannel(broker.getChannel());
                        factory.setPort(broker.getPort());
                        factory.setQueueManager(broker.getQmgr());
                        factory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
                        factory.setStringProperty(broker.getUser(), WMQConstants.USERID);
                        factory.setStringProperty(broker.getPassword(), WMQConstants.PASSWORD);

                        iBMMQFactoryList.getFactoryList().add(factory);


                        i = i + 1;
                    }

                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }

        };
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

}