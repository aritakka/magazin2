data class Point(val x: Double, val y: Double) {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
}

class Triangle(private val a: Point, private val b: Point, private val c: Point) {
    private fun cross(u: Point, v: Point) = u.x * v.y - u.y * v.x

    // Площадь треугольника (получаем половину, но для сравнения фактически используется 2*площади)
    private fun area2(p1: Point, p2: Point, p3: Point) = cross(p2 - p1, p3 - p1)

    // Возвращает:
    //  1 если точка внутри или на границе,
    //  0 если на границе (отдельно) — опционально,
    // -1 если вне,
    // false для вырожденного треугольника (area == 0)
    fun contains(pt: Point): Boolean {
        val eps = 1e-12
        val A = area2(a, b, c)
        if (kotlin.math.abs(A) < eps) return false // вырожденный

        val A1 = area2(pt, b, c)
        val A2 = area2(a, pt, c)
        val A3 = area2(a, b, pt)

        // Совпадают ли знаки площадей (включая ноль) — тогда точка внутри или на границе
        val hasNeg = (A1 < -eps) || (A2 < -eps) || (A3 < -eps)
        val hasPos = (A1 > eps) || (A2 > eps) || (A3 > eps)

        return !(hasNeg && hasPos) // если есть и положительные и отрицательные — вне
    }

    // Опционально: различать "внутри" и "на границе"
    fun location(pt: Point): String {
        val eps = 1e-12
        val A = area2(a, b, c)
        if (kotlin.math.abs(A) < eps) return "degenerate"

        val A1 = area2(pt, b, c)
        val A2 = area2(a, pt, c)
        val A3 = area2(a, b, pt)

        val hasNeg = (A1 < -eps) || (A2 < -eps) || (A3 < -eps)
        val hasPos = (A1 > eps) || (A2 > eps) || (A3 > eps)
        val onEdge = kotlin.math.abs(A1) <= eps || kotlin.math.abs(A2) <= eps || kotlin.math.abs(A3) <= eps

        return when {
            hasNeg && hasPos -> "outside"
            onEdge -> "on edge"
            else -> "inside"
        }
    }
}

fun main() {
    val tri = Triangle(Point(0.0, 0.0), Point(5.0, 0.0), Point(1.0, 4.0))

    val tests = listOf(
        Point(1.0, 1.0),
        Point(5.0, 0.0),
        Point(3.0, 0.0),
        Point(4.0, 2.0),
        Point(-1.0, 0.0)
    )

    for (p in tests) {
        println("Point $p is ${tri.location(p)} the triangle")
    }
}

