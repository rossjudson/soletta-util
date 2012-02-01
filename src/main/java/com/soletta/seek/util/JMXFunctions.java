package com.soletta.seek.util;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

//import com.google.common.base.Function;

/** JMXFunctions builds helper functions for use with google collections and JMX.
 * 
 * @author rjudson
 *
 */
public class JMXFunctions {

    private final MBeanServerConnection conn;
    
    /** Create a JMXFunctions helper that will send its requests over the given MBeanServerConnection.
     * 
     * @param conn
     */
    public JMXFunctions(MBeanServerConnection conn) {
        this.conn = conn;
    }

    /** Returns a function that transforms ObjectNames into proxies of the
     * given class.
     * 
     * @param <C>
     * @param cclass
     * @return
     */
    public <C> Function<ObjectName, C> proxy(Class<C> cclass) {
        return new Proxy<C>(cclass);
    }
//
//    
    class Proxy<C> implements Function<ObjectName,C>  {

        private final Class<C> cclass;
        
        Proxy(Class<C> cclass) {
            this.cclass = cclass;
        }

        @Override
        public C apply(ObjectName input) {
            return JMX.newMXBeanProxy(conn, input, cclass);
//            return new MBeanServerInvocationHandler(conn, input, true);
//            return MBeanServerInvocationHandler.newProxyInstance(conn, input, cclass, true);
        }
        
    }

}
