package com.rarebooks.library

object Catalog {

  import LibraryProtocol._

  val phaedrus = BookCard(
    "0872202208",
    "Plato",
    "Phaedrus",
    "Plato's enigmatic text that treats a range of important philosophical issues.",
    "370 BC",
    Set(Greece, Philosophy),
    "Hackett Publishing Company, Inc.",
    "English",
    144)

  val theEpicOfGilgamesh = BookCard(
    "0141026286",
    "unknown",
    "The Epic of Gilgamesh",
    "A hero is created by the gods to challenge the arrogant King Gilgamesh.",
    "2700 BC",
    Set(Gilgamesh, Persia, Royalty),
    "Penguin Classics",
    "English",
    80)


  val theHistories = BookCard(
    "0140449086",
    "Herodotus",
    "The Histories",
    "A record of ancient traditions of Western Asia, Northern Africa and Greece.",
    "450 to 420 BC",
    Set(Africa, Asia, Greece, Tradition),
    "Penguin Classics",
    "English",
    771)

  /**
    * Map containing book cards.
    */
  var books: Map[String, BookCard] = Map(
    phaedrus.isbn -> phaedrus,
    theEpicOfGilgamesh.isbn -> theEpicOfGilgamesh,
    theHistories.isbn -> theHistories)

  /**
    * Convert iterable to option
    *
    * @param l list of books
    * @return option of list of books
    */
  private def iterToOption(l: Iterable[BookCard]): Option[List[BookCard]] = l match {
    case found if found.nonEmpty => Some(found.toList)
    case _                       => None
  }

  def findBookByIsbn(isbn: String): Option[List[BookCard]] =
    iterToOption(books.get(isbn))

  def findBookByAuthor(author: String): Option[List[BookCard]] =
    iterToOption(books.values.filter(b => b.author == author))

  def findBookByTitle(title: String): Option[List[BookCard]] =
    iterToOption(books.values.filter(b => b.title == title))

  def findBookByTopic(topic: Set[Topic]): Option[List[BookCard]] =
    iterToOption(books.values.filter(b => b.topic.exists(t => topic.contains(t))))
}
