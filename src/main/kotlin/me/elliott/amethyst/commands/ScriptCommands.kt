package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.elliott.amethyst.arguments.ScriptArg
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.services.ScriptEngineService
import me.elliott.amethyst.util.EmbedUtils
import net.dv8tion.jda.core.MessageBuilder
import java.awt.Color
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings


@CommandSet("api")
fun scriptCommands() = commands {
    command("eval") {
        description = "Evaluate JavaScript code using Graal - without an automatic response."
        expect(WordArg("Name of the script (no spaces)"), SentenceArg("Graal JavaScript Code"))
        execute {
            val name = it.args.component1() as String
            val script = it.args.component2() as String

            ScriptEngineService().execWithThread(ScriptEngineService.engine, name,
                    it.author.asMention, script, it)
        }
    }

    command("list-scripts") {
        description = "List all currently running scripts"
        execute {
            if (RegisteredScripts.scriptsAreRegistered()) {
                if (RegisteredScripts.hasRunningScripts()) {
                    it.respond(EmbedUtils.buildScriptListEmbed(true))
                }
                if (RegisteredScripts.hasStoppedScripts()) {
                    it.respond(EmbedUtils.buildScriptListEmbed(false))
                }
            } else {
                it.respond("No scripts are currently registered.")
            }
        }
    }

    command("stop-script") {
        description = "Stop the specified script"
        expect(ScriptArg("The ID of the script you'd like to stop."))
        execute {
            val id = it.args.component1().toString()
                RegisteredScripts.stopScript(id)

            it.respond(
                    embed {
                    title("Script $id Stopped")
                    color(Color.RED)
                })
        }
    }

    command("view-script") {
        description = "View Script Content"
        expect(ScriptArg("ID of the script you'd like to view."))

        execute {
            val id = it.args.component1() as String
                it.respond(embed {
                    title("Script Content - ID: **$id**")
                })

            MessageBuilder().appendCodeBlock(
                        RegisteredScripts.getScript(id)?.script, "Javacript")
                    .buildAll().forEach { message ->
                        it.channel.sendMessage(message)
                    }
        }
    }

    command("eval-ruby") {
        description = "Stop the specified script"
        expect(SentenceArg)
        execute {

            val script = it.args.component1().toString()
            val manager = ScriptEngineManager()
            val engine = manager.getEngineByName("jruby")
            val bindings = SimpleBindings()
            bindings["event"] = it

            engine.eval(script, bindings)
        }
    }
}

