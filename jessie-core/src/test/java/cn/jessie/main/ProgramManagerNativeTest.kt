package cn.jessie.main

import cn.jessie.app.activity.ActivityStubs
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgramManagerNativeTest {
    @Test
    fun test() {
        val stubId = ActivityStubs.ID.of(12, 1, 56)
        println(stubId)
        assertEquals(ActivityStubs.ID.processIndex(stubId), 12)
        assertEquals(ActivityStubs.ID.launchMode(stubId), 1)
        assertEquals(ActivityStubs.ID.number(stubId), 56)
    }
}