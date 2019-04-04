package me.elliott.amethyst.services

import com.sun.tools.classfile.ConstantPool
import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.Utils
import org.graalvm.polyglot.*
import java.io.File

class ScriptEngineService {

    fun exec(name: String, author: String, language: String, script: String, event: CommandEvent,
             id: String = Utils.generateShortUUID()) {

        val context = Context.newBuilder()
                .option("js.nashorn-compat", "true")
                .allowIO(true)
                .allowAllAccess(true)
                .allowHostAccess(true)
                .allowNativeAccess(true)
                .allowHostClassLoading(true)
                .build()

        val setupScripts = File("scripts${File.separator}${Constants.JS}")
        val bindings = mapOf("event" to event, "jda" to event.jda)

        bindings.forEach {
            context.getBindings(Constants.JS).putMember(it.key, it.value)
            context.polyglotBindings.putMember(it.key, it.value)
        }

        try {

            walkDirectory(setupScripts, context)

            if (language == Constants.JS)
                context.eval(Constants.JS, createFunctionContext(script))
            else
                context.eval(language, script)

            RegisteredScripts.addScript(id, name, author, language, script, context)
        } catch (e: PolyglotException) {
            event.respond("Error :: ${e.cause} - ${e.message}")
            RegisteredScripts.removeScript(id)
        }
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

