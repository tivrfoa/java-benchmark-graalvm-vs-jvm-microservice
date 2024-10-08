

# Native

## TODO: add perf stat

```txt
```

## k6 benchmark

```txt
$ k6 run k6_bench1.js
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

### Getting maximum RSS size with /usr/bin/time

```txt
$ /usr/bin/time --verbose ./target/code-with-quarkus-1.0.0-SNAPSHOT-runner
	Percent of CPU this job got: 134%
	Maximum resident set size (kbytes): 112128
```


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
$ /usr/bin/time --verbose ./bin/service
Server listening on port 8081
...
	Percent of CPU this job got: 71%
	Maximum resident set size (kbytes): 16128
```

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

## TODO: add perf stat

```txt
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

### k6 - Second run

```
$ k6 run k6_bench1.js
     scenarios: (100.00%) 1 scenario, 4 max VUs, 2m0s max duration (incl. graceful stop):
              * default: Up to 4 looping VUs for 1m30s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)
       ✓ status code should be 200

     checks.........................: 100.00% ✓ 143465     ✗ 0
     data_received..................: 141 MB  1.6 MB/s
     data_sent......................: 12 MB   136 kB/s
     group_duration.................: avg=1.4ms   min=845.71µs med=1.32ms  max=11.69ms  p(90)=1.7ms   p(95)=1.91ms
     http_req_blocked...............: avg=9.32µs  min=4.4µs    med=8.24µs  max=1.34ms   p(90)=11.59µs p(95)=14.17µs
     http_req_connecting............: avg=7ns     min=0s       med=0s      max=305.27µs p(90)=0s      p(95)=0s
     http_req_duration..............: avg=1.26ms  min=735.36µs med=1.18ms  max=11.55ms  p(90)=1.54ms  p(95)=1.74ms
       { expected_response:true }...: avg=1.26ms  min=735.36µs med=1.18ms  max=11.55ms  p(90)=1.54ms  p(95)=1.74ms
     http_req_failed................: 0.00%   ✓ 0          ✗ 143465
     http_req_receiving.............: avg=67.8µs  min=24.23µs  med=63.9µs  max=1.49ms   p(90)=89.53µs p(95)=102.37µs
     http_req_sending...............: avg=23.56µs min=8.94µs   med=21.44µs max=1.67ms   p(90)=31.98µs p(95)=36.93µs
     http_req_tls_handshaking.......: avg=0s      min=0s       med=0s      max=0s       p(90)=0s      p(95)=0s
     http_req_waiting...............: avg=1.16ms  min=660.77µs med=1.09ms  max=11.47ms  p(90)=1.43ms  p(95)=1.63ms
     http_reqs......................: 143465  1594.04206/s
     iteration_duration.............: avg=1.43ms  min=868.97µs med=1.35ms  max=11.71ms  p(90)=1.74ms  p(95)=1.95ms
     iterations.....................: 143465  1594.04206/s
     vus............................: 1       min=1        max=4
     vus_max........................: 4       min=4        max=4


running (1m30.0s), 0/4 VUs, 143465 complete and 0 interrupted iterations
default ✓ [======================================] 0/4 VUs  1m30s
```

## wrk

`$ wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello`

### Getting maximum RSS size with /usr/bin/time

```txt
$ /usr/bin/time --verbose java -jar target/quarkus-app/quarkus-run.jar
	Percent of CPU this job got: 424%
	Maximum resident set size (kbytes): 289300
