package org.glavo.micromachine

import scala.collection.mutable

class MicroMachine(val initialState: MicroMachine.State) {
  type Event = MicroMachine.Event
  type State = MicroMachine.State

  type InvalidEventException = MicroMachine.InvalidEventException
  type InvalidStateException = MicroMachine.InvalidStateException

  private var _state: State = initialState
  private val callbacks: mutable.HashMap[State, mutable.ArrayBuffer[(Event, Any) => Any]] = mutable.HashMap()
  val transitionsFor: mutable.HashMap[Event, Seq[(State, State)]] = mutable.HashMap()

  def on[U](event: Event)(f: => U): Unit = {
    callbacks.getOrElseUpdate(event, mutable.ArrayBuffer()) += ((_: Event, _: Any) => f)
  }

  def onEvent[A, U](event: Event)(f: (Event, A) => U): Unit = {
    callbacks.getOrElseUpdate(event, mutable.ArrayBuffer()) += f.asInstanceOf[(Event, Any) => Any]
  }

  def when(event: Event, transitions: (State, State)*): Unit = {
    this.transitionsFor(event) = transitions.toList
  }

  object trigger {
    def apply(event: Event, payload: Any = null): Boolean = ?(event) && change(event, payload)

    def !(event: Event, payload: Any = null): Unit = apply(event, payload) || (throw new InvalidEventException)

    def ?(event: Event): Boolean = transitionsFor.keySet.contains(event)

  }

  def events: scala.collection.Set[Event] = transitionsFor.keySet

  def triggerableEvents: scala.collection.Set[Event] = events.filter(it => trigger ? it)

  def states: Set[State] = transitionsFor.values.flatten.flatMap(it => List(it._1, it._2)).toSet

  def state: Symbol = this._state

  private def change(event: Symbol, payload: Any = null): Boolean = {
    transitionsFor(event).find { case (from, _) => from == _state }.foreach { case (_, to) => _state = to }

    (this.callbacks.getOrElse(state, Nil) ++ this.callbacks.getOrElse('any, Nil)).foreach(f => f(event, payload))
    true
  }
}

object MicroMachine {

  type Event = Symbol
  type State = Symbol

  class InvalidEventException extends RuntimeException

  class InvalidStateException extends RuntimeException

  def apply(initialState: State): MicroMachine = new MicroMachine(initialState)
}