package org.apache.hadoop.io;

public class HadoopBridge {

    public static int compareTo(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {
        return FastByteComparisons.compareTo(b1, s1, l1, b2, s2, l2);
    }
}
