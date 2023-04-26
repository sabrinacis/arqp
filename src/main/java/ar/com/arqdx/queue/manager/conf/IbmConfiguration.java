package ar.com.arqdx.queue.manager.conf;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Configuration
public class IbmConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(IbmConfiguration.class);

    @Autowired
    private Broker brokers;

    List<Broker> brokersList;
    @Autowired
    IBMMQFactoryList iBMMQFactoryList;


    @PostConstruct
    public List<MQConnectionFactory> factoryList() throws JMSException {

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
    public static BeanFactoryPostProcessor beanFactoryPostProcessor(
            Environment environment) {
        return new BeanFactoryPostProcessor() {

            @Override
            public void postProcessBeanFactory(
                    ConfigurableListableBeanFactory beanFactory) throws BeansException {

                IBMMQFactoryList iBMMQFactoryList = new IBMMQFactoryList();
                beanFactory.registerSingleton("iBMMQFactoryList", iBMMQFactoryList);

                System.out.println("-->>>> contains Bean IBMMQFactoryList :: " + beanFactory.containsBean("iBMMQFactoryList"));


                BindResult<MQConnectionProperties> result = Binder.get(environment)
                        .bind("mq", MQConnectionProperties.class);
                MQConnectionProperties properties = result.get();
                // Use the properties to post-process the bean factory as needed

                for (Broker broker : properties.getBroker()) {
                    LOGGER.info("broker: " + broker.toString());
                    MQConnectionFactory factory = new MQConnectionFactory();
                    factory.setHostName(broker.getHost());
                    try {
                        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

                        factory.setChannel(broker.getChannel());
                        factory.setPort(broker.getPort());
                        factory.setQueueManager(broker.getQmgr());
                        factory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
                        factory.setStringProperty(broker.getUser(), WMQConstants.USERID);
                        factory.setStringProperty(broker.getPassword(), WMQConstants.PASSWORD);

                        iBMMQFactoryList.getFactoryList().add(factory);


                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        };
    }


}