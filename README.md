# What's this repository for?

I am trying to figure out how to create my own annotation processors,
and how to debug it, test it and so on.

This is a partial result of what I've realised that I can reference later.

## Structure

* module `processor` contains actual annotation processor
* moduile `processor-user` uses this annotation processor

## What does this annotation processor do?

It does a very simple job: when it sees an interface annotated
with `@TestAnnotation`, it generates implementation with `main`-method
printing `Hello, world!` on screen.

## Debugging

Tests inside `processor` module can be easily debugged by means
of and IDE of your choice as they use Java Compiler API to run `javac`.

## Building the project

It is as simple as 
```bash
./gradlew clean build
```

I have tested it only with Java 11.