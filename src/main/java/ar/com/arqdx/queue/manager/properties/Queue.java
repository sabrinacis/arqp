package ar.com.arqdx.queue.manager.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "mq")
@ConfigurationPropertiesScan
public class Queue  {

    private String name;

    private int minconcurrency;

    private int maxconcurrency;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinconcurrency() {
        return minconcurrency;
    }

    public void setMinconcurrency(int minconcurrency) {
        this.minconcurrency = minconcurrency;
    }

    public int getMaxconcurrency() {
        return maxconcurrency;
    }

    public void setMaxconcurrency(int maxconcurrency) {
        this.maxconcurrency = maxconcurrency;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "name='" + name + '\'' +
                ", minconcurrency=" + minconcurrency +
                ", maxconcurrency=" + maxconcurrency +
                '}';
    }
}