```


Looking at poor man profiler: System Monitor :P
It can't be trusted, because it doesn't show the usage across all CPUs
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

# Vertx

## k6 benchmark

```txt
$ k6 run k6_bench1.js
     scenarios: (100.00%) 1 scenario, 4 max VUs, 2m0s max duration (incl. graceful stop):
              * default: Up to 4 looping VUs for 1m30s over 3 stages (gracefulRampDown: 30s, gracefulStop: 30s)
       ✓ status code should be 200

     checks.........................: 100.00% ✓ 237722      ✗ 0
     data_received..................: 233 MB  2.6 MB/s
     data_sent......................: 20 MB   225 kB/s
     group_duration.................: avg=829.47µs min=419.19µs med=757.92µs max=12.35ms p(90)=1.04ms   p(95)=1.24ms
     http_req_blocked...............: avg=9.03µs   min=4.19µs   med=8.1µs    max=1.62ms  p(90)=11.31µs  p(95)=13.68µs
     http_req_connecting............: avg=4ns      min=0s       med=0s       max=324.9µs p(90)=0s       p(95)=0s
     http_req_duration..............: avg=705.88µs min=330.49µs med=641.57µs max=12.16ms p(90)=903.96µs p(95)=1.07ms
       { expected_response:true }...: avg=705.88µs min=330.49µs med=641.57µs max=12.16ms p(90)=903.96µs p(95)=1.07ms
     http_req_failed................: 0.00%   ✓ 0           ✗ 237722
     http_req_receiving.............: avg=58.23µs  min=19.83µs  med=52.38µs  max=4.41ms  p(90)=81.57µs  p(95)=93.03µs
     http_req_sending...............: avg=21.55µs  min=7.19µs   med=19.48µs  max=6.38ms  p(90)=30.59µs  p(95)=35.54µs
     http_req_tls_handshaking.......: avg=0s       min=0s       med=0s       max=0s      p(90)=0s       p(95)=0s
     http_req_waiting...............: avg=626.1µs  min=285.02µs med=566.97µs max=10.29ms p(90)=812.33µs p(95)=973.72µs
     http_reqs......................: 237722  2641.340906/s
     iteration_duration.............: avg=857.92µs min=440.56µs med=785.09µs max=12.39ms p(90)=1.08ms   p(95)=1.28ms
     iterations.....................: 237722  2641.340906/s
     vus............................: 1       min=1         max=4
     vus_max........................: 4       min=4         max=4


running (1m30.0s), 0/4 VUs, 237722 complete and 0 interrupted iterations
default ✓ [======================================] 0/4 VUs  1m30s
```

## wrk

### First run

```
Running 20s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread calibration: mean lat.: 2453.853ms, rate sampling interval: 5578ms
  ...
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   430.78ms  675.29ms   2.17s    80.22%
    Req/Sec   277.00      0.00   277.00    100.00%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.61ms
 75.000%  791.55ms
 90.000%    1.67s
 99.000%    2.13s
 99.900%    2.17s
 99.990%    2.17s
 99.999%    2.17s
100.000%    2.17s

#[Mean    =      430.775, StdDeviation   =      675.294]
#[Max     =     2170.880, Total count    =        24237]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  40008 requests in 20.00s, 37.41MB read
Requests/sec:   2000.23
Transfer/sec:      1.87MB
```

### Getting maximum RSS size with /usr/bin/time

```txt
$ /usr/bin/time --verbose java -jar target/starter-1.0.0-SNAPSHOT-fat.jar
	Percent of CPU this job got: 170%
	Maximum resident set size (kbytes): 211056
```

### Second run

```txt
Running 20s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread calibration: mean lat.: 1.649ms, rate sampling interval: 10ms
  ...
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.20ms  446.54us   5.26ms   68.21%
    Req/Sec   206.94     36.36   333.00     88.25%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.18ms
 75.000%    1.47ms
 90.000%    1.76ms
 99.000%    2.33ms
 99.900%    3.65ms
 99.990%    4.74ms
 99.999%    5.26ms
100.000%    5.26ms

#[Mean    =        1.196, StdDeviation   =        0.447]
#[Max     =        5.256, Total count    =        19897]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  40007 requests in 20.00s, 37.41MB read
Requests/sec:   2000.34
Transfer/sec:      1.87MB
```

# Go

## TODO: add perf stat

```txt
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

`$ wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello`

### Getting maximum RSS size with /usr/bin/time

```txt
$ /usr/bin/time --verbose ./bin/service
	Percent of CPU this job got: 71%
	Maximum resident set size (kbytes): 16128
```

