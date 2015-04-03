package com.rarebooks.library

class CatalogSpec extends BaseSpec {

  import Catalog._

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

  "findBookByTags" should {
    "for 'gilgamesh', 'royalty' and 'persia', return theEpicOfGilgamesh" in {
      findBookByTag(Set("gilgamesh", "royalty", "persia")) should === (Some[List[BookCard]](List(theEpicOfGilgamesh)))
    }
    "for 'philosophy', return phaedrus" in {
      findBookByTag(Set("philosophy")) should === (Some[List[BookCard]](List(phaedrus)))
    }
    "for 'philosophy' and 'greece', return phaedrus and theHistories" in {
      findBookByTag(Set("philosophy", "greece")) should === (Some[List[BookCard]](List(phaedrus, theHistories)))
    }
    "for 'tradition', 'asia', 'greece' and 'africa', return phaedrus and theHistories" in {
      findBookByTag(Set("tradition", "asia", "greece", "africa")) should === (Some[List[BookCard]](List(phaedrus, theHistories)))
    }
    "for 'tradition', return theHistories" in {
      findBookByTag(Set("tradition")) should === (Some[List[BookCard]](List(theHistories)))
    }
    "return None when tag not found" in {
      findBookByTag(Set("dogs")) should === (None)
    }
  }
}
