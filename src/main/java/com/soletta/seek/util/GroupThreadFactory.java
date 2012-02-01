package com.soletta.seek.util;

import java.util.concurrent.ThreadFactory;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public interface GroupThreadFactory extends ThreadFactory {
    /**
     * Method getThreadGroup.
     * 
     * @return ThreadGroup
     */
    ThreadGroup getThreadGroup();
}
