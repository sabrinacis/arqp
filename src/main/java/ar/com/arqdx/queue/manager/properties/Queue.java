package ar.com.arqdx.queue.manager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "mq")
@ConfigurationPropertiesScan
public class Queue  {

    private String name;

    private int concurrency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "name='" + name + '\'' +
                ", concurrency=" + concurrency +
                '}';
    }
}
