package com.soletta.seek.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibStream {

    static ExecutorService exec = Executors.newCachedThreadPool(Threads.groupedThreadFactory("LibStream"));

    /**
     * Copy everything from in to out in an efficient way, possibly multi-threaded, without closing the streams.
     * 
     * @param in
     * @param out
     * @return long * @throws IOException
     */
    public static long cat1(InputStream in, OutputStream out) throws IOException {
        long count = 0;
        byte[] buffer = new byte[16 * 1024];
        int read = in.read(buffer);
        while (read >= 0) {
            count += read;
            out.write(buffer, 0, read);
            read = in.read(buffer);
        }
        return count;
    }

    /**
     * Method cat2.
     * 
     * @param in
     *            InputStream
     * @param out
     *            OutputStream
     * @return long * @throws IOException
     */
    public static long cat2(InputStream in, OutputStream out) throws IOException {

        long ioTimeout = Long.getLong("LibStream.IOTimeoutMillis", 30000);
        Reader r = new Reader(in, ioTimeout);
        Buffer buffer = new Buffer();
        Future<Long> future = exec.submit(r);

        try {
            do {
                buffer = r.exchange(buffer, ioTimeout, TimeUnit.MILLISECONDS);
                if (buffer != null)
                    out.write(buffer.bytes, 0, buffer.length);
            } while (buffer != null);

        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        } catch (TimeoutException e) {
            throw new IOException("Reader stalled", e);
        } finally {
            if (buffer != null)
                try {
                    r.exchange(null);
                } catch (InterruptedException e) {
                    throw new InterruptedIOException();
                }
        }
        try {
            return future.get();
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        } catch (ExecutionException e) {
            Throwable c = e.getCause();
            if (c instanceof InterruptedException)
                throw new InterruptedIOException();
            else if (c instanceof Error) {
                throw (Error) c;
            } else if (c instanceof RuntimeException) {
                throw (RuntimeException) c;
            } else if (c instanceof IOException) {
                throw (IOException) c;
            } else if (c instanceof TimeoutException) {
                throw new IOException("Reader stalled", c);
            } else {
                throw new IOException(c);
            }
        }
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static final class Reader extends Exchanger<Buffer> implements Callable<Long> {

        private InputStream input;
        private Buffer buffer = new Buffer();
        private final long ioTimeout;

        /**
         * Constructor for Reader.
         * 
         * @param input
         *            InputStream
         * @param ioTimeout
         *            long
         */
        Reader(InputStream input, long ioTimeout) {
            this.input = input;
            this.ioTimeout = ioTimeout;
        }

        /**
         * Method call.
         * 
         * @return Long * @throws Exception * @see java.util.concurrent.Callable#call()
         */
        @Override
        public Long call() throws Exception {
            long count = 0;
            try {
                buffer.length = input.read(buffer.bytes);
                while (buffer.length >= 0) {
                    count += buffer.length;
                    // note that exchange gives us a happens-before, so we are
                    // free to
                    // look at the variables of the buffer object without
                    // additional
                    // synchronization
                    buffer = exchange(buffer, ioTimeout, TimeUnit.MILLISECONDS);
                    if (buffer == null)
                        break;
                    buffer.length = input.read(buffer.bytes);
                }
                return count;
            } finally {
                if (buffer != null) {
                    exchange(null);
                    buffer = null;
                }
                input = null;
            }
        }

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static class Buffer {
        final byte[] bytes = new byte[16 * 1024];
        int length;
    }

}
