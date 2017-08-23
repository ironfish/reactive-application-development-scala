package com.rarebooks.library

import scala.reflect.runtime.universe._

class RareBooksProtocolSpec extends BaseAkkaSpec("as-3002-rare-books-protocol-spec") {

  import RareBooksProtocol._

  "Rare books protocol messages" should {
    "throw IllegalArgumentException when 'BookFound.books' is empty" in process(typeOf[BookFound])
    "throw IllegalArgumentException when 'BookNotFound.reason' is empty" in process(typeOf[BookNotFound])
    "throw IllegalArgumentException when 'FindBookByIsbn.isbn' is empty" in process(typeOf[FindBookByIsbn])
    "throw IllegalArgumentException when 'FindBookByAuthor.author' is empty" in process(typeOf[FindBookByAuthor])
    "throw IllegalArgumentException when 'FindBookByTitle.title' is empty" in process(typeOf[FindBookByTitle])
    "throw IllegalArgumentException when 'FindBookByTopic.topic' is empty" in process(typeOf[FindBookByTopic])
  }

  private def process(t: Type) = {
    intercept[IllegalArgumentException] {
      t match {
        case found    if t =:= typeOf[BookFound]        => BookFound(List())
        case notFound if t =:= typeOf[BookNotFound]     => BookNotFound("")
        case isbn     if t =:= typeOf[FindBookByIsbn]   => FindBookByIsbn("")
        case author   if t =:= typeOf[FindBookByAuthor] => FindBookByAuthor("")
        case title    if t =:= typeOf[FindBookByTitle]  => FindBookByTitle("")
        case tag      if t =:= typeOf[FindBookByTopic]  => FindBookByTopic(Set())
      }
    }
  }
}
