

# Native

```txt
root@lesco-Lenovo-IdeaPad-S145-15API:/home/lesco/dev/benchmark-microservice1/code-with-quarkus# perf stat -a ./target/code-with-quarkus-1.0.0-SNAPSHOT-runner
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2024-05-29 08:37:31,080 INFO  [io.quarkus] (main) code-with-quarkus 1.0.0-SNAPSHOT native (powered by Quarkus 3.10.2) started in 0.076s. Listening on: http://0.0.0.0:8081
2024-05-29 08:37:31,081 INFO  [io.quarkus] (main) Profile prod activated.
2024-05-29 08:37:31,081 INFO  [io.quarkus] (main) Installed features: [cdi, rest, rest-client, rest-client-jackson, rest-jackson, smallrye-context-propagation, vertx]
^C2024-05-29 08:40:52,719 INFO  [io.quarkus] (Shutdown thread) code-with-quarkus stopped in 0.009s

 Performance counter stats for 'system wide':

      1.614.430,45 msec cpu-clock                        #    8,000 CPUs utilized
         8.205.230      context-switches                 #    5,082 K/sec
           759.148      cpu-migrations                   #  470,227 /sec
           603.966      page-faults                      #  374,105 /sec
 1.661.657.756.001      cycles                           #    1,029 GHz                         (83,33%)
   556.299.263.343      stalled-cycles-frontend          #   33,48% frontend cycles idle        (83,33%)
   124.644.045.893      stalled-cycles-backend           #    7,50% backend cycles idle         (83,33%)
   579.436.517.564      instructions                     #    0,35  insn per cycle
                                                  #    0,96  stalled cycles per insn     (83,33%)
   129.754.021.310      branches                         #   80,371 M/sec                       (83,33%)
     8.293.039.468      branch-misses                    #    6,39% of all branches             (83,33%)

     201,803348410 seconds time elapsed
```

## k6 benchmark result

```txt
     scenarios: (100.00%) 1 scenario, 4 max VUs, 2m0s max duration (incl. graceful stop):
              * default: Up to 4 looping VUs for 1m30s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)

       ✓ status code should be 200

     checks.........................: 100.00% ✓ 134163      ✗ 0
     data_received..................: 132 MB  1.5 MB/s
     data_sent......................: 11 MB   127 kB/s
     group_duration.................: avg=1.5ms   min=939.63µs med=1.41ms  max=40.22ms  p(90)=1.7ms   p(95)=1.97ms
     http_req_blocked...............: avg=8.64µs  min=4.4µs    med=8.1µs   max=1.72ms   p(90)=10.12µs p(95)=10.89µs
     http_req_connecting............: avg=6ns     min=0s       med=0s      max=249.12µs p(90)=0s      p(95)=0s
     http_req_duration..............: avg=1.36ms  min=839.13µs med=1.27ms  max=31.88ms  p(90)=1.54ms  p(95)=1.8ms
       { expected_response:true }...: avg=1.36ms  min=839.13µs med=1.27ms  max=31.88ms  p(90)=1.54ms  p(95)=1.8ms
     http_req_failed................: 0.00%   ✓ 0           ✗ 134163
     http_req_receiving.............: avg=64.74µs min=26.81µs  med=62.36µs max=1.48ms   p(90)=78.92µs p(95)=88.27µs
     http_req_sending...............: avg=21.8µs  min=7.54µs   med=20.6µs  max=716.28µs p(90)=26.61µs p(95)=29.19µs
     http_req_tls_handshaking.......: avg=0s      min=0s       med=0s      max=0s       p(90)=0s      p(95)=0s
     http_req_waiting...............: avg=1.28ms  min=769.99µs med=1.19ms  max=31.62ms  p(90)=1.44ms  p(95)=1.69ms
     http_reqs......................: 134163  1490.681975/s
     iteration_duration.............: avg=1.53ms  min=969.17µs med=1.44ms  max=40.37ms  p(90)=1.73ms  p(95)=2.01ms
     iterations.....................: 134163  1490.681975/s
     vus............................: 1       min=1         max=4
     vus_max........................: 4       min=4         max=4


running (1m30.0s), 0/4 VUs, 134163 complete and 0 interrupted iterations
default ✓ [======================================] 0/4 VUs  1m30s
```

