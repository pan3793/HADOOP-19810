package io.github.pan3793;

import org.apache.hadoop.io.HadoopBridge;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Fork(1)
@State(Scope.Benchmark)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.SingleShotTime)
public class BytesComparatorBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options options =
                new OptionsBuilder()
                        .include(BytesComparatorBenchmark.class.getSimpleName())
                        .build();
        new Runner(options).run();
    }

    private byte[] ba1;
    private byte[] ba2;
    private byte[] ba3;
    private byte[] ba4;

    // 4, 8, 64, 1K, 1M, 1M (unaligned), 64M, 64M (unaligned)
    @Param({"4", "8", "64", "1024", "1048576", "1048577", "6710884", "6710883"})
    // @Param({"4", "8", "64", "1024"})
    private int length;
    private final int NUM = 10000;

    @Setup
    public void setup() {
        Random r = new Random();
        ba1 = new byte[length];
        r.nextBytes(ba1);
        ba2 = Arrays.copyOf(ba1, ba1.length);
        // Differ at the last element
        ba3 = Arrays.copyOf(ba1, ba1.length);
        ba4 = Arrays.copyOf(ba1, ba1.length);
        ba3[ba1.length - 1] = (byte) 43;
        ba4[ba1.length - 1] = (byte) 42;
    }

    @Benchmark
    public void hadoopEqual() {
        for (int i = 0; i < NUM; ++i) {
            if (HadoopBridge.compareTo(ba1, 0, ba1.length, ba2, 0, ba2.length) != 0) {
                throw new Error(); // deoptimization
            }
        }
    }

    @Benchmark
    public void jdkEqual() {
        for (int i = 0; i < NUM; ++i) {
            if (Arrays.compareUnsigned(ba1, 0, ba1.length, ba2, 0, ba2.length) != 0) {
                throw new Error(); // deoptimization
            }
        }
    }

    @Benchmark
    public void hadoopDiffLast() {
        for (int i = 0; i < NUM; ++i) {
            if (HadoopBridge.compareTo(ba3, 0, ba3.length, ba4, 0, ba4.length) == 0) {
                throw new Error(); // deoptimization
            }
        }
    }

    @Benchmark
    public void jdkDiffLast() {
        for (int i = 0; i < NUM; ++i) {
            if (Arrays.compareUnsigned(ba3, 0, ba3.length, ba4, 0, ba4.length) == 0) {
                throw new Error(); // deoptimization
            }
        }
    }
}
