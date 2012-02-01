package com.soletta.seek.util;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

/**
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibDigest {

    /**
     * Return a stream that calculates an MD5 digest of its contents.
     * 
     * @param out
     * @return DigestOutputStream
     */
    public static DigestOutputStream md5OutputStream(OutputStream out) {
        return new DigestOutputStream(out, md5());
    }

    /**
     * Return a filtering output stream that calculates the CRC.
     * 
     * @param out
     * @return CRCOutputStream<T>
     */
    public static <T extends OutputStream> CRCOutputStream<T> crcOutputStream(T out) {
        return new CRCOutputStream<T>(out);
    }

    /**
     * Returns a filtering input stream that calculates the CRC.
     * 
     * @param in
     * @return CRCInputStream<T>
     */
    public static <T extends InputStream> CRCInputStream<T> crcInputStream(T in) {
        return new CRCInputStream<T>(in);
    }

    /**
     * Method md5InputStream.
     * 
     * @param in
     *            InputStream
     * @return DigestInputStream
     */
    public static DigestInputStream md5InputStream(InputStream in) {
        return new DigestInputStream(in, md5());
    }

    /**
     * Method crcOverMd5InputStream.
     * 
     * @param in
     *            InputStream
     * @return CRCInputStream<DigestInputStream>
     */
    public static CRCInputStream<DigestInputStream> crcOverMd5InputStream(InputStream in) {
        return crcInputStream(md5InputStream(in));
    }

    /**
     * Method crcOverMd5OutputStream.
     * 
     * @param out
     *            OutputStream
     * @return CRCOutputStream<DigestOutputStream>
     */
    public static CRCOutputStream<DigestOutputStream> crcOverMd5OutputStream(OutputStream out) {
        return new CRCOutputStream<DigestOutputStream>(md5OutputStream(out));
    }

    /**
     * Method md5.
     * 
     * @return MessageDigest
     */
    public static MessageDigest md5() {
        return messageDigest("MD5");
    }

    /**
     * Method sha256.
     * 
     * @return MessageDigest
     */
    public static MessageDigest sha256() {
        return messageDigest("SHA-256");
    }

    /**
     * Method messageDigest.
     * 
     * @param algorithm
     *            String
     * @return MessageDigest
     */
    private static MessageDigest messageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public static abstract class TypedFilterInputStream<T extends InputStream> extends FilterInputStream {
        /**
         * Constructor for TypedFilterInputStream.
         * 
         * @param in
         *            T
         */
        public TypedFilterInputStream(T in) {
            super(in);
        }

        /**
         * Method getBaseInputStream.
         * 
         * @return T
         */
        @SuppressWarnings("unchecked")
        public T getBaseInputStream() {
            return (T) in;
        }
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public static abstract class TypedFilterOutputStream<T extends OutputStream> extends FilterOutputStream {
        /**
         * Constructor for TypedFilterOutputStream.
         * 
         * @param out
         *            T
         */
        public TypedFilterOutputStream(T out) {
            super(out);
        }

        /**
         * Method write.
         * 
         * @param b
         *            byte[]
         * @param off
         *            int
         * @param len
         *            int
         * @throws IOException
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
        }

        /**
         * Method getBaseOutputStream.
         * 
         * @return T
         */
        @SuppressWarnings("unchecked")
        public T getBaseOutputStream() {
            return (T) out;
        }
    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public static class CRCOutputStream<T extends OutputStream> extends TypedFilterOutputStream<T> {

        final CRC32 crc = new CRC32();

        /**
         * Constructor for CRCOutputStream.
         * 
         * @param out
         *            T
         */
        CRCOutputStream(T out) {
            super(out);
        }

        /**
         * Method write.
         * 
         * @param b
         *            int
         * @throws IOException
         */
        @Override
        public void write(int b) throws IOException {
            super.write(b);
            crc.update(b);
        }

        /**
         * Method write.
         * 
         * @param b
         *            byte[]
         * @param off
         *            int
         * @param len
         *            int
         * @throws IOException
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            super.write(b, off, len);
            crc.update(b, off, len);
        }

        /**
         * Method getCrc.
         * 
         * @return CRC32
         */
        public CRC32 getCrc() {
            return crc;
        }

        /**
         * Method getValue.
         * 
         * @return long
         */
        public long getValue() {
            return crc.getValue();
        }

    }

    /**
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    public static class CRCInputStream<T extends InputStream> extends TypedFilterInputStream<T> {

        final CRC32 crc = new CRC32();

        /**
         * Constructor for CRCInputStream.
         * 
         * @param in
         *            T
         */
        public CRCInputStream(T in) {
            super(in);
        }

        /**
         * Method read.
         * 
         * @return int * @throws IOException
         */
        @Override
        public int read() throws IOException {
            int item = super.read();
            if (item >= 0)
                crc.update(item);
            return item;
        }

        /**
         * Method read.
         * 
         * @param b
         *            byte[]
         * @param off
         *            int
         * @param len
         *            int
         * @return int * @throws IOException
         */
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = super.read(b, off, len);
            if (read > 0)
                crc.update(b, off, read);
            return read;
        }

        /**
         * Method getValue.
         * 
         * @return long
         */
        public long getValue() {
            return crc.getValue();
        }
    }

    /**
     * Method streamDigest.
     * 
     * @param inputStream
     *            InputStream
     * @return byte[] * @throws IOException
     */
    public static byte[] streamDigest(InputStream inputStream) throws IOException {

        DigestInputStream in = md5InputStream(inputStream);
        try {
            byte[] buffer = new byte[4096];
            int count = in.read(buffer);
            while (count >= 0)
                count = in.read(buffer);
        } finally {
            in.close();
        }

        return in.getMessageDigest().digest();
    }

}
