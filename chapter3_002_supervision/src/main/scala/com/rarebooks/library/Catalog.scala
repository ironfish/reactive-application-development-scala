package com.rarebooks.library

object Catalog {

  sealed trait Card {
    def title: String
    def description: String
    def tags: Set[String]
  }

  final case class BookCard(
      isbn: String,
      author: String,
      title: String,
      description: String,
      dateOfOrigin: String,
      tags: Set[String],
      publisher: String,
      language: String,
      pages: Int)
    extends Card

  val theEpicOfGilgamesh = BookCard(
    "0141026286",
    "unknown",
    "The Epic of Gilgamesh",
    "A hero is created by the gods to challenge the arrogant King Gilgamesh.",
    "2700 BC",
    Set("gilgamesh", "royalty", "persia"),
    "Penguin Classics",
    "English",
    80)

  val phaedrus = BookCard(
    "0872202208",
    "Plato",
    "Phaedrus",
    "Plato's enigmatic text that treats a range of important philosophical issues.",
    "370 BC",
    Set("philosophy", "greece"),
    "Hackett Publishing Company, Inc.",
    "English",
    144)

  val theHistories = BookCard(
    "0140449086",
    "Herodotus",
    "The Histories",
    "A record of ancient traditions of Western Asia, Northern Africa and Greece.",
    "450 to 420 BC",
    Set("tradition", "asia", "greece", "africa"),
    "Penguin Classics",
    "English",
    771)

  val books: Map[String, BookCard] = Map(
    theEpicOfGilgamesh.isbn -> theEpicOfGilgamesh,
    phaedrus.isbn -> phaedrus,
    theHistories.isbn -> theHistories)

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

  def findBookByTag(tags: Set[String]): Option[List[BookCard]] =
    iterToOption(books.values.filter(b => b.tags.exists(t => tags.contains(t))))

  //  def findAntiqueBooks(): Option[List[AntiqueBook]] =
//    Option(books.values.flatMap {
//      case x: AntiqueBook => Some(x)
//      case _ => None
//    } toList)
}

