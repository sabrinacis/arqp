package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.interfaces.IMQMessageProducer;
import ar.com.arqdx.queue.manager.interfaces.IQueue;
import ar.com.arqdx.queue.manager.bean.Queue;
import ar.com.arqdx.queue.manager.service.IBMMQManagerService;
import ar.com.arqdx.queue.manager.service.QueueManagerService;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueueConnectionFactory;
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
import javax.jms.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;


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

                    MQConnectionProperties properties = getMQConnectionProperties(environment);

                    int i = 0;
                    for (Broker broker : properties.getBroker()) {
                        LOGGER.info("MQConnectionFactory broker: " + broker.toString());
                        // Genera Connection Factory
                        MQConnectionFactory factory = getMQConnectionFactory(broker);

                        iBMMQFactoryList.getFactoryList().add(factory);

                        int j = 0;
                        for (ar.com.arqdx.queue.manager.ibmmq.configuration.Queue q1 : broker.getQueue()) {
                            // Genera los Beans que se usaran en los Productores/Consumidores, bean name = 'broker0queue0', 'broker0queue1', etc
                            IQueue ibean = getQueue(broker, i, j);
                            beanFactory.registerSingleton(ibean.getQueueName(), ibean);
                            iBrokerLoader.getQueues().put(ibean.getQueueName(), ibean);
                            j = j + 1;
                        }
                        i = i + 1;
                    }

                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }


    @PostConstruct
    public List<MQConnectionFactory> factoryList() throws JMSException {


            BrokerLoader beanBrokerLoader = (BrokerLoader) applicationContext.getBean("BrokerLoader");
            IBMMQFactoryList iBMMQFactoryList = (IBMMQFactoryList) applicationContext.getBean("iBMMQFactoryList");

            System.out.println("-->>>>>> bean: " + beanBrokerLoader.toString());

            for (Map.Entry<String, IQueue> entry : beanBrokerLoader.getQueues().entrySet()) {
                IQueue iQueue1 = entry.getValue();
                iQueue1.setIbmMQQueueService(queueManagerService);
                iQueue1.setiBMMQManagerService(iBMMQManagerService);
                iQueue1.setSession(getSession(iBMMQFactoryList.getFactoryList().get(0)));

                Destination producerDestination = iQueue1.getSession().createQueue(iQueue1.getQueueName());

                iQueue1.setMessageProducer(new MQMessageProducer(iQueue1.getSession().createProducer(producerDestination)));
                iQueue1.setMessageConsumer(new MQMessageConsumer(iQueue1.getSession().createConsumer(producerDestination)));

                iQueue1.getConnection().start();

            }
            return iBMMQFactoryList.getFactoryList();
    }


    @Bean
    public BrokerLoader getBrokerLoader() throws IOException {
        return new BrokerLoader();
    }


    private static MQConnectionProperties getMQConnectionProperties(Environment environment) {
        BindResult<MQConnectionProperties> result = Binder.get(environment)
                .bind("mq", MQConnectionProperties.class);
        return result.get();
    }

    private static String getBeanName(int i, int j) {
        StringBuffer beanName = new StringBuffer();
        beanName.append(BROKER).append(i).append(QUEUE).append(j);
        return beanName.toString();
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

    private static MQConnectionFactory getMQConnectionFactory(Broker broker) throws JMSException {
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
        return factory;
    }


    private static IQueue getQueue(Broker broker, int i, int j) {
        String beanName = getBeanName(i, j);
        System.out.println("--> broker: " + broker + " queue name: " + broker.getQueue() + " --> BeanName: " + beanName);
        IQueue ibean = new Queue(beanName, null);

        return ibean;
    }

    private static Session getSession(MQConnectionFactory factory) throws JMSException {
        // Genera Connection Factory
        Connection connection = factory.createConnection();
        //La especificación JMS indica que una conexión se crea en el estado stopped
        // Hasta que se inicie una conexión, un consumidor de mensaje que esté asociado a la conexión no puede recibir ningún mensaje.
        connection.start();

        //AUTO_ACKNOWLEDGE: La sesión acusa recibo automáticamente de cada mensaje recibido por la aplicación.
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        return session;
    }

    //MQMessageProducer


}