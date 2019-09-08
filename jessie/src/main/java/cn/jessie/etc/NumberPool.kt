package cn.jessie.etc

import java.util.*

internal class NumberPool(count: Int) {
    private var runningQueue = LinkedList<Int>()
    private var record = (0.inv() ushr 1) % (1 shl count)

    fun get(): Int {
        return if (record > 0) {
            val rest = record.and(record - 1)
            val result = Integer.numberOfTrailingZeros(record - rest)
            runningQueue.add(result)
            record = rest
            result
        } else {
            return -1
        }
    }

    fun force(): Int {
        val result = runningQueue.pop()
        runningQueue.add(result)
        return result
    }

    fun recycle(number: Int) {
        runningQueue.remove(number)
        record = record or (1 shl number)
    }
}