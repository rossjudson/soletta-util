package com.soletta.seek.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface EventHandler {
    /**
     * Method value.
     * 
     * @return String
     */
    String value() default "";

    /**
     * Method key.
     * 
     * @return String
     */
    String key() default "";

    /**
     * Method markers.
     * 
     * @return Class<? extends Annotation>[]
     */
    Class<? extends Annotation>[] markers() default {};
}
