package ar.com.arqdx.queue.manager.annotation;


import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(QueueListeners.class)
@MessageMapping
public @interface DxAnnotationJmsListener {
    String id() default "";

    String containerFactory() default "";

    String destination();

    String subscription() default "";

    String selector() default "";

    String concurrency() default "";
}