## wrk

`$ wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello`

Max CPU: 29,56%

In this test, the JVM initially uses more CPU than native, but
then it uses < 22% when it becomes hot.

But higher CPU usage doesn't always mean less efficient. It would be if
the native was serving less requests, but it was able to
serve more requests (194).

On the other hand, native uses less memory.

What is more expensive in cloud cost: cpu or memory?

Each kind of application requires its own calculation.

```txt
Running 20s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread calibration: mean lat.: 1.933ms, rate sampling interval: 10ms
  ...
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.93ms  715.58us  19.97ms   89.20%
    Req/Sec   212.73     46.77   555.00     81.01%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.86ms
 75.000%    2.17ms
 90.000%    2.49ms
 99.000%    3.70ms
 99.900%   10.83ms
 99.990%   16.14ms
 99.999%   19.98ms
100.000%   19.98ms

#[Mean    =        1.926, StdDeviation   =        0.716]
#[Max     =       19.968, Total count    =        19894]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  40004 requests in 20.00s, 37.61MB read
Requests/sec:   2000.27
Transfer/sec:      1.88MB
```

# JVM

```txt
root@lesco-Lenovo-IdeaPad-S145-15API:/home/lesco/dev/benchmark-microservice1/code-with-quarkus# perf stat -a /home/lesco/Softwares/graalvm-jdk-21_linux-x64_bin/graalvm-jdk-21.0.1+12.1/bin/java -jar target/quarkus-app/quarkus-run.jar
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2024-05-29 08:44:15,763 INFO  [io.quarkus] (main) code-with-quarkus 1.0.0-SNAPSHOT on JVM (powered by Quarkus 3.10.2) started in 1.640s. Listening on: http://0.0.0.0:8081
2024-05-29 08:44:15,767 INFO  [io.quarkus] (main) Profile prod activated.
2024-05-29 08:44:15,768 INFO  [io.quarkus] (main) Installed features: [cdi, rest, rest-client, rest-client-jackson, rest-jackson, smallrye-context-propagation, vertx]
^C2024-05-29 08:45:59,190 INFO  [io.quarkus] (main) code-with-quarkus stopped in 0.074s

 Performance counter stats for 'system wide':

        843.330,36 msec cpu-clock                        #    8,000 CPUs utilized
         5.735.346      context-switches                 #    6,801 K/sec
           676.026      cpu-migrations                   #  801,615 /sec
           332.248      page-faults                      #  393,971 /sec
 1.396.189.380.176      cycles                           #    1,656 GHz                         (83,33%)
   460.967.451.968      stalled-cycles-frontend          #   33,02% frontend cycles idle        (83,33%)
    79.466.686.729      stalled-cycles-backend           #    5,69% backend cycles idle         (83,34%)
   441.575.469.047      instructions                     #    0,32  insn per cycle
                                                  #    1,04  stalled cycles per insn     (83,33%)
    98.387.092.557      branches                         #  116,665 M/sec                       (83,34%)
     5.693.829.032      branch-misses                    #    5,79% of all branches             (83,34%)

     105,416088205 seconds time elapsed
```

## k6 benchmark result

