package save.my.ass

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSh() {
        var value = 0
        for (i in 6.downTo(0)) {
            print("${1.shl(i)},")
            value = value.or(1.shl(i))
            println("$value,${value.toString(2)}")
        }
        println(value.toString(2))
    }

    @Test
    fun testShAnd() {
        val value = 120
        for (i in 5.downTo(0)) {
            println("${value.and(1.shl(i)) == 1.shl(i)},${1.shl(i).toString(2)},${value.toString(2)}")
        }
    }
}
