package ar.com.arqdx.queue.manager.ibmmq.configuration;

import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Component
//@Primary
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
