package com.soletta.seek.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Locale;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibBeans {

    /**
     * Return a possibly nested property value from a target object, returning the default value if the property can't
     * be found. Note that if the property has a null value, null will be returned, not the provided default.
     * 
     * @param target
     * @param dottedProperties
     * @param defaultValue
     * @return K
     */
    @SuppressWarnings("unchecked")
    public static <K> K getProperty(Object target, String dottedProperties, K defaultValue) {

        if (target == null)
            return defaultValue;

        int firstDot = dottedProperties.indexOf('.');
        if (firstDot == -1) {
            firstDot = dottedProperties.length();
        }
        String first = dottedProperties.substring(0, firstDot);
        String rest = dottedProperties.substring(Math.min(firstDot + 1, dottedProperties.length()));

        try {
            BeanInfo bi = Introspector.getBeanInfo(target.getClass());
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(first)) {
                    Object result = pd.getReadMethod().invoke(target);
                    return rest.length() == 0 ? (K) result : getProperty(result, rest, defaultValue);
                }
            }

            return defaultValue;

        } catch (Throwable e) {
            throw new RuntimeException("Failed to call method: " + first + " on " + target, e);
        }
    }

    /**
     * Method capitalize.
     * 
     * @param name
     *            String
     * @return String
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
    }

}
