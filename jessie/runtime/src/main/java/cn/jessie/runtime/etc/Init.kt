package cn.jessie.runtime.etc

import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author 7hens
 */
class Init private constructor() {
    private val isInitialized = AtomicBoolean(false)

    fun getElseInitialize(): Boolean {
        return !isInitialized.compareAndSet(false, true)
    }

    fun isInitialized(): Boolean {
        return isInitialized.get()
    }

    companion object {
        fun create(): Init {
            return Init()
        }
    }
}