package ar.com.arqdx.queue.manager.conf;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.Collections;
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