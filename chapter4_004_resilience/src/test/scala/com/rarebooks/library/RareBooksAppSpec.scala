package com.rarebooks.library

import akka.testkit.TestProbe

class RareBooksAppSpec extends BaseAkkaSpec("as-3004-rare-books-app-spec") {

  "Creating RareBooksApp" should {
    new RareBooksApp(system) {
      createCustomer(3, 100, 5)
    }
    "create a top-level actor named 'rare-books'" in {
      TestProbe().expectActor("/user/rare-books")
    }
    "create n customers as top-level actors when calling createCustomer" in {
      TestProbe().expectActor("/user/$a")
      TestProbe().expectActor("/user/$b")
      TestProbe().expectActor("/user/$c")
    }
  }
}