```txt
$ k6 run k6_bench1.js
     scenarios: (100.00%) 1 scenario, 4 max VUs, 2m0s max duration (incl. graceful stop):
              * default: Up to 4 looping VUs for 1m30s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)

       ✓ status code should be 200

     checks.........................: 100.00% ✓ 91304       ✗ 0
     data_received..................: 90 MB   1.0 MB/s
     data_sent......................: 7.8 MB  86 kB/s
     group_duration.................: avg=2.21ms   min=1.22ms   med=2.04ms   max=496.91ms p(90)=2.88ms   p(95)=3.36ms
     http_req_blocked...............: avg=11.91µs  min=5.16µs   med=11.1µs   max=665.65µs p(90)=14.24µs  p(95)=16.62µs
     http_req_connecting............: avg=13ns     min=0s       med=0s       max=375.81µs p(90)=0s       p(95)=0s
     http_req_duration..............: avg=1.99ms   min=1.07ms   med=1.82ms   max=496.07ms p(90)=2.63ms   p(95)=3.09ms
       { expected_response:true }...: avg=1.99ms   min=1.07ms   med=1.82ms   max=496.07ms p(90)=2.63ms   p(95)=3.09ms
     http_req_failed................: 0.00%   ✓ 0           ✗ 91304
     http_req_receiving.............: avg=104.13µs min=34.85µs  med=101.68µs max=1.49ms   p(90)=132.69µs p(95)=148.69µs
     http_req_sending...............: avg=32.5µs   min=12.78µs  med=30.93µs  max=1.39ms   p(90)=38.62µs  p(95)=42.32µs
     http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s       p(90)=0s       p(95)=0s
     http_req_waiting...............: avg=1.86ms   min=971.14µs med=1.68ms   max=495.83ms p(90)=2.48ms   p(95)=2.94ms
     http_reqs......................: 91304   1014.479549/s
     iteration_duration.............: avg=2.25ms   min=1.25ms   med=2.07ms   max=497.11ms p(90)=2.93ms   p(95)=3.4ms
     iterations.....................: 91304   1014.479549/s
     vus............................: 1       min=1         max=4
     vus_max........................: 4       min=4         max=4


running (1m30.0s), 0/4 VUs, 91304 complete and 0 interrupted iterations
default ✓ [======================================] 0/4 VUs  1m30s
```

## wrk

`$ wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello`

(Looking at poor man profiler: System Monitor :P)
Max CPU, first run: 84,26%
Max CPU, second run: 70%
Max CPU, third run: 42,55%

After the second run, it's already hot. There's only a spike at
the beginning of ~40%, than it stays < 22%.

### Data after first run:

```txt
Running 20s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread calibration: mean lat.: 2337.622ms, rate sampling interval: 5558ms
  ...
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.23s   438.80ms   2.93s    64.55%
    Req/Sec   214.40      0.66   215.00    100.00%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    2.32s
 75.000%    2.60s
 90.000%    2.73s
 99.000%    2.86s
 99.900%    2.91s
 99.990%    2.93s
 99.999%    2.93s
100.000%    2.93s

#[Mean    =     2228.418, StdDeviation   =      438.798]
#[Max     =     2930.688, Total count    =        22630]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  37333 requests in 20.01s, 35.10MB read
Requests/sec:   1865.53
Transfer/sec:      1.75MB
```

### Data after third run:

```txt
Running 20s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread calibration: mean lat.: 1.884ms, rate sampling interval: 10ms
  ...
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.85ms  584.71us   8.34ms   72.55%
    Req/Sec   207.88    130.11     1.11k    90.37%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.79ms
 75.000%    2.15ms
 90.000%    2.56ms
 99.000%    3.70ms
 99.900%    5.52ms
 99.990%    7.64ms
 99.999%    8.35ms
100.000%    8.35ms

#[Mean    =        1.855, StdDeviation   =        0.585]
#[Max     =        8.344, Total count    =        19900]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  39810 requests in 20.00s, 37.43MB read
Requests/sec:   1990.22
Transfer/sec:      1.87MB
```

# Go

