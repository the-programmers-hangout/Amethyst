package me.elliott.amethyst.services

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.api.dsl.CommandsContainer
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.Utils
import net.dv8tion.jda.core.JDA
import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import kotlin.concurrent.timer

class ScriptEngineService {

    private val functionName = "functionScope"
    private fun createFunctionContext(scriptBody: String) =
            """
        function $functionName(event) {
            $scriptBody
        };
    """.trimIndent()

    fun execWithThread(engine: ScriptEngine?, name: String,
                       author: String, script: String, commandEvent: CommandEvent, id : String = Utils.generateShortUUID()) {

        val functionContext = createFunctionContext(script)

        val r = Runnable {
            try {
                engine?.eval(functionContext)
                (me.elliott.amethyst.services.ScriptEngineService.engine as Invocable)
                        .invokeFunction(functionName, commandEvent)

            } catch (e: ScriptException) {
                val existingScript = RegisteredScripts.getScript(id)

                when (existingScript != null) {
                    existingScript?.status == Constants.STOPPED -> {
                        //TODO :: Stop Notification
                    }
                    else -> {
                        commandEvent.respond("${e.message} - **cause** - ${e.cause}")
                        RegisteredScripts.removeScript(id)
                    }
                }
            }
        }

        val t = Thread(r)
        t.start()

        if (!RegisteredScripts.scriptExists(id))
            RegisteredScripts.addScript(id, name, author, script, t)
    }

    companion object EngineContainer {

        var engine: ScriptEngine? = null

        fun setupScriptEngine(jda: JDA, container: CommandsContainer): ScriptEngine {
            val engine = ScriptEngineManager().getEngineByName("graal.js");
            engine.put("jda", jda)
            engine.put("container", container)

            val setupScripts = File("config/scripts${File.separator}jsapi")
            walkDirectory(setupScripts, engine)

            return engine
        }

        fun setupScriptWatcher() {
            timer(name = "script-watcher", initialDelay = 0, period = 1000) {
                if (RegisteredScripts.scriptsAreRegistered())
                    RegisteredScripts.getAllScripts().forEach {
                        //TODO: Figure out how to get the state of the thread Graal executes the evaluated script in.
                    }
            }
        }

        private fun walkDirectory(dir: File, engine: ScriptEngine) = dir.walk()
                .filter { !it.isDirectory }
                .map { it.readText() }
                .forEach { engine.eval(it) }
    }
}


