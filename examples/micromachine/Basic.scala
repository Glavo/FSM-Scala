package micromachine

import org.glavo.micromachine._

object Basic {
  def main(args: Array[String]): Unit = {
    val fsm = MicroMachine('pending)

    fsm.when('confirm,  'pending -> 'confirmed)
    fsm.when('ignore,   'pending -> 'ignored)
    fsm.when('reset,    'confirmed -> 'pending, 'ignored -> 'pending)

    println( "Should print Confirmed, Reset and Ignored:")

    if(fsm trigger 'confirm)
    println( "Confirmed")

    if (fsm trigger 'ignore)
    println( "Ignored")

    if (fsm trigger 'reset)
      println( "Reset")

    if (fsm trigger 'ignore)
      println( "Ignored")
  }
}
