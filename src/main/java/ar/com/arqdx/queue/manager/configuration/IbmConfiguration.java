package ar.com.arqdx.queue.manager.configuration;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



@Configuration
public class IbmConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(IbmConfiguration.class);

    @Autowired
    private MQConnectionProperties mqProperties;

    @Autowired
    private Broker brokers;

    List<Broker> brokersList;

    List<MQConnectionFactory> factoryList;

    @Autowired
    public IbmConfiguration(MQConnectionProperties mqProperties) {
        this.factoryList = new ArrayList<>();
        this.mqProperties = mqProperties;
        this.brokersList = Collections.unmodifiableList(new ArrayList<>(mqProperties.getBrokers()));
    }

    @PostConstruct
    public List<MQConnectionFactory> factoryList() throws JMSException {

// List<Broker> list = List.copyOf(mqProperties.getBrokers());
// List<Broker> brokersList = Collections.unmodifiableList(new ArrayList<>(mqProperties.getBrokers()));
// List<MQConnectionFactory> factoryList = new ArrayList<>();

// METER EL FACTORY EN EL FOR
        for(Broker broker : this.brokersList) {
            LOGGER.info("user: " + broker.getUser());
            LOGGER.info("pass: " + broker.getPassword());
            LOGGER.info("host: " + broker.getHost());
            LOGGER.info("channel: " + broker.getChannel());
            LOGGER.info("port: " + broker.getPort());
            LOGGER.info("qmgr: " + broker.getQmgr());
            MQConnectionFactory factory = new MQConnectionFactory();
            factory.setHostName(broker.getHost());
            factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            factory.setChannel(broker.getChannel());
            factory.setPort(broker.getPort());
            factory.setQueueManager(broker.getQmgr());
            factory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
            factory.setStringProperty(broker.getUser() , WMQConstants.USERID);
            factory.setStringProperty(broker.getPassword(), WMQConstants.PASSWORD);

// Connection connection = factory.createConnection();
// Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

// Borrar el close
// connection.close();
// session.close();

            LOGGER.info("[MQ]*** Se estableció la conexión correctamente para el QMGR "+broker.getQmgr()+" en el host: "+broker.getHost()+"con el puerto: "+broker.getPort());

            this.factoryList.add(factory);
        }
// producer.initApp();

        return factoryList;
    }

    public List<Broker> getBrokersList() {
        return brokersList;
    }

    public void setBrokersList(List<Broker> brokersList) {
        this.brokersList = brokersList;
    }

    public List<MQConnectionFactory> getFactoryList() {
        return factoryList;
    }

    public void setFactoryList(List<MQConnectionFactory> factoryList) {
        this.factoryList = factoryList;
    }
}