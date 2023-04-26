package ar.com.arqdx.queue.manager.ibmmq.configuration;

import com.ibm.mq.jms.MQConnectionFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class IBMMQFactoryList {


    List<MQConnectionFactory> factoryList;

    public IBMMQFactoryList() {
        this.factoryList = new ArrayList<>();
    }

    public List<MQConnectionFactory> getFactoryList() {
        return factoryList;
    }

    public void setFactoryList(List<MQConnectionFactory> factoryList) {
        this.factoryList = factoryList;
    }

    @Override
    public String toString() {
        return "IBMMQFactoryList{" +
                "factoryList=" + factoryList +
                '}';
    }
}