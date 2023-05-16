package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.bean.QueueIBMMQ;
import ar.com.arqdx.queue.manager.client.ArqDXSessionAwareMessageListener;
import ar.com.arqdx.queue.manager.consumer.MQMessageConsumer;
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
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor;
import org.springframework.jms.config.*;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.*;
import java.util.Enumeration;
import java.util.concurrent.Executor;


//@Configuration
public class IbmConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(IbmConfiguration.class);

    private static final String BROKER = "broker";

    private static final String QUEUE = ".queue";

    private static final String MQ = "mq";

  //  @Autowired
    private ApplicationContext applicationContext;

 //   @Autowired
    private static Executor taskExecutor;


  //  @Autowired
    private Broker brokers;


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
                            // Genera los Beans que se usaran en los Productores/Consumidores, bean name = 'broker0queue0', 'broker0queue1', etc
                            IQueueIBMMQ ibean = new QueueIBMMQ(q1.getName().trim());
                            ibean.setSession(session);
                            ibean.setConnection(connection);

                            Destination producerDestination = session.createQueue(q1.getName());

                            ArqDXSessionAwareMessageListener arqDXMessageListener = new ArqDXSessionAwareMessageListener(Integer.toString(i));
                            JmsListenerContainerFactory jmsListenerContainerFactory = jmsListenerContainerFactory(factory, session, q1.getName(), broker.getConcurrency());
                 //           beanFactory.registerSingleton(getJmsListenerContainerFactoryBeanName(getBeanName(i, j)), jmsListenerContainerFactory);
                            ibean.setJmsListenerContainerFactory(jmsListenerContainerFactory);

                            SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
                            endpoint.setId("myJmsEndpoint");
                            endpoint.setDestination("anotherQueue");

                            DefaultMessageListenerContainer messageListenerContainer = registerMessageListener(ibean.getQueueName(), (MessageListener) arqDXMessageListener, factory, session);
                            beanFactory.registerSingleton(getListenerBeanName(getBeanName(i, j)), messageListenerContainer);

                            ibean.setMessageProducer(new MQMessageProducer(session.createProducer(producerDestination)));
                            ibean.setMessageConsumer(new MQMessageConsumer(session.createConsumer(producerDestination), messageListenerContainer));

                            beanFactory.registerSingleton(getBeanName(i, j), ibean);

                            brokerLoader.getQueues().put(ibean.getQueueName(), ibean);

                            j = j + 1;
                        }
                        i = i + 1;
                        connection.start();

                        for (String bean_name : beanFactory.getRegisteredScopeNames()) {
                            System.out.println("bean_name1:: " + bean_name);
                        }
                        for (String bean_name : beanFactory.getBeanDefinitionNames()) {
                            System.out.println("bean_name2:: " + bean_name);
                        }
                    }

                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

   // @Bean
    public JmsListenerEndpointRegistry jmsListenerEndpointRegistry() {
        return new JmsListenerEndpointRegistry();
    }

    //@Bean
    public JmsListenerContainerTestFactory testFactory() {
        return new JmsListenerContainerTestFactory();
    }

    //@Bean
    public JmsListenerAnnotationBeanPostProcessor postProcessor() {
        JmsListenerAnnotationBeanPostProcessor postProcessor = new JmsListenerAnnotationBeanPostProcessor();
        postProcessor.setContainerFactoryBeanName("testFactory");
        postProcessor.setEndpointRegistry(jmsListenerEndpointRegistry());
        return postProcessor;
    }