```txt
benchmark-microservice1/go1# perf stat -a bin/service
Server listening on port 8081
^Cbin/service: Interrupt

 Performance counter stats for 'system wide':

        922.559,49 msec cpu-clock                        #    8,000 CPUs utilized
        12.778.145      context-switches                 #   13,851 K/sec
         2.418.088      cpu-migrations                   #    2,621 K/sec
           371.431      page-faults                      #  402,609 /sec
 1.735.925.835.375      cycles                           #    1,882 GHz                         (83,33%)
   452.012.861.438      stalled-cycles-frontend          #   26,04% frontend cycles idle        (83,33%)
   120.632.126.301      stalled-cycles-backend           #    6,95% backend cycles idle         (83,33%)
   695.183.892.670      instructions                     #    0,40  insn per cycle
                                                  #    0,65  stalled cycles per insn     (83,34%)
   157.903.344.954      branches                         #  171,158 M/sec                       (83,33%)
    10.684.696.855      branch-misses                    #    6,77% of all branches             (83,34%)

     115,319221896 seconds time elapsed
```

## k6 benchmark result

```txt
$ k6 run k6_bench1.js
     scenarios: (100.00%) 1 scenario, 4 max VUs, 2m0s max duration (incl. graceful stop):
              * default: Up to 4 looping VUs for 1m30s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)

       ✓ status code should be 200

     checks.........................: 100.00% ✓ 201037      ✗ 0
     data_received..................: 201 MB  2.2 MB/s
     data_sent......................: 17 MB   190 kB/s
     group_duration.................: avg=991.66µs min=583.33µs med=932.33µs max=82.44ms p(90)=1.27ms  p(95)=1.42ms
     http_req_blocked...............: avg=8.73µs   min=3.91µs   med=8.1µs    max=1.3ms   p(90)=10.54µs p(95)=12.78µs
     http_req_connecting............: avg=4ns      min=0s       med=0s       max=270.5µs p(90)=0s      p(95)=0s
     http_req_duration..............: avg=866.2µs  min=509.08µs med=805.5µs  max=18.33ms p(90)=1.13ms  p(95)=1.28ms
       { expected_response:true }...: avg=866.2µs  min=509.08µs med=805.5µs  max=18.33ms p(90)=1.13ms  p(95)=1.28ms
     http_req_failed................: 0.00%   ✓ 0           ✗ 201037
     http_req_receiving.............: avg=61.43µs  min=21.09µs  med=55.73µs  max=17.57ms p(90)=83.95µs p(95)=96.31µs
     http_req_sending...............: avg=21.37µs  min=7.26µs   med=19.97µs  max=2.11ms  p(90)=27.65µs p(95)=31.01µs
     http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s      p(90)=0s      p(95)=0s
     http_req_waiting...............: avg=783.39µs min=446.15µs med=720.64µs max=9.33ms  p(90)=1.04ms  p(95)=1.18ms
     http_reqs......................: 201037  2233.466824/s
     iteration_duration.............: avg=1.01ms   min=606.24µs med=960.13µs max=82.61ms p(90)=1.3ms   p(95)=1.46ms
     iterations.....................: 201037  2233.466824/s
     vus............................: 1       min=1         max=4
     vus_max........................: 4       min=4         max=4


running (1m30.0s), 0/4 VUs, 201037 complete and 0 interrupted iterations
default ✓ [======================================] 0/4 VUs  1m30s
```

## wrk

```txt
$ wrk -t 10 -c 100 -R 1000 http://localhost:8081/hello
Running 10s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.71ms  523.88us   4.74ms   69.99%
    Req/Sec       -nan      -nan   0.00      0.00%
  10010 requests in 10.00s, 9.54MB read
Requests/sec:   1000.73
Transfer/sec:      0.95MB
```


# benchmarksgame Go x Java

https://benchmarksgame-team.pages.debian.net/benchmarksgame/fastest/go.html

Go 6 x Java 4 (2024-05-30)

```txt
Go

go version go1.22.0 linux/amd64
GOAMD64=v2
Java

java 22 2024-03-19
Java HotSpot(TM) 64-Bit Server VM
(build 22+36-2370,
mixed mode, sharing)
```

## Go wins

fannkuch-redux
n-body
spectral-norm
mandelbrot
regex-redux
reverse-complement

## Java wins

pidigits
fasta
k-nucleotide
binary-trees

