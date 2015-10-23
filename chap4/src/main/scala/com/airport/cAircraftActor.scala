package com.airport

import akka.actor.Actor

class AircraftActor(
  id: String,
  callsign: String,
  altitude: Double,
  speed: Double,
  heading: Double,
  passengers: List[Passenger],
  weather: List[Weather]) extends Actor {

  import AircraftProtocol._

  var currentState: Aircraft = Aircraft(id, callsign, altitude, speed, heading, passengers, weather)

  def receive = {
    case ChangeAltitude(altitude)    =>
      currentState = currentState.copy(altitude = altitude)
      sender() ! OK

    case ChangeSpeed(speed)          =>
      currentState.copy(speed = speed)
      sender() ! OK

    case ChangeHeading(heading)      =>
      currentState = currentState.copy(heading = heading)
      sender() ! OK

    case BoardPassenger(passenger)   =>
      currentState = currentState.copy(passengers = passenger :: passengers)
      sender() ! OK

    case AddWeather(incomingWeather) =>
      currentState = currentState.copy(weather = incomingWeather :: weather)
      sender() ! OK
  }
}