/*
    @PostConstruct
    public void factoryList() {


        System.out.println("PostConstruct-------------> Implementamos la anotacion ");

        // Implementamos la anotacion

        try {


// recorremos todas las clases
            //  for (Class<?> qlass : allClasses) {


            MQListener objetoDeMiClase = new MQListener();
            MQListener b1 = applicationContext.getBean(objetoDeMiClase.getClass());
            Class<? extends MQListener> objetoDeClassConInfoDeMiClase = objetoDeMiClase.getClass();
            objetoDeClassConInfoDeMiClase.newInstance();


            // recorremos todos los métodos de cada clase
            for (Method method : objetoDeClassConInfoDeMiClase.getDeclaredMethods()) {

                // si hace uso de nuestra anotación "@MessageFilter()"
                if (method.isAnnotationPresent(JmsListener.class)) {

                    String valueAtributeDestinationOfAnnotation = method.getAnnotation(JmsListener.class).destination();

                    String containerFactory = method.getAnnotation(JmsListener.class).containerFactory();

                    // y si ademas, el mensaje recibido del socket, es igual al de la anotación
                    if (valueAtributeDestinationOfAnnotation != null) {
                        System.out.println("<< PostConstruct Value Atribute Destination of Annotation :: " + valueAtributeDestinationOfAnnotation + " >>");

                        if (applicationContext.containsBean("brokerLoader")) {

                            BrokerLoader bean1 = (BrokerLoader) applicationContext.getBean("brokerLoader");
                            Map<String, IQueueIBMMQ> queues = bean1.getQueues();
                            IQueueIBMMQ q1 = queues.get(valueAtributeDestinationOfAnnotation);

                            Object listener = q1.getMessageConsumer().getMessageListenerContainer().getMessageListener();
                            ArqDXMessageListener messageListener =
                                    (ArqDXMessageListener) q1.getMessageConsumer().getMessageListenerContainer().getMessageListener();
                            messageListener.getLatch().await(3000,
                                    TimeUnit.MILLISECONDS);

                            // entonces ejecutamos el método por reflection
                            Object result = method.invoke(b1, getMsg());

                            System.out.println("<< Fin PostConstructor >>");
                        }
                    }
                }
            }
            //    }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
*/

    /**
     * El DefaultMessageListenerContainer es el único contenedor listener que no impone la gestión de hilos al proveedor JMS
     * (ya que no utiliza/bloquea los hilos del proveedor JMS).
     * El DMLC también es capaz de recuperarse de fallos del proveedor JMS, como la pérdida de conexión.
     * Y es la única variante que soporta gestores de transacciones externos, en particular para transacciones XA.
     *
     * @param destinationName
     * @param listener
     * @param connectionFactory
     * @param session
     * @return
     * @throws JMSException
     */
    public static DefaultMessageListenerContainer registerMessageListener(String destinationName,
                                                                          MessageListener listener, MQConnectionFactory connectionFactory, Session session) throws JMSException {
        LOGGER.info("registerMessageListener(" + destinationName + ", " + listener + ")");
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName(destinationName);
        container.setMessageListener(listener);
        container.setTaskExecutor(taskExecutor);
        container.afterPropertiesSet();
        DestinationResolver destinationResolver = new DynamicDestinationResolver();
        destinationResolver.resolveDestinationName(session, destinationName, false);
        container.setDestinationResolver(destinationResolver);
        container.start();
        return container;
    }


    public static JmsListenerContainerFactory jmsListenerContainerFactory(MQConnectionFactory mqConnectionfactory, Session session, String qName, int concurrency) throws JMSException {
        DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
        containerFactory.setConnectionFactory(mqConnectionfactory);
        containerFactory.setSessionTransacted(true);
        containerFactory.setConcurrency(String.valueOf(concurrency));
        DestinationResolver destinationResolver = new DynamicDestinationResolver();
        destinationResolver.resolveDestinationName(session, qName, false);
        containerFactory.setDestinationResolver(destinationResolver);
        containerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return containerFactory;
    }


    static MQProperties getMQConnectionProperties(Environment environment) {
        BindResult<MQProperties> result = Binder.get(environment)
                .bind(MQ, MQProperties.class);
        return result.get();
    }


    static MQConnectionFactory getMQConnectionFactory(Broker broker) throws JMSException {
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
        //       factory.setStringProperty(CMQC.USER_ID_PROPERTY, broker.getUser());
        //       factory.setStringProperty(CMQC.PASSWORD_PROPERTY, broker.getPassword());

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


    private Object getMsg() {

        Message msg = new Message() {
            @Override
            public String getJMSMessageID() throws JMSException {
                return null;
            }

            @Override
            public void setJMSMessageID(String id) throws JMSException {

            }

            @Override
            public long getJMSTimestamp() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSTimestamp(long timestamp) throws JMSException {

            }

            @Override
            public byte[] getJMSCorrelationIDAsBytes() throws JMSException {
                return new byte[0];
            }

            @Override
            public void setJMSCorrelationIDAsBytes(byte[] correlationID) throws JMSException {

            }

            @Override
            public void setJMSCorrelationID(String correlationID) throws JMSException {

            }

            @Override
            public String getJMSCorrelationID() throws JMSException {
                return null;
            }

            @Override
            public Destination getJMSReplyTo() throws JMSException {
                return null;
            }

            @Override
            public void setJMSReplyTo(Destination replyTo) throws JMSException {

            }

            @Override
            public Destination getJMSDestination() throws JMSException {
                return null;
            }

            @Override
            public void setJMSDestination(Destination destination) throws JMSException {

            }

            @Override
            public int getJMSDeliveryMode() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSDeliveryMode(int deliveryMode) throws JMSException {

            }

            @Override
            public boolean getJMSRedelivered() throws JMSException {
                return false;
            }

            @Override
            public void setJMSRedelivered(boolean redelivered) throws JMSException {

            }

            @Override
            public String getJMSType() throws JMSException {
                return null;
            }

            @Override
            public void setJMSType(String type) throws JMSException {

            }

            @Override
            public long getJMSExpiration() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSExpiration(long expiration) throws JMSException {

            }

            @Override
            public long getJMSDeliveryTime() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSDeliveryTime(long deliveryTime) throws JMSException {

            }

            @Override
            public int getJMSPriority() throws JMSException {
                return 0;
            }

            @Override
            public void setJMSPriority(int priority) throws JMSException {

            }

            @Override
            public void clearProperties() throws JMSException {

            }

            @Override
            public boolean propertyExists(String name) throws JMSException {
                return false;
            }

            @Override
            public boolean getBooleanProperty(String name) throws JMSException {
                return false;
            }

            @Override
            public byte getByteProperty(String name) throws JMSException {
                return 0;
            }

            @Override
            public short getShortProperty(String name) throws JMSException {
                return 0;
            }

            @Override
            public int getIntProperty(String name) throws JMSException {
                return 0;
            }

            @Override
            public long getLongProperty(String name) throws JMSException {
                return 0;
            }

            @Override
            public float getFloatProperty(String name) throws JMSException {
                return 0;
            }

            @Override
            public double getDoubleProperty(String name) throws JMSException {
                return 0;
            }

            @Override
            public String getStringProperty(String name) throws JMSException {
                return null;
            }

            @Override
            public Object getObjectProperty(String name) throws JMSException {
                return null;
            }

            @Override
            public Enumeration getPropertyNames() throws JMSException {
                return null;
            }

            @Override
            public void setBooleanProperty(String name, boolean value) throws JMSException {

            }

            @Override
            public void setByteProperty(String name, byte value) throws JMSException {

            }

            @Override
            public void setShortProperty(String name, short value) throws JMSException {

            }

            @Override
            public void setIntProperty(String name, int value) throws JMSException {

            }

            @Override
            public void setLongProperty(String name, long value) throws JMSException {

            }

            @Override
            public void setFloatProperty(String name, float value) throws JMSException {

            }

            @Override
            public void setDoubleProperty(String name, double value) throws JMSException {

            }

            @Override
            public void setStringProperty(String name, String value) throws JMSException {

            }

            @Override
            public void setObjectProperty(String name, Object value) throws JMSException {

            }

            @Override
            public void acknowledge() throws JMSException {

            }

            @Override
            public void clearBody() throws JMSException {

            }

            @Override
            public <T> T getBody(Class<T> c) throws JMSException {
                return null;
            }

            @Override
            public boolean isBodyAssignableTo(Class c) throws JMSException {
                return false;
            }
        };
        return msg;
    }

}