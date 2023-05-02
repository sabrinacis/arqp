package ar.com.arqdx.queue.manager.ibmmq.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.List;

@ConfigurationProperties(prefix = "mq")
@ConfigurationPropertiesScan
public class Broker {
    private String host;
    private String channel;
    private String qmgr;
    private String user;
    private String password;
    private int port;
    private List<Queue> queue;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getQmgr() {
        return qmgr;
    }

    public void setQmgr(String qmgr) {
        this.qmgr = qmgr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Queue> getQueue() {
        return queue;
    }

    public void setQueue(List<Queue> queues) {
        this.queue = queues;
    }

    @Override
    public String toString() {
        return "Broker{" +
                "host='" + host + '\'' +
                ", channel='" + channel + '\'' +
                ", qmgr='" + qmgr + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", port=" + port +
                ", queue=" + queue +
                '}';
    }
}
