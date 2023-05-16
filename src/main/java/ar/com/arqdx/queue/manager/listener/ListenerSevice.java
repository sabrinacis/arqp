package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.ibmmq.configuration.AppConfig;
import ar.com.arqdx.queue.manager.properties.BrokerLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.Map;

@Service
@Slf4j
public class ListenerSevice {

    @Autowired
    @Qualifier("broker0_queue0")
    private IQueueIBMMQ broker0_queue0;

    @Autowired
    @Qualifier("broker0_queue1")
    private IQueueIBMMQ broker0_queue1;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JmsListenerEndpointRegistry someUpdateListener;

    @PostConstruct
    public void afteConstruct() {
        log.info("<< ListenerSevice 2 >> ************* initialized MQ Listener successfully, will read from = {}", broker0_queue0.getQueueName());
        BrokerLoader bean1 = (BrokerLoader) applicationContext.getBean("brokerLoader");
        Map<String, IQueueIBMMQ> queues = bean1.getQueues();
        reflection(bean1);
    }
/*
    @Bean
    public JmsListenerAnnotationBeanPostProcessor postProcessor() {
        JmsListenerAnnotationBeanPostProcessor postProcessor = new JmsListenerAnnotationBeanPostProcessor();
        postProcessor.setContainerFactoryBeanName("testFactory");
        postProcessor.setEndpointRegistry(jmsListenerEndpointRegistry());
        return postProcessor;
    }
  */
    @JmsListener(destination = "#{@broker0_queue0.getQueueName()}", containerFactory = "#{@broker0_queue0.getListenerName()}")
    public void process(Message msg) {
        log.info("<< ListenerSevice >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}", msg);

    }

    @JmsListener(destination = "#{@broker0_queue1.getQueueName()}", containerFactory = "#{@broker0_queue1.getListenerName()}")
    public void process2(Message msg) {

        log.info("<< ListenerSevice 2 >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}", msg);

    }


    private void reflection(BrokerLoader bean1) {

        MessageListenerContainer container = someUpdateListener.getListenerContainer("someUpdateListener");


        ListenerSevice objetoDeMiClase = new ListenerSevice();
        ListenerSevice b1 = applicationContext.getBean(objetoDeMiClase.getClass());
        Class<? extends ListenerSevice> objetoDeClassConInfoDeMiClase = objetoDeMiClase.getClass();


        // recorremos todos los métodos de cada clase
        for (Method method : objetoDeClassConInfoDeMiClase.getDeclaredMethods()) {

            // si hace uso de nuestra anotación "@MessageFilter()"
            if (method.isAnnotationPresent(JmsListener.class)) {

                String valueAtributeDestinationOfAnnotation = method.getAnnotation(JmsListener.class).destination();

                String containerFactory = method.getAnnotation(JmsListener.class).containerFactory();

                // y si ademas, el mensaje recibido del socket, es igual al de la anotación
                if (containerFactory.equals("")) {
                    System.out.println("<< PostConstruct Value Atribute Destination of Annotation :: " + valueAtributeDestinationOfAnnotation + " >>");
                    method.getAnnotation(JmsListener.class).containerFactory().replace("","1");
                    String containerFactory2 = method.getAnnotation(JmsListener.class).containerFactory();
                }
            }
        }
    }
}
