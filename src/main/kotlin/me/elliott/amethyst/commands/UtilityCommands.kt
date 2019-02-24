package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import me.elliott.amethyst.services.ListenerService
import me.elliott.amethyst.services.ScriptEngineService
import net.dv8tion.jda.core.entities.TextChannel
import java.lang.Exception
import javax.script.Invocable
import javax.script.ScriptEngineManager

@CommandSet
fun utilityCommands(listenerService: ListenerService) = commands {
    command("ping") {
        description = "Check the status of the bot."
        execute {
            it.respond("pong! (${it.jda.ping}ms)")
        }
    }

    command("source") {
        description = "Display the source code via a GitLab link."
        execute {
            it.respond("https://github.com/elliottshort/amethyst")
        }
    }

    command("Author") {
        description = "Display the bot author."
        execute {
            it.respond("Elliott#0001")
        }
    }
}

@CommandSet("api")
fun kotlinScriptCommands() = commands {
    command("eval") {
        description = "Evaluate Kotlin Script code - without an automatic response."
        expect(SentenceArg("Kotlin Code"))
        execute {
            val script = it.args.component1() as String
            val functionContext = ScriptEngineService.EngineContainer.generateFunctionContext(script)

            val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
            with(scriptEngine) {

                try {
                    it.respond(eval(script).toString())
                } catch(e: Exception) {
                    it.respond("Error :: could not evaluate script - ${e.cause}")
                }
            }
        }
    }
}
