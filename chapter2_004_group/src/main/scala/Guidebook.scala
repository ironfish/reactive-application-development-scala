object Guidebook {

  case class Inquiry(code: String)

}

import java.util.{Currency, Locale}

import akka.actor.Actor


import Guidebook.Inquiry
import Tourist.Guidance

class Guidebook extends Actor {
  def describe(locale: Locale) =
    s"""In ${locale.getDisplayCountry}, ${locale.getDisplayLanguage} is spoken and the currency is the ${Currency.getInstance(locale).getDisplayName}"""

  override def receive = {
    case Inquiry(code) =>
      println(s"Actor ${self.path.name} responding to inquiry about $code")
      Locale.getAvailableLocales.filter(_.getCountry == code).
        foreach { locale =>
          sender ! Guidance(code, describe(locale))
        }
  }
}
