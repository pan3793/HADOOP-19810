## Background

Hadoop trunk (3.5.0-SNAPSHOT) moves to JDK 17+, FastByteComparisons now can be replaced by `Arrays.compareUnsigned`.

Guava v33.4.5 starts to use Arrays.compareUnsigned when it available.
https://github.com/google/guava/commit/b3bb29a54b8f13d6f6630b6cb929867adbf6b9a0

> The benchmarks suggest that the old, `Unsafe`-based implementation is faster up to 64 elements or so
> but that it's a matter of only about a nanosecond. The new implementation pulls ahead for larger arrays,
> including an advantage of about 5-10 ns for 1024 elements.

## Benchmark results

Ubuntu 24.04 LTS x86_64
CPU: Intel i5-9500 (6) @ 4.400GHz
Kernel: 6.17.9-76061709-generic
OpenJDK Runtime Environment Temurin-25.0.2+10 (build 25.0.2+10-LTS)
```
Benchmark                                (length)  Mode  Cnt   Score    Error  Units
BytesComparatorBenchmark.hadoopDiffLast         4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopDiffLast         8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopDiffLast        64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopDiffLast      1024    ss    5   0.001 ±  0.001   s/op
BytesComparatorBenchmark.hadoopDiffLast   1048576    ss    5   0.598 ±  0.026   s/op
BytesComparatorBenchmark.hadoopDiffLast   1048577    ss    5   0.711 ±  0.021   s/op
BytesComparatorBenchmark.hadoopDiffLast   6710884    ss    5   6.213 ±  0.152   s/op
BytesComparatorBenchmark.hadoopDiffLast   6710883    ss    5   6.198 ±  0.253   s/op

BytesComparatorBenchmark.jdkDiffLast            4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkDiffLast            8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkDiffLast           64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkDiffLast         1024    ss    5  ≈ 10⁻³            s/op
BytesComparatorBenchmark.jdkDiffLast      1048576    ss    5   0.391 ±  0.128   s/op
BytesComparatorBenchmark.jdkDiffLast      1048577    ss    5   0.377 ±  0.106   s/op
BytesComparatorBenchmark.jdkDiffLast      6710884    ss    5   4.955 ±  2.631   s/op
BytesComparatorBenchmark.jdkDiffLast      6710883    ss    5   4.310 ±  1.008   s/op

BytesComparatorBenchmark.hadoopEqual            4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopEqual            8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopEqual           64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopEqual         1024    ss    5   0.001 ±  0.001   s/op
BytesComparatorBenchmark.hadoopEqual      1048576    ss    5   0.610 ±  0.025   s/op
BytesComparatorBenchmark.hadoopEqual      1048577    ss    5   0.711 ±  0.016   s/op
BytesComparatorBenchmark.hadoopEqual      6710884    ss    5   6.512 ±  1.566   s/op
BytesComparatorBenchmark.hadoopEqual      6710883    ss    5   6.714 ±  1.409   s/op

BytesComparatorBenchmark.jdkEqual               4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkEqual               8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkEqual              64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkEqual            1024    ss    5  ≈ 10⁻³            s/op
BytesComparatorBenchmark.jdkEqual         1048576    ss    5   0.381 ±  0.123   s/op
BytesComparatorBenchmark.jdkEqual         1048577    ss    5   0.369 ±  0.049   s/op
BytesComparatorBenchmark.jdkEqual         6710884    ss    5   4.236 ±  0.252   s/op
BytesComparatorBenchmark.jdkEqual         6710883    ss    5   4.184 ±  0.236   s/op
```

CPU: Apple M1 Max
OpenJDK Runtime Environment Temurin-25.0.2+10 (build 25.0.2+10-LTS)
```
Benchmark                                (length)  Mode  Cnt   Score    Error  Units
BytesComparatorBenchmark.hadoopDiffLast         4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopDiffLast         8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopDiffLast        64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopDiffLast      1024    ss    5  ≈ 10⁻³            s/op
BytesComparatorBenchmark.hadoopDiffLast   1048576    ss    5   0.404 ±  0.015   s/op
BytesComparatorBenchmark.hadoopDiffLast   1048577    ss    5   0.392 ±  0.013   s/op
BytesComparatorBenchmark.hadoopDiffLast   6710884    ss    5   2.872 ±  0.057   s/op
BytesComparatorBenchmark.hadoopDiffLast   6710883    ss    5   2.893 ±  0.046   s/op

BytesComparatorBenchmark.jdkDiffLast            4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkDiffLast            8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkDiffLast           64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkDiffLast         1024    ss    5   0.001 ±  0.001   s/op
BytesComparatorBenchmark.jdkDiffLast      1048576    ss    5   0.399 ±  0.009   s/op
BytesComparatorBenchmark.jdkDiffLast      1048577    ss    5   0.390 ±  0.004   s/op
BytesComparatorBenchmark.jdkDiffLast      6710884    ss    5   2.913 ±  0.029   s/op
BytesComparatorBenchmark.jdkDiffLast      6710883    ss    5   2.897 ±  0.015   s/op

BytesComparatorBenchmark.hadoopEqual            4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopEqual            8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopEqual           64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.hadoopEqual         1024    ss    5  ≈ 10⁻³            s/op
BytesComparatorBenchmark.hadoopEqual      1048576    ss    5   0.393 ±  0.011   s/op
BytesComparatorBenchmark.hadoopEqual      1048577    ss    5   0.395 ±  0.012   s/op
BytesComparatorBenchmark.hadoopEqual      6710884    ss    5   2.877 ±  0.005   s/op
BytesComparatorBenchmark.hadoopEqual      6710883    ss    5   2.903 ±  0.023   s/op

BytesComparatorBenchmark.jdkEqual               4    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkEqual               8    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkEqual              64    ss    5  ≈ 10⁻⁴            s/op
BytesComparatorBenchmark.jdkEqual            1024    ss    5  ≈ 10⁻³            s/op
BytesComparatorBenchmark.jdkEqual         1048576    ss    5   0.406 ±  0.009   s/op
BytesComparatorBenchmark.jdkEqual         1048577    ss    5   0.390 ±  0.008   s/op
BytesComparatorBenchmark.jdkEqual         6710884    ss    5   2.900 ±  0.044   s/op
BytesComparatorBenchmark.jdkEqual         6710883    ss    5   2.912 ±  0.015   s/op
```

## Miscellaneous

Related JIRA tickets:
- https://issues.apache.org/jira/browse/HADOOP-14313
- https://issues.apache.org/jira/browse/HBASE-17877
