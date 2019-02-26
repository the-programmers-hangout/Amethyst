package me.elliott.amethyst.data


import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.services.ScriptEngineService
import me.elliott.amethyst.util.Constants
import org.graalvm.polyglot.Context

data class ScriptData(val id: String, var name: String, val author: String, var script: String,
                  var status: String, var context: Context)

class RegisteredScripts {

    companion object {

        private var scripts = mutableMapOf<String, ScriptData>()

        fun scriptsAreRegistered(): Boolean = scripts.isNotEmpty()
        fun scriptExists(id: String): Boolean = scripts.contains(id)
        fun getAllScripts(): List<ScriptData> = scripts.values.toList()
        fun getScript(id: String) = scripts.values.toList().firstOrNull { s -> s.id == id }
        fun tryReturnScript(id: String): ScriptData? = scripts.values.toList().firstOrNull { l -> l.id == id }
        fun hasRunningScripts(): Boolean = scripts.filter { s -> s.value.status == Constants.RUNNING }.isNotEmpty()
        fun hasStoppedScripts(): Boolean = scripts.filter { s -> s.value.status == Constants.STOPPED }.isNotEmpty()

        fun getScripts(running: Boolean): List<ScriptData> = scripts.values.filter { s ->
            s.status == if (running) Constants.RUNNING else Constants.STOPPED
        }

        fun addScript(id: String, name: String, author: String, script: String, context: Context) {
            scripts[id] = ScriptData(id, name, author, script, Constants.RUNNING, context)
        }

        fun stopScript(script: ScriptData) {
            script.status = Constants.STOPPED
            script.context.close(true)
        }

        fun startScript(script: ScriptData, event: CommandEvent) {
            removeScript(script.id)
            ScriptEngineService().exec(script.name,
                    script.author, script.script, event, script.id)
        }

        fun removeScript(id: String) {
            scripts.remove(id)
        }
    }
}