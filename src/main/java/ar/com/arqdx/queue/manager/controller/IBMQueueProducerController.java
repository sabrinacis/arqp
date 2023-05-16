package ar.com.arqdx.queue.manager.controller;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import ar.com.arqdx.queue.manager.message.QueueMessageTX;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import java.io.IOException;

@RestController
@Slf4j
public class IBMQueueProducerController {

    @Autowired
    @Qualifier("broker0_queue0")
    private IQueueIBMMQ queue0;

    @Autowired
    @Qualifier("broker0_queue1")
    private IQueueIBMMQ queue1;

    @GetMapping("send1/{value}")
    public ResponseEntity<String> values1(@PathVariable String value) throws IOException, JMSException {
        log.info("'send1' - Se va a enviar el mensaje: {}", value);

        log.info(queue0.toString());

        queue0.sendMessage(new QueueMessageTX("1",value));

        return ResponseEntity.ok("ok");
    }


    @GetMapping("send2/{value}")
    public ResponseEntity<String> values2(@PathVariable String value) throws IOException, JMSException {
        log.info("'send2' - Se va a enviar el mensaje: {}", value);

        log.info(queue1.toString());

        queue1.sendMessage(new QueueMessageTX("2",value));

        return ResponseEntity.ok("ok");
    }

    @GetMapping("consume2")
    public ResponseEntity<String> consume2() throws IOException, JMSException {
        log.info("'VALUES2' - consumiendo el mensaje ");

        log.info(queue1.toString());

        queue1.consume();

        return ResponseEntity.ok("ok");
    }


}

