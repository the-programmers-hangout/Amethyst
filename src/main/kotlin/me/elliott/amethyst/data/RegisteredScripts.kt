package me.elliott.amethyst.data

import me.elliott.amethyst.util.Utils.Companion.generateShortUUID

data class Script(val id: String, var name: String, val author: String, var script: String, var status: String, var thread: Thread)

class RegisteredScripts {
    companion object {
         private var scripts = mutableMapOf<String, Script>()

        fun scriptsAreRegistered(): Boolean = scripts.isNotEmpty()

        fun scriptExists(id: String): Boolean = scripts.contains(id)
        fun getScripts() = scripts.entries.toList()
        fun getScript(id: String) = scripts[id]

        fun hasRunningScripts(): Boolean = scripts.filter { s -> s.value.status == "Running" }.isNotEmpty()
        fun hasStoppedScripts(): Boolean = scripts.filter { s -> s.value.status == "Stopped" }.isNotEmpty()

        fun addScript(name: String, author: String, script: String, t: Thread) {
            val id = generateShortUUID()
            scripts[id] = Script(id, name, author, script, "Running", t)
        }

        fun stopScript(id: String) {
            val script = getScript(id)
            script?.thread?.stop()
            script?.status = "Stopped"
        }

        fun startScript(id: String) {
            val script = getScript(id)
            script?.thread?.start()
            script?.status = "Running"
        }

        fun removeScript(id: String) {
            scripts.remove(id)
        }
    }
}