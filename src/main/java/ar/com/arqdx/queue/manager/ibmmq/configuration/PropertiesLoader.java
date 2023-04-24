package ar.com.arqdx.queue.manager.ibmmq.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@Configuration
public class PropertiesLoader {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QueueManagerService queueManagerService;

    @Bean
    public BrokerLoader getBrokerLoader() throws IOException {
        return new BrokerLoader();
    }

    @Bean("broker0.queue0")
    public Queue getQueue0() {
        return new Queue("asd",queueManagerService);
    }

    @Bean("broker0.queue1")
    public Queue getQueue1() {
        return new Queue("csd",queueManagerService);
    }




    @PostConstruct
    public void init() {

        try {
        //  Map<String, String> queueMap = loadQueues();

            ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();

            Map<String, String> queueMap =  getBrokerLoader().getQueues();


            // iterate over properties and register new beans
            for (Map.Entry<String, String> entry : queueMap.entrySet()) {
                System.out.println("<>" + entry.getKey() + ":" + entry.getValue());

                String beanName = entry.getKey() ;

                System.out.println("BeanName: " + beanName);

                IQueue bean = new Queue(entry.getValue(), queueManagerService); // nombre de queue

                beanFactory.registerSingleton(beanName, bean);    // bean name = 'queue0', 'queue1', etc

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
