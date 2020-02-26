package sux.server.game

import scala.collection.immutable.HashMap

object Specs {
  import Items._

  val humans: HashMap[String, Human] = HashMap(
    "Rifleman" -> Rifleman,
    "Engineer" -> Engineer
  )
  val items: HashMap[String, Item] = HashMap(
    "Soda" -> Soda
  )

  private trait Health {
    val maxHealth: Float
  }
  private trait Storage {
    val maxStorage: Float
  }
  private trait LeggedMover {
    protected val proneBaseSpeed: Float
    protected val crouchBaseSpeed: Float
    protected val walkBaseSpeed: Float
    protected val runBaseSpeed: Float
    protected val sprintBaseSpeed: Float

    protected val speedBaseMultiplier: Float = 1.0f

    final def proneSpeed: Float = proneBaseSpeed * speedBaseMultiplier
    final def crouchSpeed: Float = crouchBaseSpeed * speedBaseMultiplier
    final def walkSpeed: Float = walkBaseSpeed * speedBaseMultiplier
    final def runSpeed: Float = runBaseSpeed * speedBaseMultiplier
    final def sprintSpeed: Float = sprintBaseSpeed * speedBaseMultiplier
  }

  trait Human extends Health with Storage with LeggedMover {
    override val maxHealth: Float = 100
    override val maxStorage: Float = 50
    override val proneBaseSpeed: Float = 0.4f
    override val crouchBaseSpeed: Float = 1.2f
    override val walkBaseSpeed: Float = 2.0f
    override val runBaseSpeed: Float = 4.4f
    override val sprintBaseSpeed: Float = 5.8f
  }

  object Rifleman extends Human {
    override val maxStorage: Float = 100
  }
  object Engineer extends Human {
    override val maxStorage: Float = 120
    override protected val speedBaseMultiplier: Float = 0.85f
  }

  object Items {
    trait Item {
      val name: String
      val volume: Float
    }
    trait Junk extends Item {

    }
    object Soda extends Junk {
      override val name: String = "Soda"
      override val volume: Float = 3.5f
    }
  }
}
