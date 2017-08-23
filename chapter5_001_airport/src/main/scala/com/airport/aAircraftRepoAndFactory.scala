package com.airport

/**
 * A very basic Aircraft companion object that functions as both a factory and repository.
 */
object AircraftRepositoryAndFactory {

  val MockAircraft = Aircraft("someId", "300", 0.0, 0.0, 0.0, Nil, Nil)

  def create(id: String, callsign: String): Aircraft = Aircraft(id, callsign, 0.0, 0.0, 0.0, Nil, Nil)

  // Return a mock aircraft, we won't bother populating passengers and weather for now.
  def get(id: String): Aircraft = MockAircraft

  def getAll(): List[Aircraft] = List(MockAircraft)

  def findByCallsign(callsign: String): Aircraft = MockAircraft
}
