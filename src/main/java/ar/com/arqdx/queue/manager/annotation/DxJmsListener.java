package ar.com.arqdx.queue.manager.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
@Component
public class DxJmsListener  implements DxAnnotationJmsListener{

    public <T> boolean queueJmsListener(T objeto) {
        Class<?> clase = objeto.getClass();
        for ( Method i1 : clase.getDeclaredMethods()) {
            try {
                final Method[] metodos = clase.getDeclaredMethods();
                for (final Method metodo : metodos) {
                    System.out.println("\nNombre del MÉTODO: " + metodo.getName());
                    System.out.println("  Cantidad de parámetros: " + metodo.getParameterCount());

                    if (i1.isAnnotationPresent(DxAnnotationJmsListener.class)) {
                        System.out.println(" >>>>>> MÉTODO ANOTADO: " + metodo.getName());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return true;
    }



    @Override
    public String destination() {

        return "true";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
