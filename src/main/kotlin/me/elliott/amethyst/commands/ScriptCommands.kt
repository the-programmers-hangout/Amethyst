package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.api.dsl.embed
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.elliott.amethyst.arguments.ScriptArg
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.data.ScriptData
import me.elliott.amethyst.services.ScriptEngineService
import me.elliott.amethyst.services.createFunctionContext
import me.elliott.amethyst.services.walkDirectory
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.EmbedUtils
import net.dv8tion.jda.core.MessageBuilder
import org.graalvm.polyglot.Context
import org.graalvm.polyglot.PolyglotException
import java.awt.Color
import java.io.File
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

            ScriptEngineService().exec(name,
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
        expect(ScriptArg("The ID of the script you'd like to start."))
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
        expect(ScriptArg("ID of the script you'd like to view."))

        execute {
            val script = it.args.component1() as ScriptData
            it.respond(embed {
                title("Script Content - ID: **${script.id}**")
            })

            MessageBuilder().appendCodeBlock(
                    script.script, Constants.JAVASCRIPT)
                    .buildAll().forEach { message ->
                        it.channel.sendMessage(message).queue()
                    }
        }
    }

    command("eval-ruby") {
        description = "Stop the specified script"
        expect(SentenceArg)
        execute {

            val context = Context.newBuilder().allowHostAccess(true).build()

            try {

                val setupScripts = File("scripts${File.separator}js")
                val bindings = context.getBindings(Constants.JS)

                bindings.putMember("event", it)
                bindings.putMember("jda", it.jda)

                val rubyBindings = context.getBindings("ruby")

                rubyBindings.putMember("event", it)
                rubyBindings.putMember("jda", it.jda)

                walkDirectory(setupScripts, context)

                context.eval("ruby", createFunctionContext(it.args.component1() as String))

            } catch (e: PolyglotException) {
                it.respond("Error :: ${e.cause} - ${e.message}")
                e.printStackTrace()
            }

        }
    }
}


