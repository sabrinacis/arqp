package ar.com.arqdx.queue.manager.client;

import javax.jms.Message;

public interface IArqDXMessageListener {

    void onMessage(Message message);
}
