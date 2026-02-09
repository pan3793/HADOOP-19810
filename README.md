## Background

Hadoop trunk (3.5.0-SNAPSHOT) moves to JDK 17+, FastByteComparisons now can be replaced by `Arrays.compareUnsigned`.

Guava v33.4.5 starts to use Arrays.compareUnsigned when it available.
https://github.com/google/guava/commit/b3bb29a54b8f13d6f6630b6cb929867adbf6b9a0

> The benchmarks suggest that the old, `Unsafe`-based implementation is faster up to 64 elements or so
> but that it's a matter of only about a nanosecond. The new implementation pulls ahead for larger arrays,
> including an advantage of about 5-10 ns for 1024 elements.

## Benchmark results

- Ubuntu 24.04 LTS x86_64
- CPU: Intel i5-9500 (6) @ 4.400GHz
- Kernel: 6.17.9-76061709-generic
- OpenJDK Runtime Environment Temurin-25.0.2+10 (build 25.0.2+10-LTS)
```
Benchmark                                  (length)  Mode  Cnt  Score   Error  Units
BytesComparatorBenchmark.diff_last_hadoop       128    ss    3  0.319 ± 0.023   s/op
BytesComparatorBenchmark.diff_last_hadoop       256    ss    3  0.505 ± 0.008   s/op
BytesComparatorBenchmark.diff_last_hadoop       512    ss    3  0.879 ± 0.189   s/op
BytesComparatorBenchmark.diff_last_hadoop      1024    ss    3  1.684 ± 0.141   s/op
BytesComparatorBenchmark.diff_last_hadoop      2048    ss    3  3.233 ± 0.095   s/op
BytesComparatorBenchmark.diff_last_jdk          128    ss    3  0.379 ± 0.046   s/op
BytesComparatorBenchmark.diff_last_jdk          256    ss    3  0.491 ± 0.024   s/op
BytesComparatorBenchmark.diff_last_jdk          512    ss    3  0.685 ± 0.055   s/op
BytesComparatorBenchmark.diff_last_jdk         1024    ss    3  1.180 ± 0.052   s/op
BytesComparatorBenchmark.diff_last_jdk         2048    ss    3  1.929 ± 0.081   s/op
BytesComparatorBenchmark.equal_hadoop           128    ss    3  0.318 ± 0.052   s/op
BytesComparatorBenchmark.equal_hadoop           256    ss    3  0.502 ± 0.005   s/op
BytesComparatorBenchmark.equal_hadoop           512    ss    3  0.957 ± 0.086   s/op
BytesComparatorBenchmark.equal_hadoop          1024    ss    3  1.696 ± 0.174   s/op
BytesComparatorBenchmark.equal_hadoop          2048    ss    3  3.216 ± 0.219   s/op
BytesComparatorBenchmark.equal_jdk              128    ss    3  0.380 ± 0.155   s/op
BytesComparatorBenchmark.equal_jdk              256    ss    3  0.479 ± 0.053   s/op
BytesComparatorBenchmark.equal_jdk              512    ss    3  0.741 ± 0.094   s/op
BytesComparatorBenchmark.equal_jdk             1024    ss    3  1.421 ± 0.187   s/op
BytesComparatorBenchmark.equal_jdk             2048    ss    3  1.872 ± 0.209   s/op
```

- CPU: Apple M1 Max
- OpenJDK Runtime Environment Temurin-25.0.2+10 (build 25.0.2+10-LTS)
```
Benchmark                                  (length)  Mode  Cnt  Score   Error  Units
BytesComparatorBenchmark.diff_last_hadoop       128    ss    3  0.279 ± 0.123   s/op
BytesComparatorBenchmark.diff_last_hadoop       256    ss    3  0.441 ± 0.009   s/op
BytesComparatorBenchmark.diff_last_hadoop       512    ss    3  0.788 ± 0.037   s/op
BytesComparatorBenchmark.diff_last_hadoop      1024    ss    3  1.442 ± 0.201   s/op
BytesComparatorBenchmark.diff_last_hadoop      2048    ss    3  2.845 ± 0.177   s/op
BytesComparatorBenchmark.diff_last_jdk          128    ss    3  0.306 ± 0.002   s/op
BytesComparatorBenchmark.diff_last_jdk          256    ss    3  0.492 ± 0.204   s/op
BytesComparatorBenchmark.diff_last_jdk          512    ss    3  0.867 ± 0.315   s/op
BytesComparatorBenchmark.diff_last_jdk         1024    ss    3  1.467 ± 0.179   s/op
BytesComparatorBenchmark.diff_last_jdk         2048    ss    3  2.871 ± 0.129   s/op
BytesComparatorBenchmark.equal_hadoop           128    ss    3  0.253 ± 0.007   s/op
BytesComparatorBenchmark.equal_hadoop           256    ss    3  0.426 ± 0.018   s/op
BytesComparatorBenchmark.equal_hadoop           512    ss    3  0.772 ± 0.015   s/op
BytesComparatorBenchmark.equal_hadoop          1024    ss    3  1.428 ± 0.030   s/op
BytesComparatorBenchmark.equal_hadoop          2048    ss    3  2.842 ± 0.173   s/op
BytesComparatorBenchmark.equal_jdk              128    ss    3  0.294 ± 0.007   s/op
BytesComparatorBenchmark.equal_jdk              256    ss    3  0.474 ± 0.009   s/op
BytesComparatorBenchmark.equal_jdk              512    ss    3  0.842 ± 0.075   s/op
BytesComparatorBenchmark.equal_jdk             1024    ss    3  1.448 ± 0.029   s/op
BytesComparatorBenchmark.equal_jdk             2048    ss    3  2.854 ± 0.153   s/op
```

## Miscellaneous

Related JIRA tickets:
- https://issues.apache.org/jira/browse/HADOOP-14313
- https://issues.apache.org/jira/browse/HBASE-17877