```txt
Running 20s test @ http://localhost:8081/hello
  10 threads and 100 connections
  Thread calibration: mean lat.: 1.565ms, rate sampling interval: 10ms
  ...
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.43ms  457.59us   4.53ms   67.74%
    Req/Sec   212.41     40.44   333.00     85.20%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.40ms
 75.000%    1.71ms
 90.000%    2.03ms
 99.000%    2.62ms
 99.900%    3.24ms
 99.990%    3.59ms
 99.999%    4.53ms
100.000%    4.53ms

#[Mean    =        1.425, StdDeviation   =        0.458]
#[Max     =        4.528, Total count    =        19896]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  40006 requests in 20.00s, 38.14MB read
Requests/sec:   2000.44
Transfer/sec:      1.91MB
```

# DB

## Quarkus

max 30 db connections.

After first run:

CPU usage < 13,1%

Max RAM: 276 MB

```txt
$ make wrkDB 
wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/db
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.55ms  721.84us   5.76ms   70.92%
    Req/Sec   209.41    165.92   666.00     60.30%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.41ms
 75.000%    1.91ms
 90.000%    2.57ms
 99.000%    3.72ms
 99.900%    4.78ms
 99.990%    5.63ms
 99.999%    5.77ms
100.000%    5.77ms

#[Mean    =        1.545, StdDeviation   =        0.722]
#[Max     =        5.764, Total count    =        19900]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  39971 requests in 20.00s, 13.38MB read
Requests/sec:   1998.41
Transfer/sec:    685.20KB
```

## Quarkus Hibernate Reactive Panache

After first run:

CPU usage after it became hot: < 13%

Max RAM: 380 MB

```txt
$ make wrkDB 
wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/db
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.65ms    1.62ms  17.81ms   76.14%
    Req/Sec   209.92    197.91   800.00     53.03%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    2.06ms
 75.000%    3.54ms
 90.000%    4.94ms
 99.000%    7.71ms
 99.900%   10.72ms
 99.990%   14.75ms
 99.999%   17.82ms
100.000%   17.82ms

#[Mean    =        2.647, StdDeviation   =        1.622]
#[Max     =       17.808, Total count    =        19900]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  39972 requests in 20.00s, 13.38MB read
Requests/sec:   1998.35
Transfer/sec:    685.18KB
```


## Vertx

It uses just 1 db connection.
I guess this is the main reason for the lower memory usage.

After first run:

Max RAM: 231 MB

Very low (< 8%) CPU usage.

```txt
$ make wrkDB 
wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/db
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.74ms    1.02ms   8.10ms   73.31%
    Req/Sec   210.36    213.77     1.11k    81.48%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.44ms
 75.000%    2.20ms
 90.000%    3.31ms
 99.000%    4.76ms
 99.900%    5.48ms
 99.990%    6.54ms
 99.999%    8.10ms
100.000%    8.10ms

#[Mean    =        1.738, StdDeviation   =        1.016]
#[Max     =        8.096, Total count    =        19900]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  39780 requests in 20.00s, 12.79MB read
Requests/sec:   1988.79
Transfer/sec:    654.70KB
```

## Go

It's using only 6 db connections.

CPU usage < 13%

Max RAM: 22 MB

```
$ make wrkDB 
wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/db
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.53ms  703.65us   6.56ms   73.04%
    Req/Sec   210.88    227.09     1.11k    80.27%
  Latency Distribution (HdrHistogram - Recorded Latency)
 50.000%    1.41ms
 75.000%    1.86ms
 90.000%    2.43ms
 99.000%    3.90ms
 99.900%    5.30ms
 99.990%    6.37ms
 99.999%    6.56ms
100.000%    6.56ms

#[Mean    =        1.526, StdDeviation   =        0.704]
#[Max     =        6.560, Total count    =        19900]
#[Buckets =           27, SubBuckets     =         2048]
----------------------------------------------------------
  39780 requests in 20.00s, 14.06MB read
Requests/sec:   1988.75
Transfer/sec:    719.95KB
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

