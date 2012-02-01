package com.soletta.seek.util.args;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Attach this annotation to the getter methods of your JavaBean, if you need 
 * to refine the default behaviors.
 * 
 * @author rjudson
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Arg {
    
    /** Specifies the name of this property, as an argument.
     * 
     * @return
     */
    String name() default "";
    
    /** Provide a description of this argument, for usage.
     * 
     * @return
     */
    String description() default "";
    
    /** For any given option group, you can define a property with definesGroup set to
     * true. That property is then a "meta-property", providing the description, long name,
     * etc for each of the properties in its group. The defineGroup property <em>must</em>
     * specify a character shortcut.
     * 
     * @return
     */
    boolean definesGroup() default false;
    
    /** Options are sometimes grouped together into two letter combinations, like -Tn and -Tk.
     * Specifying a grouping character tells LibArgs to look for these. The character given
     * here can match against a property that has definesGroup specified, in which case the 
     * long name will be provied in dotted form.
     * 
     * @return
     */
    String group() default "";
    
    /** Indicates the single character that should be an alias for this argument.
     * 
     * @return
     */
    char single() default (char)0;
    
    /** Specifies a required, positional argument.
     * 
     * @return
     */
    int pos() default -1;
    
    /** Indicates that this argument is required. This is useful for named arguments.
     * 
     * @return
     */
    boolean required() default false;
    /** Indicates that this parameter gets the remaining arguments. The type should be a List of 
     * something. 
     * 
     * @return
     */
    boolean remaining() default false;
    /** Specifies a class whose public static methods will be searched to find a way of
     * creating an object from a string. 
     * @return
     */
    Class<?> parseClass() default String.class;
    /** Specifies the name of a static method on the parse class that will be called.
     * 
     * @return
     */
    String parseMethod() default "";
    
    /** Specifies other names for a parameter.
     * 
     */
    String [] aliases() default {};
    
    /** Hide this parameter from the usage explanation.
     * 
     */
    boolean hide() default false;
    
    
}


