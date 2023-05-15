package ar.com.arqdx.queue.manager.annotation;

import ar.com.arqdx.queue.manager.listener.MQListner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class DxJmsListener2 implements DxAnnotationJmsListener2 {
    @Override
    public <T> boolean queueJmsListener(T o) {
        MQListner objetoDeMiClase = new MQListner();

        Class<? extends MQListner> objetoDeClassConInfoDeMiClase = objetoDeMiClase.getClass();

        for (Method method : objetoDeClassConInfoDeMiClase.getDeclaredMethods()) {
            if (method.isAnnotationPresent(DxAnnotationJmsListener.class)) {

                String annotationMessage = method.getAnnotation(DxAnnotationJmsListener.class).destination();

                // y si ademas, el mensaje recibido del socket, es igual al de la anotación
                if (annotationMessage != null) {
                    System.out.println("<<DESTINATION VALUE:: "+annotationMessage+" >>");




                    // entonces ejecutamos el método por reflection
                    String returnedMessage = null;


                }
            }


        }

        return true;
    }
}