# Task Dispatch: Java Code Sample
[![Build Status](https://buildbot.hq.mm-corp.systems/jenkins/buildStatus/icon?job=Sam/samples-delivery-java)](https://buildbot.hq.mm-corp.systems/jenkins/job/Sam/samples-delivery-java)
[![Build Status](https://travis-ci.org/sgammon/samples-delivery-java.svg?branch=master)](https://travis-ci.org/sgammon/samples-delivery-java)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ccfa5c3038464d708051888e1cf45311)](https://www.codacy.com/app/samuel-gammon/samples-delivery-java?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sgammon/samples-delivery-java&amp;utm_campaign=Badge_Grade)
[![Code Climate](https://codeclimate.com/github/sgammon/samples-delivery-java.png)](https://codeclimate.com/github/sgammon/samples-delivery-java)

∞ // @sgammon

## Overview
This codebase is written for a take-home problem assigned during an interview with ∞. 

### How to Build
Code is written in *[Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)*,
which ships with recent copies of Mac OS X. For build tools, *[Maven](https://maven.apache.org/)* and
*[GNU Make](https://www.gnu.org/software/make/)* are used. If you have *[Homebrew]()* installed, you can simply run
`make` and the `Makefile` will auto-install *Maven* for you. `make` is optional but *Maven* and *Java* are not ;).

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

### Followup Questions
```text
1) You specified sub-linear complexity. I assume you mean time complexity - is there any restriction on space?

2) Forgive me, but I don't have a math background and I'm not sure what you mean by "euclidean" in this context
   (I'm familiar with the general concept, just not its use here). Is it alright if I use geopoints with standard
   algorithms for distance, etc, geo-bounded to the SF bay area? (Follow up, would that be what you mean?)

3) When you specify an ordered list of tasks, you do mean the order in which the drivers should execute that task?
   Or is that left up to the implementor? Is the implied need to output the tasks in an ordered way for drivers to
   execute (for instance, in a driver-facing app) an explicit need or is there no such need?

4) I like this problem, and given the choice of any language, I would implement it in Java (because of the strong
   data-structure selection, algorithmic control w/typing and ability to optimize this kind of problem via JIT on
   a hot JVM), specifically SE 8. Is it cool if I write it in that?

5) I'd like to write tests and show my devops workflow skills by keeping this on Github. Would that work in place
   of a Codepen, or should I paste the solution there afterwards?
```

#### Response
*pending*

### Credit and licensing
- Name data from [enorvelle/NameDatabases](https://github.com/enorvelle/NameDatabases)
- GeoJSON bounds from [johan/world.geo.json](https://github.com/johan/world.geo.json)

![∞](https://d1er272rpp2pqg.cloudfront.net/d54c2859/app/images/home/section-hero-logo.svg "∞")
