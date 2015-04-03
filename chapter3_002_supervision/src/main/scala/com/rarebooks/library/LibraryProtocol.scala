package com.rarebooks.library

/**
 * This file contains the messages for the rare books info service.
 */
object LibraryProtocol {

  /** trait for all messages. */
  trait Msg {
    def dateInMillis: Long
  }

  /** Marker trait for all commands */
  trait Cmd extends Msg

  /** Marker trait for all events */
  trait Evt extends Msg
}

