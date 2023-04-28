package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.interfaces.IQueue;

import java.util.HashMap;
import java.util.Map;

public class BrokerLoader {

    private  Map<String, IQueue> queues;

    public BrokerLoader(Map<String, IQueue> queues) {
        this.queues = queues;
    }

    public BrokerLoader()   {
        this.queues = new HashMap<String, IQueue>();;
    }

    public Map<String, IQueue> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, IQueue> queues) {
        this.queues = queues;
    }
}
