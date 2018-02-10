package micromachine

import org.glavo.micromachine._

object Advanced {
  def main(args: Array[String]): Unit = {
    val fsm = MicroMachine('pending)

    fsm.when('confirm, 'pending -> 'confirmed)
    fsm.when('ignore, 'pending -> 'ignored)
    fsm.when('reset, 'confirmed -> 'pending, 'ignored -> 'pending)

    println("Should print Confirmed, Pending and Ignored:")

    fsm trigger 'confirm
    assert(fsm.state == 'confirmed)

    fsm trigger 'ignore
    assert(fsm.state == 'confirmed)

    fsm trigger 'reset
    assert(fsm.state == 'pending)

    fsm trigger 'ignore
    assert(fsm.state == 'ignored)

    println("Should print all states: pending, confirmed, ignored")
    println(fsm.states mkString ", ")

    println("Should print all events: confirm, ignore, reset")
    println(fsm.events mkString ", ")

  }
}

