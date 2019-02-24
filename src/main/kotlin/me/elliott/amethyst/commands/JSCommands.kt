package me.elliott.amethyst.commands

import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.CommandsContainer
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import net.dv8tion.jda.core.JDA
import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine

object EngineContainer {
    var engine: ScriptEngine? = null

    fun setupScriptEngine(jda: JDA, container: CommandsContainer): ScriptEngine {
        val engine = NashornScriptEngineFactory().getScriptEngine("--language=es6", "-scripting")
        engine.put("jda", jda)
        engine.put("container", container)

//        val setupScripts = File(configPath("scripts${File.separator}jsapi"))
//        val custom =  File(configPath("scripts${File.separator}custom"))

        return engine
    }

    private fun walkDirectory(dir: File, engine: ScriptEngine) = dir.walk()
            .filter { !it.isDirectory }
            .map { it.readText() }
            .forEach { engine.eval(it) }
}

private const val functionName = "functionScope"

@CommandSet("api")
fun jsCommands() = commands {
    command("eval-js") {
        description = "Evaluate Nashorn JavaScript code - without an automatic response."
        expect(SentenceArg("Nashorn JavaScript Code"))
        execute {
            val script = it.args.component1() as String
            val functionContext = createFunctionContext(script)

            try {
                EngineContainer.engine?.eval(functionContext)
                (EngineContainer.engine as Invocable).invokeFunction(functionName, it)
            } catch (e: Exception) {
                it.respond("${e.message} - **cause** - ${e.cause}")
            }
        }
    }
}

private fun createFunctionContext(scriptBody: String) =
        """
        function $functionName(event) {
            $scriptBody
        };
    """.trimIndent()
