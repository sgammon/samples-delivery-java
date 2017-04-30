# Task Dispatch: Java Code Sample
[![Build Status](https://travis-ci.org/sgammon/samples-delivery-java.svg?branch=master)](https://travis-ci.org/sgammon/samples-delivery-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ccfa5c3038464d708051888e1cf45311)](https://www.codacy.com/app/samuel-gammon/samples-delivery-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sgammon/samples-delivery-java&amp;utm_campaign=Badge_Grade)
[![Code Climate](https://codeclimate.com/github/sgammon/samples-delivery-java.png)](https://codeclimate.com/github/sgammon/samples-delivery-java)

∞ // @sgammon

## Overview
This codebase is written for a take-home problem assigned during an interview with ∞. 

### How to Build
Just run `make`.

### Tour
Here's how it works:
- _Step 1_: [Autogenerate data](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/util/ObjectGenerator.java#L24) *or* [load a dataset](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/util/FileUtil.java#L24) from [JSON embedded in the JAR](https://github.com/sgammon/samples-delivery-java/blob/master/datafiles/testsuite.json)
  - A set of [common US first and last names is downloaded](https://github.com/sgammon/samples-delivery-java/blob/master/Makefile#L97) upon first `make` and used to generate [`Driver`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Driver.java)s
  - A [rough geopoint generation algorithm](https://github.com/sgammon/samples-delivery-java/pull/2) generates [`Location`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Location.java)s for [`Task`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Task.java)s
- _Step 2_: Assign [`Task`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Task.java) records from the dataset to [`Driver`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Driver.java)s
  - [`Tasklist`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/collection/Tasklist.java) is initially used, in concert with [`TaskManager`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/manager/TaskManager.java), which is [optimized for reading](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/manager/TaskManager.java#L104) by [controlling the write path](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/collection/Tasklist.java#L183)
- _Step 3_: With an ordered list of [`Task`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Task.java)s for each [`Driver`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct/Driver.java), experiment with algorithms to intelligently assign more tasks
  - [`BlindTaskManager`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/manager/BlindTaskManager.java) is used next to demonstrate lowest-cost-assignment resolution algorithms with no write path control
- _Step 4_: The output from [`TaskManager`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/manager/TaskManager.java) and [`BlindTaskManager`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/manager/BlindTaskManager.java) is interrogated and verified by the builtin JUnit-based testsuite, which specifies tests that enforce the expected output

The codebase is split into some notable Java packages (click for individual README):
- [`com.onfleet.demo.homework.cli`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/cli): powers the `deliverytool` CLI
- [`com.onfleet.demo.homework.collection`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/collection): contains `Tasklist`, a read-optimized task assignment structure
- [`com.onfleet.demo.homework.manager`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/manager): implementation classes for task assignment managers
- [`com.onfleet.demo.homework.struct`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/struct): data models/structures for `Driver`, `Location`, `Geopoint`, and `Task`
- [`com.onfleet.demo.homework.util`](https://github.com/sgammon/samples-delivery-java/blob/master/src/main/java/com/onfleet/demo/homework/util): static utilities, mostly for generating mock objects

#### How things evolved/comments from the author
Initially, I understood the solution to be optimizable/approachable from the write-side of the problem. To that end, once I had
testing and devops tools in place (including some to generate data), I extended it to operate with no knowledge of the precomputed
stats from a write-optimized run of the tool.

This way, it shows how the problem may be approached both on-write and on-read, and how one might mitigate issues that arise
from choosing one over the other.

I'm not afraid of misunderstanding things. That's part of engineering. What you have to do is move on. As such, I provide this
GitHub to show my work - the algorithms within changed as I better understood the problem. They are revised in the open, in PR's,
with a friend simulating code review, and 4 robots scanning it for missteps.

### How it's structured
Code is written in *[Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)*,
which ships with recent copies of Mac OS X. For build tools, *[Maven](https://maven.apache.org/)* and
*[GNU Make](https://www.gnu.org/software/make/)* are used. If you have *[Homebrew](https://brew.sh/)* installed, you can simply run
`make` and the `Makefile` will auto-install *Maven* for you.

### Further help
Just run `make help` for a list of available codebase commands, or run the standard gamut of Maven commands, like `mvn clean install`.

#### Usage as a Maven dependency
```xml
  <dependency>
    <groupId>com.onfleet.demo</groupId>
    <artifactId>homework</artifactId>
    <version>1.0</version>
  </dependency>
```
 
### Devops and Tooling
- Support is included for Maven and IntelliJ IDEA
- Support for static analysis via PMD and Findbugs
- Support for CI via Jenkins (internal) and TravisCI (public, in both Java 7 and 8)
- Support for auto-generated documentation via Javadoc and Dokka
- Available both as a CLI tool (`deliverytool`) or library via Maven (`com.onfleet.demo:homework`)
- Self-documenting `make` should be bootstrap-able from basically nothing, assuming Xcode Tools are installed

## Problem Statement

(Via Mikel Cármenes Cavia, @euskode)

```text
Inserting tasks to minimize total route length increase:

Problem statement: Given a set of drivers D, each d in D with an ordered list of
assigned tasks T(d), each t in T(d) with a location. The goal is to insert a new
task into one of the assigned task lists (at the start, between any pair of tasks
or at the end) so as to minimize the total increase in route length (where the
distance for a route is the sum of distances between adjacent tasks). Distance
here may be Euclidean.
```

#### Part 1
Build a naive solution that produces the optimal solution and benchmark the performance for a known set of random data. You may generate semi-realistic data any way you like. It doesn't need to be distributed like actual deliveries but should at least have all unique points. Benchmark the runtime for 1k, 10k, and 100k drivers each with 25 tasks.

#### Part 2
Improve the approach to scale sub-linearly with the number of total possible assignments, (25 + 1) * driversCount here. Since it may be hard to produce the exact solution, you may use any reasonable heuristics etc to achieve this goal. You should briefly justify your solution and provide benchmark results with the same dataset from the previous part.

### Credit and licensing
- Name data from [enorvelle/NameDatabases](https://github.com/enorvelle/NameDatabases)
- GeoJSON bounds from [johan/world.geo.json](https://github.com/johan/world.geo.json)
