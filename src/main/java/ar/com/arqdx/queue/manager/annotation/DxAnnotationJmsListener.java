package ar.com.arqdx.queue.manager.annotation;


import jdk.nashorn.internal.objects.NativeDebug;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DxAnnotationJmsListener {
      String destination();
}