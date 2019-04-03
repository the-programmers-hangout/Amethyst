package me.elliott.amethyst.services

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.Utils
import org.graalvm.polyglot.*
import java.io.File

class ScriptEngineService {

    fun exec(name: String, author: String, script: String, event: CommandEvent,
             id: String = Utils.generateShortUUID()) {

        val context = Context.newBuilder()
                .allowIO(true)
                .allowAllAccess(true)
                .allowHostAccess(true)
                .allowNativeAccess(true)
                .allowHostClassLoading(true)
                .build()

        try {
            val setupScripts = File("scripts${File.separator}js")

            context.polyglotBindings.putMember("event", event)
            context.polyglotBindings.putMember("jda", event.jda)

            walkDirectory(setupScripts, context)

            val language = bayes.classify(script.split(" "))

            println("Detected Language :: ${language.category}")

            if (language.category == Constants.JS)
                context.eval(Constants.JS, createFunctionContext(script))
            else
                context.eval(language.category, script)

        } catch (e: PolyglotException) {
            event.respond("Error :: ${e.cause} - ${e.message}")
        }
        RegisteredScripts.addScript(id, name, author, script, context)
    }
}

fun walkDirectory(dir: File, context: Context) = dir.walk()
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

