package com.example

import java.util.UUID

import org.scalactic._
import Accumulation._
import OrderType._

trait ValidationFailure {
  def message: String
}

case class InvalidId(message: String) extends ValidationFailure
case class InvalidCustomerId(message: String) extends ValidationFailure
case class InvalidOrderType(message: String) extends ValidationFailure
case class InvalidDate(message: String) extends ValidationFailure
case class InvalidOrderLine(message: String) extends ValidationFailure
case class InvalidOrderLines(message: String) extends ValidationFailure

case class Order private[Order] (
  id: String,
  customerId: String,
  date: Long,
  orderType: OrderType,
  orderLines: List[OrderLine])

object OrderType {

  type OrderType = String

  val Phone = new OrderType("phone")
  val Web = new OrderType("web")
  val Promo = new OrderType("promo")
  val OrderTypes: List[OrderType] = List(Phone, Web, Promo)
}

case class OrderLine(
  itemId: String,
  quantity: Int
)

object Order {

  def apply(customerId: String, date: Long, orderType: OrderType, orderLines: List[OrderLine]): Order Or Every[ValidationFailure] = {
    withGood(
      validateCustomerId(customerId),
      validateDate(date),
      validateOrderType(orderType),
      validateOrderLines(orderLines)
    ) { (cid, dt, ot, ols) => Order(UUID.randomUUID.toString, cid, dt, ot, ols) }
  }

  private def validateId(id: String): String Or Every[ValidationFailure] =
    if (id !=null && !id.isEmpty)
      Good(id)
    else
      Bad(One(InvalidId(id)))

  private def validateDate(date: Long): Long Or Every[ValidationFailure] =
    if (date > 0)
      Good(date)
    else
      Bad(One(InvalidDate(date.toString)))

  private def validateCustomerId(customerId: String): String Or Every[ValidationFailure] =
    if (customerId !=null && !customerId.isEmpty)
      Good(customerId)
    else
      Bad(One(InvalidCustomerId(customerId)))

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
