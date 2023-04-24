package ar.com.arqdx.queue.manager.ibmmq.configuration;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
@Order(1)
public class BrokerLoader {

    private  Map<String, String> queues;

    public BrokerLoader(Map<String, String> queues) {
        this.queues = queues;
    }

    public BrokerLoader() throws IOException {
        this.queues = loadQueues();
    }

    private Map<String, String> loadQueues() throws IOException {

        // se carga archivo de properties
        queues = new HashMap<String, String>();
        Properties configuration = new Properties();
        InputStream inputStream = BrokerLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties");
        configuration.load(inputStream);


        //////////////////////////
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
        /////////////////////////

        return queues;
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


    private static boolean isQueueName(String key) {
        return key.contains("brokers") & key.contains("queue") & key.contains("name");
    }

    public Map<String, String> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, String> queues) {
        this.queues = queues;
    }
}
