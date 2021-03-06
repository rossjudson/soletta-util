package com.soletta.seek.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * A PerThread that is restricted to classes that extend Closeable, which includes a lot of standard Java classes.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
abstract public class CloseablePerThread<T extends Closeable> extends AbstractPerThread<T, IOException> implements Closeable {

    /**
     * Method dispose.
     * 
     * @param t
     *            T
     * @throws IOException
     */
    @Override
    protected void dispose(T t) throws IOException {
        t.close();
    }

}
