package me.elliott.amethyst.services

import net.dv8tion.jda.core.JDA
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ScriptEngineService {

    object EngineContainer {

        var engine: ScriptEngine? = null

        fun setupScriptEngine(jda: JDA, config: Configuration): ScriptEngine {

            val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")!!

            engine.put("jda", jda)
            engine.put("config", config)

            return engine
        }

        const val functionName = "functionScope"

        fun generateFunctionContext(script: String) =
                """
        fun $functionName(val event: Any) {
            $script
        };
        """.trimIndent()
    }
}

