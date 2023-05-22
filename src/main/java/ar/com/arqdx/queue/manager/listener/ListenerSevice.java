package ar.com.arqdx.queue.manager.listener;

import ar.com.arqdx.queue.manager.annotation.DxAnnotationJmsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.jms.Message;

@Service
@Slf4j
public class ListenerSevice {

    @DxAnnotationJmsListener(destination = "DEPOSITOS")
    public void process(Message msg) {
        log.info(" LISTENER DEPOSITOS -->> MENSAJE RECIBIDO: {}", msg);

    }

    @DxAnnotationJmsListener(destination = "ECHEQS")
    public void process2(Message msg) {

        log.info(" LISTENER ECHEQS -->> MENSAJE RECIBIDO: {}", msg);

    }

}
