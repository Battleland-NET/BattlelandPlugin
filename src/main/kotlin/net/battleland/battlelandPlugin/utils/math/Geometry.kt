package net.battleland.battlelandPlugin.utils.math

import org.bukkit.Location
import kotlin.math.*

class Geometry {
    companion object {
        fun getPointOnCircle(radius: Double, angle: Double /*In radians*/, offset: Location): Location {
            var result = offset
            result.x += radius*cos(angle)
            result.z += radius*sin(angle)
            return result
        }

        fun getDirectionFromPointToAnother(point1: Location, point2: Location): Double {
            return Math.atan2(point2.y-point1.y, point2.x-point2.x)
        }
    }
}