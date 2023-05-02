package ar.com.arqdx.queue.manager.properties;

import ar.com.arqdx.queue.manager.bean.IQueueIBMMQ;

import java.util.HashMap;
import java.util.Map;

public class BrokerLoader {

    private  Map<String, IQueueIBMMQ> queues;

    public BrokerLoader(Map<String, IQueueIBMMQ> queues) {
        this.queues = queues;
    }

    public BrokerLoader()   {
        this.queues = new HashMap<String, IQueueIBMMQ>();;
    }

    public Map<String, IQueueIBMMQ> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, IQueueIBMMQ> queues) {
        this.queues = queues;
    }
}
