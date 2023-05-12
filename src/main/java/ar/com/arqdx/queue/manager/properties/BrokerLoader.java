package ar.com.arqdx.queue.manager.properties;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

import javax.jms.MessageListener;
import java.util.HashMap;
import java.util.Map;

public class BrokerLoader {

    private Map<String, IQueueIBMMQ> queues;

    private Map<MessageListener, AbstractMessageListenerContainer> listenerContainerMap;

    public BrokerLoader(Map<String, IQueueIBMMQ> queues) {
        this.queues = queues;
    }

    public BrokerLoader() {
        this.queues = new HashMap<String, IQueueIBMMQ>();
        this.listenerContainerMap = new HashMap<MessageListener, AbstractMessageListenerContainer>();
    }

    public Map<String, IQueueIBMMQ> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, IQueueIBMMQ> queues) {
        this.queues = queues;
    }

    public Map<MessageListener, AbstractMessageListenerContainer> getListenerContainerMap() {
        return listenerContainerMap;
    }

    public void setListenerContainerMap(Map<MessageListener, AbstractMessageListenerContainer> listenerContainerMap) {
        this.listenerContainerMap = listenerContainerMap;
    }
}
