package sbd.pemgami.TasksPlanner

object PointsCalculator {
    private const val factor = 5

    // for every minute user gets x points
    fun calcPoints(minutes: Int): Int {
        return minutes * factor
    }
}