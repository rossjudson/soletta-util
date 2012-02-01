package com.soletta.seek.util;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

//import com.google.common.base.Function;

/**
 * JMXFunctions builds helper functions for use with google collections and JMX.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class JMXFunctions {

    private final MBeanServerConnection conn;

    /**
     * Create a JMXFunctions helper that will send its requests over the given MBeanServerConnection.
     * 
     * @param conn
     */
    public JMXFunctions(MBeanServerConnection conn) {
        this.conn = conn;
    }

    /**
     * Returns a function that transforms ObjectNames into proxies of the given class.
     * 
     * @param cclass
     * @return Function<ObjectName,C>
     */
    public <C> Function<ObjectName, C> proxy(Class<C> cclass) {
        return new Proxy<C>(cclass);
    }

    //
    //
    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    class Proxy<C> implements Function<ObjectName, C> {

        private final Class<C> cclass;

        /**
         * Constructor for Proxy.
         * 
         * @param cclass
         *            Class<C>
         */
        Proxy(Class<C> cclass) {
            this.cclass = cclass;
        }

        /**
         * Method apply.
         * 
         * @param input
         *            ObjectName
         * @return C
         */
        @Override
        public C apply(ObjectName input) {
            return JMX.newMXBeanProxy(conn, input, cclass);
            // return new MBeanServerInvocationHandler(conn, input, true);
            // return MBeanServerInvocationHandler.newProxyInstance(conn, input,
            // cclass, true);
        }

    }

}
