package sux.server.game

import scala.collection.immutable.HashMap

sealed trait Spec {
  val spec: String
}

object Specs {
  val specs: HashMap[String, Spec] = HashMap(
    "Rifleman" -> Rifleman,
    "Engineer" -> Engineer,

    "Soda" -> Soda,
  )

  trait Health {
    val maxHealth: Float
  }
  trait Storage {
    val maxStorage: Float
  }
  trait LeggedMover {
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

  object Rifleman extends Spec with Human {
    override val spec: String = "Rifleman"
    override val maxStorage: Float = 100
  }
  object Engineer extends Spec with Human {
    override val spec: String = "Engineer"
    override val maxStorage: Float = 120
    override protected val speedBaseMultiplier: Float = 0.85f
  }

  trait Item {
    val name: String
    val volume: Float
  }

  object Soda extends Spec with Item {
    override val spec: String = "Soda"
    override val name: String = "Soda"
    override val volume: Float = 3.5f
  }
}
