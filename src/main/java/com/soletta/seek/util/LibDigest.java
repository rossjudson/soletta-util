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

public class LibDigest {

    /** Return a stream that calculates an MD5 digest of its contents.
     * 
     * @param out
     * @return
     */
    public static DigestOutputStream md5OutputStream(OutputStream out) {
        return new DigestOutputStream(out, md5());
    }
    
    /** Return a filtering output stream that calculates the CRC.
     * 
     * @param <T>
     * @param out
     * @return
     */
    public static <T extends OutputStream> CRCOutputStream<T> crcOutputStream(T out) {
        return new CRCOutputStream<T>(out);
    }

    /** Returns a filtering input stream that calculates the CRC.
     * 
     * @param <T>
     * @param in
     * @return
     */
    public static <T extends InputStream> CRCInputStream<T> crcInputStream(T in) {
        return new CRCInputStream<T>(in);
    }
    
    public static DigestInputStream md5InputStream(InputStream in) {
        return new DigestInputStream(in, md5());
    }
    
    public static CRCInputStream<DigestInputStream> crcOverMd5InputStream(InputStream in) {
        return crcInputStream(md5InputStream(in));
    }
    
    public static CRCOutputStream<DigestOutputStream> crcOverMd5OutputStream(OutputStream out) {
        return new CRCOutputStream<DigestOutputStream>(md5OutputStream(out));
    }
    
    public static MessageDigest md5() {
        return messageDigest("MD5");
    }

    public static MessageDigest sha256() {
        return messageDigest("SHA-256");
    }

    private static MessageDigest messageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static abstract class TypedFilterInputStream<T extends InputStream> extends FilterInputStream {
        public TypedFilterInputStream(T in) {
            super(in);
        }
        
        @SuppressWarnings("unchecked")
        public T getBaseInputStream() {
            return (T)in;
        }
    }
    
    public static abstract class TypedFilterOutputStream<T extends OutputStream> extends FilterOutputStream {
        public TypedFilterOutputStream(T out) {
            super(out);
        }

        
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
        }

        @SuppressWarnings("unchecked")
        public T getBaseOutputStream() {
            return (T)out;
        }
    }
    
    public static class CRCOutputStream<T extends OutputStream> extends TypedFilterOutputStream<T> {

        final CRC32 crc = new CRC32();
        
        CRCOutputStream(T out) {
            super(out);
        }

        @Override
        public void write(int b) throws IOException {
            super.write(b);
            crc.update(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            super.write(b, off, len);
            crc.update(b, off, len);
        }
        
        public CRC32 getCrc() {
            return crc;
        }
        
        public long getValue() {
            return crc.getValue();
        }
        
    }
    
    public static class CRCInputStream<T extends InputStream> extends TypedFilterInputStream<T> {

        final CRC32 crc = new CRC32();
        
        public CRCInputStream(T in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int item = super.read();
            if (item >= 0)
                crc.update(item);
            return item;
        }


        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = super.read(b, off, len);
            if (read > 0)
                crc.update(b, off, read);
            return read;
        }
        
        public long getValue() {
            return crc.getValue();
        }
    }

    public static byte[] streamDigest(InputStream inputStream) throws IOException {
        
        DigestInputStream in = md5InputStream(inputStream);
        try {
            byte [] buffer = new byte[4096];
            int count = in.read(buffer);
            while (count >= 0) 
                count = in.read(buffer);
        } finally {
            in.close();
        }
        
        return in.getMessageDigest().digest();
    }
    
    
}
