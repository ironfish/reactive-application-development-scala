# Chapter 3: Akka Basic Toolkit - Resilience

In this source code example we fix the failure introduced in the previous example that simulated when a librarian receives too many complaints. Our basic actor layout will stay the some changes to our `RareBooks`:

## `RareBooks` Actor

1. Implement a `SupervisorStrategy` for `RareBooks`.
2. When `Librarian.ComplainException(complain, customer)` is matched, send `Customer` a `Credit`.
3. Log at info `RareBooks sent customer … a credit`.
4. Restart the faulty `Librarian`.

## Summary

We have solved our failure scenario with `self-healing` resilience through implementing a `SupervisorStrategy` in `RareBooks`. Our application will now behave as expected with increased throughput and failure management.

## Running the Application

1. Run a terminal session and navigate to the root directory, `reactive-application-development-scala`.
2. Run `sbt`.
3. Inside the `sbt` session, enter `project chapter3_004_resilience`.
4. Inside the `sbt` session, enter `run` to bootstrap the application.
5. Under `Enter commands [q = quit, 2c = 2 customers, etc.]:` enter `5c` for 5 customers.
6. The `rarebook.log` file will be created in the root directory containing the application output.
7. We continue to have a **significant increase in throughput**, and resolved our faulty `Librarian` issue.
