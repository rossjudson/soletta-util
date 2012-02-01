package com.soletta.seek.util.args;

public @interface ArgParser {
    
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
    

}
