package com.rarebooks.library

import akka.testkit.TestProbe

class RareBooksAppSpec extends BaseAkkaSpec("as-5001-rare-books-app-spec") {

  "Creating RareBooksApp" should {
    new RareBooksApp(system)
    "create a top-level actor named 'rare-books'" in {
      TestProbe().expectActor("/user/rare-books")
    }
  }
}
