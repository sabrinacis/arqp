package ar.com.arqdx.queue.manager.ibmmq.configuration;

import java.util.Collection;

/**
 *  interfaz para la definición dinámica de múltiples beans
 */
public interface MultiBeanFactory<T> {

    String getBeanName(String name);



    Class<?> getObjectType();

    Collection<String> getNames();

}