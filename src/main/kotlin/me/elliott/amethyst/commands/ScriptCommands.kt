package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.elliott.amethyst.arguments.InstalledLanguageArg
import me.elliott.amethyst.arguments.ScriptIdArg
import me.elliott.amethyst.arguments.YesNoArg
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.data.ScriptData
import me.elliott.amethyst.services.ScriptEngineService
import me.elliott.amethyst.services.ExecutionResult
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.EmbedUtils
import net.dv8tion.jda.core.MessageBuilder
import java.awt.Color

@CommandSet("api")
fun scriptCommands() = commands {
    command("eval") {
        description = "Evaluate code using Graal - without an automatic response."
        expect(WordArg("Name of the script (no spaces)"), InstalledLanguageArg("Name of the script language"),
                YesNoArg("Watch Script Execution"), SentenceArg("Code"))

        execute {
            val name = it.args.component1() as String
            val language = it.args.component2() as String
            val watch = it.args.component3() as Boolean
            val script = it.args.component4() as String
            val returned = ScriptEngineService().exec(language, script, it)

            when (returned) {
                is ExecutionResult.Error -> {
                    it.respond(returned.message)
                }

                is ExecutionResult.Success -> {
                    RegisteredScripts.addScript(returned.id, name, it.author.asMention,
                            language, script, returned.context, watch)
                }
            }
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
        expect(ScriptIdArg("The ID of the script you'd like to stop."))
        execute {

            val script = it.args.component1() as ScriptData
            RegisteredScripts.stopScript(script)

            it.respond(
                    embed {
                        title("Script ${script.id} Stopped")
                        color(Color.RED)
                    })
        }
    }

    command("start-script") {
        description = "Start the specified script"

        expect(ScriptIdArg("The ID of the script you'd like to start."))
        execute {
            val script = it.args.component1() as ScriptData
            RegisteredScripts.startScript(script, it)

            it.respond(
                    embed {
                        title("Script ${script.id} Started")
                        color(Color.GREEN)
                    })
        }
    }

    command("view-script") {
        description = "View Script Content"
        expect(ScriptIdArg("ID of the script you'd like to view."))

        execute {

            val script = it.args.component1() as ScriptData
            it.respond(embed {
                title("Script Content - ID: **${script.id}**")
            })

            MessageBuilder().appendCodeBlock(
                    script.script, Constants.JAVASCRIPT)

            val id = it.args.component1() as String
            it.respond(embed {
                title("Script Content - ID: **$id**")
            })

            MessageBuilder().appendCodeBlock(
                    RegisteredScripts.getScript(id)?.script, "Javacript")
                    .buildAll().forEach { message ->
                        it.channel.sendMessage(message).queue()
                    }
        }
    }
}
