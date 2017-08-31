# Chapter 3: Akka Basic Toolkit - Elasticity

In this source code example we extend our analogy by introducing elacticity in the form of `Parallelization`. Our basic actor layout will stay the same with a small change to how we implement our `Librarian`:

## `RareBooks` Actor

In our `Messaging` example, we implemented `Librarian` as a single child actor. In this example we will change this by implementing our `Librarian` as a router that supports a configurable number of `Librarian` routees.

## Summary

By increasing the number of `Librarian` actors and `parallelizing` our throughput, we significantly increase our processing speed in the face of increasing load.

## Running the Application

1. Run a terminal session and navigate to the root directory, `reactive-application-development-scala`.
2. Run `sbt`.
3. Inside the `sbt` session, enter `project chapter3_002_elasticty`.
4. Inside the `sbt` session, enter `run` to bootstrap the application.
5. Under `Enter commands [q = quit, 2c = 2 customers, etc.]:` enter `5c` for 5 customers.
6. The `rarebook.log` file will be created in the root directory containing the application output.
7. You should notice a **significant increase in throughput** for requests being processed!
