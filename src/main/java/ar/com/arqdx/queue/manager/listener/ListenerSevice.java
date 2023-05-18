package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.Message;

@Service
@Slf4j
public class ListenerSevice {

    @Autowired
    @Qualifier("broker0_queue0")
    private IQueueIBMMQ broker0_queue0;

    @Autowired
    @Qualifier("broker0_queue1")
    private IQueueIBMMQ broker0_queue1;


    @JmsListener(destination = "#{@broker0_queue0.getQueueName()}", containerFactory = "#{@broker0_queue0.getListenerName()}")
    public void process(Message msg) {
        log.info("<< ListenerSevice >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}", msg);

    }

    @JmsListener(destination = "#{@broker0_queue1.getQueueName()}", containerFactory = "#{@broker0_queue1.getListenerName()}")
    public void process2(Message msg) {

        log.info("<< ListenerSevice 2 >> @DxAnnotationJmsListener -->> MENSAJE RECIBIDO --> {}", msg);

    }

}
