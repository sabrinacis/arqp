package ar.com.arqdx.queue.manager.ibmmq.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "mq")
@ConfigurationPropertiesScan
public class MQConnectionProperties {

    private List<Broker> broker;

    public List<Broker> getBroker() {
        return broker;
    }

    public void setBroker(List<Broker> broker) {
        this.broker = broker;
    }

    @Override
    public String toString() {
        return "MQConnectionProperties{" +
                "broker=" + broker +
                '}';
    }
}