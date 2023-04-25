package ar.com.arqdx.queue.manager.ibmmq.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import ar.com.arqdx.queue.manager.configuration.Broker;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnProperty(name = JmsAutoConfiguration.JMS_CONFIG_PREFIX + ".enabled", havingValue = "true", matchIfMissing = false)
@EnableJms
@Slf4j
public class JmsAutoConfiguration implements EnvironmentAware {

    protected static final String JMS_CONFIG_PREFIX = "mq";

    private Environment environment;


    @Primary
    @Bean
    public MultiBeanFactory<ConnectionFactory> connectionManagerFactory(
            Map<String, String> queueMap, BeanFactory beanFactory) {

        return new MultiBeanFactory<ConnectionFactory>() {


            @Override
            public String getBeanName(String name) {
                return (getNames().size() == 1) ? "connectionFactory" : name.concat(ConnectionFactory.class.getSimpleName());
            }

            @Override
            public Class<?> getObjectType() {
                return ConnectionFactory.class;
            }

            @Override
            public Collection<String> getNames() {
                return Arrays.asList(queueMap.keySet().toArray(new String[0]));
            }
        };
    }


    @Override
    public void setEnvironment(Environment environment) {

    }
}