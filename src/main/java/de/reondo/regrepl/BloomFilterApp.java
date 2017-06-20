package de.reondo.regrepl;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dw on 20.06.2017.
 */
public class BloomFilterApp {


    private static final int M = 1000000;

    public static void main(String[] args) throws IOException {
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), M, 0.01);
        for (int i = 0; i < M; ++i) {
            bloomFilter.put(i);
        }
        for (int i = 0; i < M; ++i) {
            assert bloomFilter.mightContain(i);
        }
        int fpCount = 0;
        final int NEG = 2 * M;
        for (int i = M; i < M + NEG; ++i) {
            if (bloomFilter.mightContain(i)) {
                fpCount++;
            }
        }
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        bloomFilter.writeTo(bOut);
        System.out.printf("Number of false positives: %d. Rate: %.2f\n", fpCount, ((double)fpCount)/NEG);
        int length = bOut.toByteArray().length;
        System.out.printf("Size of bloom filter: %d bytes (%.1f bytes per entry)", length, ((double)length)/M);

        /*
         * Number of false positives: 20339. Rate: 0.01
         * Size of bloom filter: 1198142 bytes (1.2 bytes per entry)
         */
    }
}
