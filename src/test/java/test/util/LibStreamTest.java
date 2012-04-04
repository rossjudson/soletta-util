package test.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;

import com.soletta.seek.util.LibStream;

/**
 */
public class LibStreamTest {

    static Random random = new Random();

    /**
     * Method simpleCopy.
     * 
     * @throws IOException
     */
    @Test
    public void simpleCopy() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream("Hello World".getBytes());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        long count = LibStream.cat2(bis, bos);
        System.out.format("Read %,d bytes\n", count);
    }

    /**
     * Method interruptCaller.
     * 
     * @throws InterruptedException
     */
    @Test
    public void interruptCaller() throws InterruptedException {

        final SuspendInputStream in = new SuspendInputStream();
        final SuspendOutputStream out = new SuspendOutputStream();
        final AtomicReference<Throwable> callerException = new AtomicReference<Throwable>();
        final AtomicLong bytesTransferred = new AtomicLong();

        Thread callerThread = new Thread() {
            public void run() {
                try {
                    bytesTransferred.set(LibStream.cat2(in, out));
                } catch (Throwable e) {
                    callerException.set(e);
                }
            }
        };

        callerThread.start();
        Thread.sleep(1000);
        callerThread.interrupt();

        callerThread.join(4000);
        if (callerThread.isAlive()) {
            dumpFrames(callerThread);
            Assert.fail("LibStream caller is still alive.");
        }

        Assert.assertTrue(bytesTransferred.get() == 0);
        Assert.assertTrue(callerException.get() instanceof InterruptedIOException);
    }

    /**
     * Method dumpFrames.
     * 
     * @param callerThread
     *            Thread
     */
    private void dumpFrames(Thread callerThread) {
        for (StackTraceElement se : callerThread.getStackTrace()) {
            System.out.println(se);
        }
    }

    /**
     * Method interruptReader.
     * 
     * @throws InterruptedException
     */
    @Test
    public void interruptReader() throws InterruptedException {

        final SuspendInputStream in = new SuspendInputStream();
        final SuspendOutputStream out = new SuspendOutputStream();
        final AtomicReference<Throwable> callerException = new AtomicReference<Throwable>();
        final AtomicLong bytesTransferred = new AtomicLong();

        Thread callerThread = new Thread() {
            public void run() {
                try {
                    bytesTransferred.set(LibStream.cat2(in, out));
                } catch (Throwable e) {
                    callerException.set(e);
                }
            }
        };

        callerThread.start();
        Thread.sleep(1000);
        in.reader.get().interrupt();

        callerThread.join(2000);
        if (callerThread.isAlive())
            Assert.fail("LibStream caller is still alive.");

        Assert.assertTrue(bytesTransferred.get() == 0);
        Assert.assertTrue(callerException.get() instanceof InterruptedIOException);
    }

    /**
     * Method errorReader.
     * 
     * @throws InterruptedException
     */
    @Test
    public void errorReader() throws InterruptedException {

        final SuspendInputStream in = new SuspendInputStream();
        final SuspendOutputStream out = new SuspendOutputStream();
        final AtomicReference<Throwable> callerException = new AtomicReference<Throwable>();
        final AtomicLong bytesTransferred = new AtomicLong();

        Thread callerThread = new Thread() {
            public void run() {
                try {
                    bytesTransferred.set(LibStream.cat2(in, out));
                } catch (Throwable e) {
                    callerException.set(e);
                }
            }
        };

        callerThread.start();
        Thread.sleep(1000);
        in.reader.get().interrupt();

        callerThread.join(2000);
        if (callerThread.isAlive()) {
            dumpFrames(callerThread);
            Assert.fail("LibStream caller is still alive.");
        }

        Assert.assertTrue(bytesTransferred.get() == 0);
        Assert.assertTrue(callerException.get() instanceof InterruptedIOException);
    }

    /**
     */
    class SuspendInputStream extends InputStream {
        AtomicBoolean suspended = new AtomicBoolean();
        AtomicReference<Thread> reader = new AtomicReference<Thread>();

        byte next = 0;

        /**
         * Method read.
         * 
         * @return int
         * @throws IOException
         */
        @Override
        public int read() throws IOException {
            reader.set(Thread.currentThread());
            while (suspended.get()) {
                if (Thread.interrupted()) {
                    throw new InterruptedIOException();
                }

            }
            return next++;
        }
    }

    /**
     */
    class SuspendOutputStream extends OutputStream {
        AtomicBoolean suspended = new AtomicBoolean();

        /**
         * Method write.
         * 
         * @param b
         *            int
         * @throws IOException
         */
        @Override
        public void write(int b) throws IOException {
            while (suspended.get()) {
                if (Thread.interrupted())
                    throw new InterruptedIOException();
            }
        }

    }
}
