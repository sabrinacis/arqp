package ar.com.arqdx.queue.manager.controller;

import ar.com.arqdx.queue.manager.ibmmq.configuration.IQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class MQProducerController {

    @Autowired(required = false)
    @Qualifier("broker0.queue0")
    private IQueue queue0;

    @Autowired(required = false)
    @Qualifier("broker0.queue1")
    private IQueue queue1;

    @GetMapping("send")
    public ResponseEntity<String> send() throws IOException {
        log.info("'VALUES' - Se va a enviar el mensaje ");

        log.info(queue0.toString());

        queue0.sendMessage("--> Send value");

        return ResponseEntity.ok("ok");
    }

    @GetMapping("send/{value}")
    public ResponseEntity<String> values(@PathVariable String value) throws IOException {
        log.info("'VALUES' - Se va a enviar el mensaje: {}", value);

        log.info(queue0.toString());

        queue0.sendMessage(value);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("send2/{value}")
    public ResponseEntity<String> values2(@PathVariable String value) throws IOException {
        log.info("'VALUES2' - Se va a enviar el mensaje: {}", value);

        log.info(queue1.toString());

        queue1.sendMessage(value);

        return ResponseEntity.ok("ok");
    }

    @GetMapping("consume2")
    public ResponseEntity<String> consume2() throws IOException {
        log.info("'VALUES2' - consumiendo el mensaje " );

        log.info(queue1.toString());

        queue1.consume();

        return ResponseEntity.ok("ok");
    }


}

