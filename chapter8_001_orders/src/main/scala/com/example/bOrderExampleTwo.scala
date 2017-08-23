package com.example

import java.util.UUID

import akka.persistence.PersistentActor
import org.scalactic._
import Accumulation._

import OrderType._

object OrderActor {

  object OrderType {

    type OrderType = String

    val Phone = new OrderType("phone")
    val Web = new OrderType("web")
    val Promo = new OrderType("promo")
    val OrderTypes: List[OrderType] = List(Phone, Web, Promo)
  }

  case object CommandAccepted
  case class ExpectedVersionMismatch(expected: Long, actual: Long)

  case class CreateOrder(
    id: UUID,
    customerId: String,
    date: Long,
    orderType: OrderType,
    orderLines: List[OrderLine])

  case class AddOrderLine(
    id: UUID,
    orderLine: OrderLine,
    expectedVersion: Long)

  case class OrderCreated(
   id: UUID,
   customerId: String,
   date: Long,
   orderType: OrderType,
   orderLines: List[OrderLine],
   version: Long)

  case class OrderLineAdded(
    id: UUID,
    orderLine: OrderLine,
    version: Long)
}

class OrderActor extends PersistentActor {

  import OrderActor._

  override def persistenceId: String = self.path.parent.name + "-" + self.path.name

  private case class OrderState(
    id: UUID = null,
    customerId: String = null,
    date: Long = -1L,
    orderType: OrderType = null,
    orderLines: List[OrderLine] = Nil, version: Long = -1L)

  private var state = OrderState()

  def create: Receive = {
    case CreateOrder(id, customerId, date, orderType, orderLines) =>
      val validations = withGood(
        validateCustomerId(customerId),
        validateDate(date),
        validateOrderType(orderType),
        validateOrderLines(orderLines)
      ) { (cid, d, ot, ol) => OrderCreated(UUID.randomUUID(), cid, d, ot, ol, 0L) }
      sender ! validations.fold(
        event => {
          sender ! CommandAccepted
          persist(event) { e =>
            state = OrderState(event.id, event.customerId, event.date, event.orderType, event.orderLines, 0L)
            context.system.eventStream.publish(e)
            context.become(created)
          }
        },
        bad  => sender ! bad
      )
  }

  def created: Receive = {
    case AddOrderLine(id, orderLine, expectedVersion) =>
      if (expectedVersion != state.version)
        sender ! ExpectedVersionMismatch(expectedVersion, state.version)
      else {
        val validations = withGood(
          validateOrderLines(state.orderLines :+ orderLine)
        ) { (ol) => OrderLineAdded(id, orderLine, state.version + 1) }
        .fold(
          event => {
            persist(OrderLineAdded(id, orderLine, state.version + 1)) { e =>
              state = state.copy(orderLines = state.orderLines :+ e.orderLine, version = state.version + 1)
              context.system.eventStream.publish(e)
            }
          },
          bad => sender ! bad
        )
      }
  }

  override def receiveCommand = create

  override def receiveRecover: Receive = {
    case CreateOrder(id, customerId, date, orderType, orderLines) =>
      state = OrderState(id, customerId, date, orderType, orderLines, 0L)
      context.become(created)
    case AddOrderLine(id, orderLine, expectedVersion)             =>
      state = state.copy(orderLines = state.orderLines :+ orderLine, version = state.version + 1)
  }

  def validateCustomerId(customerId: String): String Or Every[ValidationFailure] =
    if (Option(customerId).exists(_.trim.nonEmpty))
      Good(customerId)
    else
      Bad(One(InvalidCustomerId(customerId)))

  private def validateDate(date: Long): Long Or Every[ValidationFailure] =
    if (date > 0)
      Good(date)
    else
      Bad(One(InvalidDate(date.toString)))

  private def validateOrderType(orderType: OrderType): OrderType Or Every[ValidationFailure] =
    if (OrderTypes.contains(orderType))
      Good(orderType)
    else
      Bad(One(InvalidOrderType(orderType)))

  private def validateOrderLines(orderLines: List[OrderLine]): List[OrderLine] Or Every[ValidationFailure] =
    if (!orderLines.isEmpty)
      Good(orderLines)
    else
      Bad(One(InvalidOrderLines(orderLines.mkString)))
}
