package com.rarebooks.library

class CatalogSpec extends BaseSpec {

  import Catalog._
  import LibraryProtocol._

  "books" should {
    "contain theEpicOfGilgamesh, phaedrus and theHistories" in {
      books.values.toSet should === (Set[BookCard](theEpicOfGilgamesh, phaedrus, theHistories))
    }
  }

  "findBookByIsbn" should {
    "return theEpicOfGilgamesh" in {
      findBookByIsbn(theEpicOfGilgamesh.isbn) should === (Some[List[BookCard]](List(theEpicOfGilgamesh)))
    }
    "return phaedrus" in {
      findBookByIsbn(phaedrus.isbn) should === (Some[List[BookCard]](List(phaedrus)))
    }
    "return theHistories" in {
      findBookByIsbn(theHistories.isbn) should === (Some[List[BookCard]](List(theHistories)))
    }
    "return None when isbn not found" in {
      findBookByIsbn("1234567890") should === (None)
    }
  }

  "findBookByAuthor" should {
    "return theEpicOfGilgamesh" in {
      findBookByAuthor(theEpicOfGilgamesh.author) should === (Some[List[BookCard]](List(theEpicOfGilgamesh)))
    }
    "return phaedrus" in {
      findBookByAuthor(phaedrus.author) should === (Some[List[BookCard]](List(phaedrus)))
    }
    "return theHistories" in {
      findBookByAuthor(theHistories.author) should === (Some[List[BookCard]](List(theHistories)))
    }
    "return None when author not found" in {
      findBookByAuthor("Michael Crichton") should === (None)
    }
  }

  "findBookByTitle" should {
    "return theEpicOfGilgamesh" in {
      findBookByTitle(theEpicOfGilgamesh.title) should === (Some[List[BookCard]](List(theEpicOfGilgamesh)))
    }
    "return phaedrus" in {
      findBookByTitle(phaedrus.title) should === (Some[List[BookCard]](List(phaedrus)))
    }
    "return theHistories" in {
      findBookByTitle(theHistories.title) should === (Some[List[BookCard]](List(theHistories)))
    }
    "return None when title not found" in {
      findBookByTitle("Swiss Family Robinson") should === (None)
    }
  }

  "findBookByTopic" should {
    "for Gilgamesh, Persia and Royalty, return theEpicOfGilgamesh" in {
      findBookByTopic(Set(Gilgamesh, Persia, Royalty)) should === (Some[List[BookCard]](List(theEpicOfGilgamesh)))
    }
    "for Philosophy, return phaedrus" in {
      findBookByTopic(Set(Philosophy)) should === (Some[List[BookCard]](List(phaedrus)))
    }
    "for Greece and Philosophy, return phaedrus and theHistories" in {
      findBookByTopic(Set(Greece, Philosophy)) should === (Some[List[BookCard]](List(phaedrus, theHistories)))
    }
    "for Africa, Asia, Greece and Tradition, return phaedrus and theHistories" in {
      findBookByTopic(Set(Africa, Asia, Greece, Tradition)) should === (Some[List[BookCard]](List(phaedrus, theHistories)))
    }
    "for Tradition, return theHistories" in {
      findBookByTopic(Set(Tradition)) should === (Some[List[BookCard]](List(theHistories)))
    }
    "return None when tag not found" in {
      findBookByTopic(Set(Unknown)) should === (None)
    }
  }
}
