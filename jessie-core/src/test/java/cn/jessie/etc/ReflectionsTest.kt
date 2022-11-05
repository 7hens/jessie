package cn.jessie.etc

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author 7hens
 */
class ReflectionsTest {

    @Test
    operator fun invoke() {
        Reflections.invoke(ReflectionTarget::class.java, "invoke", 1)
    }

}