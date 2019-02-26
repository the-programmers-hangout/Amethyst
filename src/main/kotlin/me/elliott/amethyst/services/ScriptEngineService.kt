package me.elliott.amethyst.services

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.Utils
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.PolyglotException
import java.io.File
import kotlin.concurrent.timer

class ScriptEngineService {

    fun exec(name: String,
             author: String, script: String, event: CommandEvent, id: String = Utils.generateShortUUID()) {

        val context = Context.newBuilder().option("js.nashorn-compat", "true")
                .allowHostAccess(true).build()

            try {

                val setupScripts = File("config/scripts${File.separator}jsapi")
                val bindings = context.getBindings("js")

                bindings.putMember("event", event);
                bindings.putMember("jda", event.jda)
                walkDirectory(setupScripts, context)

                context.eval("js", createFunctionContext(script))

            } catch (e: PolyglotException) {
                event.respond("Error :: ${e.cause} - ${e.message}")
            }

        RegisteredScripts.addScript(id, name, author, script, context)
        }
    }

private fun walkDirectory(dir: File, context: Context) = dir.walk()
        .filter { !it.isDirectory }
        .map { it.readText() }
        .forEach { context.eval(Constants.JS, it) }

fun createFunctionContext(scriptBody: String) =
        """
        function ${Constants.FUNCTION_NAME}(event) {
            $scriptBody
        }

        ${Constants.FUNCTION_NAME}(event);
    """.trimIndent()

fun setupScriptWatcher() {
            timer(name = "script-watcher", initialDelay = 0, period = 1000) {
                if (RegisteredScripts.scriptsAreRegistered())
                    RegisteredScripts.getAllScripts().forEach {
                        //TODO: Figure out how to get the state of the thread Graal executes the evaluated script in.
                    }
            }
}


