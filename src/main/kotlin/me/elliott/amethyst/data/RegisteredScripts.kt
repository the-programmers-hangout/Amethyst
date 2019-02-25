package me.elliott.amethyst.data

import me.elliott.amethyst.util.Constants

data class Script(val id: String, var name: String, val author: String, var script: String, var status: String, var thread: Thread)

class RegisteredScripts {

    companion object {

        private var scripts = mutableMapOf<String, Script>()

        fun scriptsAreRegistered(): Boolean = scripts.isNotEmpty()
        fun scriptExists(id: String): Boolean = scripts.contains(id)
        fun getAllScripts(): List<Script> = scripts.values.toList()
        fun getScript(id: String) = scripts[id]
        fun tryReturnScript(id: String): Script? = scripts.values.toList().firstOrNull { l -> l.id == id }
        fun hasRunningScripts(): Boolean = scripts.filter { s -> s.value.status == Constants.RUNNING }.isNotEmpty()
        fun hasStoppedScripts(): Boolean = scripts.filter { s -> s.value.status == Constants.STOPPED }.isNotEmpty()

        fun getScripts(running: Boolean): List<Script> = scripts.values.filter { s ->
            s.status == if (running) Constants.RUNNING else Constants.STOPPED
        }

        fun addScript(id: String, name: String, author: String, script: String, t: Thread) {
            scripts[id] = Script(id, name, author, script, Constants.RUNNING, t)
        }

        fun stopScript(id: String) {
            val script = getScript(id)
            script?.thread?.stop()
            script?.status = Constants.STOPPED
        }

        fun startScript(id: String) {
            val script = getScript(id)
            script?.thread?.start()
            script?.status = Constants.RUNNING
        }

        fun removeScript(id: String) {
            scripts.remove(id)
        }
    }
}