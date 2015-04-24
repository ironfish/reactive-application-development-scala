package com.rarebooks.library

import scala.util.parsing.combinator.RegexParsers

trait Console {

  protected sealed trait Command

  protected object Command {
    case class Customer(count: Int, odds: Int, tolerance: Int) extends Command
    case object Quit extends Command
    case class Unknown(command: String) extends Command
    def apply(command: String): Command = CommandParser.parseCommand(command)
  }

  private object CommandParser extends RegexParsers {

    def parseCommand(s: String): Command =
      parseAll(parser, s) match {
        case Success(command, _) => command
        case _                   => Command.Unknown(s)
      }

    def createCustomer: Parser[Command.Customer] =
      opt(int) ~ ("customer|c".r ~> opt(int) ~ opt(int)) ^^ {
        case count ~ (odds ~ tolerance) =>
          Command.Customer(
            count getOrElse 1,
            odds getOrElse 75,
            tolerance getOrElse 3)
      }

    def quit: Parser[Command.Quit.type] =
      "quit|q".r ^^ (_ => Command.Quit)

    def int: Parser[Int] =
      """\d+""".r ^^ (_.toInt)
  }

  private val parser: CommandParser.Parser[Command] =
    CommandParser.createCustomer | CommandParser.quit
}
