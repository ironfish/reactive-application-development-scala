package com.airport

import akka.actor.{ReceiveTimeout, Actor, ActorRef}

import scala.concurrent.duration._

object BankTransferProcessProtocol {
  sealed trait BankTransferProcessMessage

  final case class TransferFunds(
    transactionId: String,
    fromAccount: ActorRef,
    toAccount: ActorRef,
    amount: Double) extends BankTransferProcessMessage
}

object BankTransferProcess {
  final case class FundsTransfered(transactionId: String)
  final case class TransferFailed(transactionId: String)
}

object AccountProtocol {
  sealed trait AccountProtocolMessage
  case class Withdraw(amount: Double) extends AccountProtocolMessage
  case class Deposit(amount: Double) extends AccountProtocolMessage
  final case object Acknowledgment
}

class BankTransferProcess extends Actor {

  import BankTransferProcess._
  import BankTransferProcessProtocol._
  import AccountProtocol._

  context.setReceiveTimeout(30.minutes)

  override def receive = {
    case TransferFunds(transactionId, fromAccount, toAccount, amount) =>
      fromAccount ! Withdraw(amount)
      val client = sender()
      context become awaitWithdrawal(transactionId, amount, toAccount, client)
  }

  def awaitWithdrawal(transactionId: String, amount: Double, toAccount: ActorRef, client: ActorRef): Receive = {
    case Acknowledgment =>
      toAccount ! Deposit(amount)
      context become awaitDeposit(transactionId, client)

    case ReceiveTimeout =>
      client ! TransferFailed(transactionId)
      context.stop(self)
  }

  def awaitDeposit(transactionId: String, client: ActorRef): Receive = {
    case Acknowledgment =>
      client ! FundsTransfered(transactionId)
      context.stop(self)

    case ReceiveTimeout =>
      client ! TransferFailed(transactionId)
      context.stop(self)
  }
}
