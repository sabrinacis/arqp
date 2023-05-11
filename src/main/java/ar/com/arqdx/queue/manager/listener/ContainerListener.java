package ar.com.arqdx.queue.manager.listener;

import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@Component(value="responseListener")
public class ContainerListener implements MessageListener {

    public void onMessage(Message message) {
        //TODO
    }
}