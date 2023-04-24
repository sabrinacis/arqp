package ar.com.arqdx.queue.manager.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "mq")
@ConfigurationPropertiesScan
public class MQConnectionProperties {

    private List<Broker> brokers;

    public List<Broker> getBrokers() {
        return brokers;
    }

    public void setBrokers(List<Broker> brokers) {
        this.brokers = brokers;
    }
}