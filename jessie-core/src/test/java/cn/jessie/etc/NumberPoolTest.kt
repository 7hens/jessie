package cn.jessie.etc

import org.junit.Assert.*
import org.junit.Test

class NumberPoolTest {
    @Test
    fun test() {
        val pool = NumberPool(5)
        assertEquals(pool.get(), 0)
        assertEquals(pool.get(), 1)
        assertEquals(pool.get(), 2)
        assertEquals(pool.get(), 3)
        assertEquals(pool.get(), 4)
        assertEquals(pool.get(), -1)
        assertEquals(pool.force(), 0)
        assertEquals(pool.force(), 1)
        assertEquals(pool.force(), 2)
        assertEquals(pool.force(), 3)
        assertEquals(pool.force(), 4)
    }
}