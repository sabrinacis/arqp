package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jms.Message;

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

    @PostConstruct
    public void afteConstruct() {
        log.info("<< ListenerSevice 2 >> ************* initialized MQ Listener successfully, will read from = {}",queue0.getQueueName());

    }

    @JmsListener(destination = "#{@queue0.getQueueName()}", containerFactory = "")
 //   @JmsListener(containerFactory = "broker0.queue0.jmsListenerContainerFactory", destination="DEV.QUEUE.VALUES")
    public void process(Message msg) {
        log.info("<< ListenerSevice >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}",msg);

    }

    @JmsListener(destination = "#{@queue1.getQueueName()}", containerFactory = "")
    //   @JmsListener(containerFactory = "broker0.queue0.jmsListenerContainerFactory", destination="DEV.QUEUE.VALUES")
    public void process2(Message msg) {

        log.info("<< ListenerSevice 2 >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}",msg);

    }
}
