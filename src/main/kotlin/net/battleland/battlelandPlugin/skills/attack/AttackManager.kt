package net.battleland.battlelandPlugin.skills.attack

import net.battleland.battlelandPlugin.utils.math.Geometry
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.*

class AttackManager {

    fun attack(
        player: Player,
        shape: AttackShape,
        range: Double,
        thicc: Double,
        damage: Double,
        knockback: Double = 0.0
    ) {
        var nearbyEntities = player.getNearbyEntities(range, range, 128.0)



        nearbyEntities = nearbyEntities.filterIsInstance<Damageable>()

        when (shape) {
            AttackShape.CONE -> {
                nearbyEntities =
                    nearbyEntities.filter { it.location.distanceSquared(player.location) <= range.pow(2) }
                nearbyEntities = nearbyEntities.filter {
                    val angle = abs(
                        Math.toDegrees(
                            atan2(
                                it.location.x - player.location.x,
                                it.location.z - player.location.z
                            )
                        )
                    ) - player.location.yaw
                    println(angle)
                    return@filter (thicc / 2 > angle)
                }
                coneParticles(player, range, thicc)
            }
            AttackShape.RAY -> {
                nearbyEntities =
                    nearbyEntities.filter { it.location.distanceSquared(player.location) <= range.pow(2) }
                nearbyEntities = nearbyEntities.filter {
                    val angle = abs(
                        Math.toDegrees(
                            atan2(
                                it.location.x - player.location.x,
                                it.location.z - player.location.z
                            )
                        )
                    ) - player.location.yaw
                    return@filter (1 > angle)
                }
            }
            AttackShape.CIRCLE -> {
                nearbyEntities =
                    nearbyEntities.filter { it.location.distanceSquared(player.location) <= range.pow(2) }
                circleParticles(player, range)
            }
        }

        for (e in nearbyEntities) {
            (e as Damageable).damage(damage)
            e.velocity = Vector(e.location.x - player.location.x, e.location.y - player.location.y, 0.0).multiply(knockback)

            val particleLocation = e.location
            particleLocation.y += 2
            player.location.world?.spawnParticle(
                Particle.REDSTONE,
                particleLocation,
                1,
                Particle.DustOptions(Color.BLUE, 100f)
            )
        }
    }

    private fun coneParticles(player: Player, range: Double, thicc: Double, particleSpaceTarget: Double = 20.0) {
        val particleCount = round(thicc / particleSpaceTarget).toInt()
        for (i in (-particleCount / 2)..particleCount / 2) {
            player.location.world?.spawnParticle(
                Particle.REDSTONE,
                Geometry.getPointOnCircle(
                    range,
                    Math.toRadians(i * thicc / particleCount + player.location.yaw + 90),
                    player.location
                ),
                10,
                Particle.DustOptions(Color.BLUE, 100f)
            )
        }
    }

    private fun circleParticles(player: Player, range: Double) {
        for (i in 0..9) {
            player.location.world?.spawnParticle(Particle.REDSTONE, Geometry.getPointOnCircle(range, i*36.0, player.location), 10, Particle.DustOptions(Color.BLUE, 100f))
        }
    }
}