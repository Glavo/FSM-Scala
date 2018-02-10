package micromachine

import org.glavo.micromachine._

object Callbacks {
  def main(args: Array[String]): Unit = {
    val fsm = MicroMachine('pending)

    fsm.when('confirm, 'pending -> 'confirmed)
    fsm.when('ignore, 'pending -> 'ignored)
    fsm.when('reset, 'confirmed -> 'pending, 'ignored -> 'pending)

    println("Should print Confirmed, Reset and Ignored:")

    (fsm on 'confirmed) {
      println("Confirmed")
    }

    (fsm on 'ignored) {
      println("Ignored")
    }

    (fsm on 'pending) {
      println("Reset")
    }


    fsm trigger 'confirm

    fsm trigger 'ignore

    fsm trigger 'reset

    fsm trigger 'ignore
  }
}
