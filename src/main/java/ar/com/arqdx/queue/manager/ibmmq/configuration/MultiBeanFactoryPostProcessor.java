package ar.com.arqdx.queue.manager.ibmmq.configuration;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Postprocesador que recuperando instancias de la factoría de beans
 * registrados proviamente del tipo de interfaz MultiBeanFactory,
 * registra de forma dinámica instancias de los mismos
 */
@Slf4j
public class MultiBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @SuppressWarnings({"rawtypes"})
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            HashMap<String, String> queues = (HashMap<String, String>) loadQueues();

            init(queues, beanFactory);

            System.out.println("-->>>> contains Bean broker0.queue1 :: " + beanFactory.containsBean("broker0.queue1"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void init(HashMap<String, String> queueMap, ConfigurableListableBeanFactory beanFactory) {
        try {
            BrokerLoader iBrokerLoader = new BrokerLoader();

            for (Map.Entry<String, String> entry : queueMap.entrySet()) {
                String beanName = entry.getKey();
                System.out.println("--> Key: " + entry.getKey() + " Value: " + entry.getValue() + " --> BeanName: " + beanName);
                IQueue ibean = new Queue(entry.getValue(), null);
                beanFactory.registerSingleton(beanName, ibean);    // bean name = 'broker0queue0', 'broker0queue1', etc
                iBrokerLoader.getQueues().put(beanName,ibean);
            }
            beanFactory.registerSingleton("BrokerLoader", iBrokerLoader);    // bean name = 'broker0queue0', 'broker0queue1', etc

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, String> loadQueues() throws IOException {
        // se carga archivo de properties
        HashMap<String, String> queues = new HashMap<String, String>();

        Properties configuration = new Properties();
        InputStream inputStream = BrokerLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties");
        configuration.load(inputStream);

        // se recorren todas las properties
        Enumeration<Object> valueEnumeration = configuration.keys();
        while (valueEnumeration.hasMoreElements()) {
            String key = (String) valueEnumeration.nextElement();
            System.out.println(">>>" + key + " = " + configuration.getProperty(key));

            // se procesa property que sea un nombre de queue
            if (isQueueName(key)) { // key -> ms.broker[0].queue[0].name
                String queue = getKey(key);   // queue[0]
                queues.put(queue, configuration.getProperty(key));
            }
        }
        inputStream.close();
        return queues;
    }

    private static boolean isQueueName(String key) {
        return key.contains("brokers") & key.contains("queue") & key.contains("name");
    }

    private String getKey(String v1) {
        String sbuffer = replace(v1, 1) +
                "." +
                replace(v1, 2);
        return sbuffer;
    }

    private String replace(String v1, int i) {
        return v1.split("\\.")[i].replaceAll("[^\\w+]", "").replaceAll("[sS]", "");
    }


    public void registerBeans(ConfigurableListableBeanFactory beanFactory) {
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        Map<String, MultiBeanFactory> factories = beanFactory.getBeansOfType(MultiBeanFactory.class);
        registerBeans(registry, factories);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerBeans(BeanDefinitionRegistry registry, Map<String, MultiBeanFactory> factories) {
        factories.forEach((factoryName, factory) -> {
            factory.getNames().forEach(bean -> {
                final String beanName = factory.getBeanName((String) bean);
                BeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(factory.getObjectType())
                        .setScope(BeanDefinition.SCOPE_SINGLETON).setFactoryMethod("getObject")
                        .addConstructorArgValue(bean).getBeanDefinition();
                definition.setFactoryBeanName(factoryName);
                log.info("Registering {} of {}", beanName, definition);
                registry.registerBeanDefinition(beanName, definition);

            });
        });
    }

}
