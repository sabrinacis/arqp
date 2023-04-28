package ar.com.arqdx.queue.manager.ibmmq.configuration;

import com.ibm.disthub2.client.Message;
import org.springframework.stereotype.Service;

import javax.jms.MessageListener;


@Service
public class IBMMQListner implements MessageListener {

    public void onMessage(Message message) {
        System.out.println("Consuming Message - " + new String(message.getBytesBody()));
    }

    @Override
    public void onMessage(javax.jms.Message message) {

    }
}