The goal here is to compare Java vs Go: cpu, rss and performance.

  - [Why do that?](#why-do-that)
  - [Why compare against Go?](#why-compare-against-go)
  - [Running the Benchmark](#running-the-benchmark)

## Why do that?

I'm mostly a Java developer, but Java uses too much memory compared to
some other languages.<br>
For many kinds of applications this is not a problem, and memory is
cheap nowadays (???), but if you have thousands of microservices,
the cost can become significant.

Of course, that is not the only cost you should consider. Probably
Go and Rust developers are more expensive that Java developers,
which could make the memory cost irrelevant.

They say every benchmark is flawed =), so please open an issue or PR
if you find something wrong.

I'm not a Go developer, so most of the Go code was generated by Gemini.<br>
Gemini did a great job, I guess. =)<br>

## Why compare against Go?

Two things that I'm really impressed about Go:
  - Fast compilation time;
  - Low memory usage (even with a GC).

## What does this application do?

The app consists of only two endpoints:
  - /hello
  - /db

### /hello

It returns a JSON with client's phones, address and loan options.<br>
It contains an unreachable branch: all clients are at least 18 years old.
The goal here is to check if Java JIT can take advantage of that.

  - getClient: returns one client from 10 clients;
  - getPhones: calls a REST API to get client's phones;
  - getAddress: calls a REST API to get client's address;
  - calculateLoanOptions: returns loan options based on customer's salary.

### /db



## Running the Benchmark

