package me.elliott.amethyst.util



import org.graalvm.polyglot.Context
import java.util.*

class Utils {
    companion object {
        fun generateShortUUID(): String = UUID.randomUUID().toString().substring(0, 7)

        fun getInstalledLanguagesString(): String {
            val context = Context.newBuilder().allowAllAccess(true)
                    .build()
            val result = context.engine.languages.entries.map { l -> l.key }.toString()
            context.close(true)

            return result
        }
    }
}

fun <T : Comparable<T>> case(target: T, tester: Tester<T>.() -> Unit) {
    val test = Tester(target)
    test.tester()
    test.funFiltered?.invoke() ?: return
}

class Tester<T : Comparable<T>>(val it: T) {
    var funFiltered: (() -> Unit)? = null
    infix operator fun Boolean.minus(block: () -> Unit) {
        if (this && funFiltered == null) funFiltered = block
    }

    fun lt(arg: T) = it < arg
    fun gt(arg: T) = it > arg
    fun ge(arg: T) = it >= arg
    fun le(arg: T) = it <= arg
    fun eq(arg: T) = it == arg
    fun ne(arg: T) = it != arg
    fun inside(arg: Collection<T>) = it in arg
    fun inside(arg: String) = it as String in arg
    fun outside(arg: Collection<T>) = it !in arg
    fun outside(arg: String) = it as String !in arg
}


