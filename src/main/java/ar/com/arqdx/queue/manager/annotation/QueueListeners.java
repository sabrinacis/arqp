package ar.com.arqdx.queue.manager.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueueListeners {
    DxAnnotationJmsListener[] value();
}