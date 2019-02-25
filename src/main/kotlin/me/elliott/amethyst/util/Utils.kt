package me.elliott.amethyst.util

import me.elliott.amethyst.data.Script
import java.util.*

class Utils {
    companion object {
        fun generateShortUUID(): String = UUID.randomUUID().toString().substring(0, 7)
    }
}


