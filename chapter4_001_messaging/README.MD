# Chapter 3: Akka Basic Toolkit - Messaging

In this source code example we focus on creating the basic structure of our analogy in the form of a local `ActorSystem`. The actor system contains the following actors:

## `RareBooks` Actor

This actor is a top-level actor that represents the business, Rare Books as follows:

1. Uses a single `Librarian` actor who is responsible for customer requests.
2. Uses `become` with `stash` to simulate hours of operation.
3. The initial behavior is `open` which simulates `RareBooks` is open for business.
4. When `open`, `RareBooks` will:
    - Process `Msg`:
        - Forward requests to the `Librarian`.
        - Increase `requestsToday` by 1.
    - Process `Close`:
        - Schedule the next `Open` time.
        - Log `"Closing down for the day"`.
        - Change behavior to `close`.
        - Send `Report` message to `self`.
5. When `closed`, `RareBooks` will:
    - Process `Open`:
        - Schedule he next `Close` time.
        - `unstashAll` messages received while closed.
        - Log `"Time to open up!"`.
        - Change behavior to `open`.
    - Process `Report`:
        - Add `requestsToday` to `totalRequests`.
        - Log `"requestsToday requests processed today. Total requests processed = totalRequests"`.
        - Reset `requestsToday` to zero.
    - `stash` all other incoming requests.

## `Librarian` Actor

This actor is a child of `RareBooks` that processes all customer requests as follows:

1. Uses `become` with `stash` to simulate processing customer requests.
2. The initial behavior is `ready` to simulate the `Librarian` can process customer requests
3. When `ready`, `Librarian` will:
    - Process `Complain`:
        - Send `Credit`.
    - Process `FindBookBy..`:
        - Schedule research `Done` to simulate researching find book info request.
        - Change behavior to `busy`.
4. When `busy`, `Librarian` will:
    - Process `Done`:
        - Return `BookFound` when book info found.
        - Return and log `BookNotFound` when book info not found.
        - `unstashAll` `stashed` messages while busy.
        - Change behavior to `ready`.
    - `stash` all other incoming customer requests

## `Customer` Actor

This actor represents the `Customer` and is a top-level actor.

1. Has a single behavior of receiving messages.
2. Process `BookFound`:
    - Update internal state.
    - Log `"f.books.size Book(s) found!"`
    - Request more book information.
3. Process `BookNotFound` if `state.model.notFound` less than `state.model.tolerance`:
    - Update internal state.
    - log `" # Books(s) not found! My tolerance is #."`.
    - Request more book information.
4. Process `BookNotFound`:
    - Update internal state.
    - log `" # Book(s) ont found! Reached my tolerance of #. Sent complaint!"`
    - NOTE: Will stop requesting book information.
5. Process `Credit`:
    - Update internal state, resetting `state.model.notFound` to 0.
    - log `"Credit received, will start requesting again!"`
    - Request more book information.
6. Process `GetCustomer`:
    - Send `state.model`.

## Summary

This is the first step in being `Reactive` by providing the underlying messaging infrastructure we have established the base for being `elastic` and `resilient`. However, we have introduced a bottleneck. By having only one actor, the `Librarian` that can handles messages, we restrict ourselves to the processing speed of this actor.

## Running the Application

1. Run a terminal session and navigate to the root directory, `reactive-application-development-scala`.
2. Run `sbt`.
3. Inside the `sbt` session, enter `project chapter3_001_messaging`.
4. Inside the `sbt` session, enter `run` to bootstrap the application.
5. Under `Enter commands [q = quit, 2c = 2 customers, etc.]:` enter `5c` for 5 customers.
6. The `rarebook.log` file will be created in the root directory containing the application output.
