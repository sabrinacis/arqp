package ar.com.arqdx.queue.manager.ibmmq.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Procesador
 */
@Configuration
@ConditionalOnProperty(name = "mq", matchIfMissing = true)
public class MultiBeanFactoryConfig {

    @Bean
    public BrokerLoader getBrokerLoader() throws IOException {
        return new BrokerLoader();
    }

    @Bean
    public static MultiBeanFactoryPostProcessor multiBeanFactoryPostProcessor() {
        return new MultiBeanFactoryPostProcessor();
    }

}