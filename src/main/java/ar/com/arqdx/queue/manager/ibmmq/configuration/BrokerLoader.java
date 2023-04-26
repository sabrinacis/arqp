package ar.com.arqdx.queue.manager.ibmmq.configuration;

import ar.com.arqdx.queue.manager.bean.IQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BrokerLoader {

    private  Map<String, IQueue> queues;

    public BrokerLoader(Map<String, IQueue> queues) {
        this.queues = queues;
    }

    public BrokerLoader() throws IOException {
        this.queues = new HashMap<String, IQueue>();;
    }

    public Map<String, IQueue> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, IQueue> queues) {
        this.queues = queues;
    }
}
