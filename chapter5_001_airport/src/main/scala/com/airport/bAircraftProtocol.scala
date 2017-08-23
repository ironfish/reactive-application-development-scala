package com.airport

/**
 * The aircraft protocol used for interaction with aircraft modeled as an actor domain
 * with messaging as the communication.
 */
object AircraftProtocol {
  sealed trait AircraftProtocolMessage
  final case class ChangeAltitude(altitude: Double) extends AircraftProtocolMessage
  final case class ChangeSpeed(speed: Double) extends AircraftProtocolMessage
  final case class ChangeHeading(heading: Double) extends AircraftProtocolMessage
  final case class BoardPassenger(passenger: Passenger) extends AircraftProtocolMessage
  final case class AddWeather(weather: Weather) extends AircraftProtocolMessage
  final case object OK
}
