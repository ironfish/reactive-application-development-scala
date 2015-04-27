# Chapter 3: Akka Basic Toolkit - Faulty Router

In this source code example we will introduce failure to simulate when a librarian receives too many complaints. Our basic actor layout will stay the same with a small change to our `Librarian`:

## `Librarian` Actor

1. Implement a `max-complain-count` for our `Librarian`.
2. Create `ComplainException(c: Complain, customer: ActorRef)`.
3. Keep local state, `complainCount`, of the number of `Complain` messages received.
4. When `complainCount` equals `max-complain-count`, the `Librarian` will throw a `ComplainException`.

## Summary

We have introduced failure to our scenario. When a `Customer` complains, our `Librarian` will eventually become frustrated and throw an exception. In turn, this means our `Customer` will not receive a `Credit` and will stop requesting information. Given enough time, all customer's will eventually stop requesting information essentially grinding our application to a stand still. We will address this problem in the next section with `self-healing` resilience.

## Running the Application

1. Run a terminal session and navigate to the root directory, `reactive-application-development-scala`.
2. Run `sbt`.
3. Inside the `sbt` session, enter `project chapter3_003_faulty`.
4. Inside the `sbt` session, enter `run` to bootstrap the application.
5. Under `Enter commands [q = quit, 2c = 2 customers, etc.]:` enter `5c` for 5 customers.
6. The `rarebook.log` file will be created in the root directory containing the application output.
7. You will notice that while we still have a **significant increase in throughput**, the introduced failure will eventually cause all customers to stop requesting book information.
