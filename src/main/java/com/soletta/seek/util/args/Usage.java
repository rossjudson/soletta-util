package com.soletta.seek.util.args;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Attach a usage annotation to your configuration bean if you want to provide additional 
 * information about how to run your program, or guide how the usage is presented.
 * 
 * @author rjudson
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Usage {
    /** A header string that will be shown first, in the usage information.
     * 
     * @return
     */
    String value() default "";
    /** Footer information that will be shown after the parameters are shown.
     * 
     * @return
     */
    String footer() default "";
    
}
