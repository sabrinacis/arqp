package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.properties.BrokerLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jms.Message;
import java.lang.reflect.Method;
import java.util.Map;

@Service
@Slf4j
public class ListenerSevice {

    @Autowired
    @Qualifier("broker0.queue0")
    private IQueueIBMMQ queue0;

    @Autowired
    @Qualifier("broker0.queue1")
    private IQueueIBMMQ queue1;

    @Bean
    @Primary
    public IQueueIBMMQ queue0() {
        return queue0;
    }

    @Bean
    public IQueueIBMMQ queue1() {
        return queue1;
    }


    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void afteConstruct() {
        log.info("<< ListenerSevice 2 >> ************* initialized MQ Listener successfully, will read from = {}", queue0.getQueueName());
        BrokerLoader bean1 = (BrokerLoader) applicationContext.getBean("brokerLoader");
        Map<String, IQueueIBMMQ> queues = bean1.getQueues();
        reflection(bean1);
    }

    @JmsListener(destination = "#{@queue0.getQueueName()}", containerFactory = "")
    //   @JmsListener(containerFactory = "broker0.queue0.jmsListenerContainerFactory", destination="DEV.QUEUE.VALUES")
    public void process(Message msg) {
        log.info("<< ListenerSevice >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}", msg);

    }

    @JmsListener(destination = "#{@queue1.getQueueName()}", containerFactory = "")
    //   @JmsListener(containerFactory = "broker0.queue0.jmsListenerContainerFactory", destination="DEV.QUEUE.VALUES")
    public void process2(Message msg) {

        log.info("<< ListenerSevice 2 >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}", msg);

    }


    private void reflection(BrokerLoader bean1) {
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
