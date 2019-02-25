package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.arguments.SentenceArg
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.elliott.amethyst.data.RegisteredScripts
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.MessageBuilder
import java.awt.Color
import java.io.File
import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import javax.script.SimpleBindings
import javax.script.Bindings



object EngineContainer {
    var engine: ScriptEngine? = null

    fun setupScriptEngine(jda: JDA, container: CommandsContainer): ScriptEngine {
        val engine = ScriptEngineManager().getEngineByName("graal.js");
        engine.put("jda", jda)
        engine.put("container", container)

        val setupScripts = File("config/scripts${File.separator}jsapi")

//        val custom =  File(configPath("scripts${File.separator}custom"))

        walkDirectory(setupScripts, engine)

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
    command("eval") {
        description = "Evaluate Nashorn JavaScript code - without an automatic response."
        expect(WordArg("Name of the script (no spaces)"), SentenceArg("Nashorn JavaScript Code"))
        execute {
            val name = it.args.component1() as String
            val script = it.args.component2() as String

            try {
                execWithThread(EngineContainer.engine, name, it.author.asMention, script, it)
            } catch (e: Exception) {
                it.respond("${e.message} - **cause** - ${e.cause}")
            }
        }
    }

    command("list-scripts") {
        description = "List all currently running scripts"
        execute {
            when (RegisteredScripts.scriptsAreRegistered()) {

                (RegisteredScripts.hasRunningScripts()) -> {
                    it.respond(embed {
                        title("Running Scripts")
                        RegisteredScripts.getScripts().forEach { script ->
                            addField("ID", script.value.id, true)
                            addField("Author", script.value.author, true)
                            addField("Name", script.value.name, true)

                        }
                        color(Color.GREEN)
                    })
                }

                RegisteredScripts.hasStoppedScripts() -> {
                    it.respond(embed {
                        title("Stopped Scripts")
                        RegisteredScripts.getScripts().forEach { script ->
                            addField("ID", script.value.id, true)
                            addField("Author", script.value.author, true)
                            addField("Name", script.value.name, true)

                        }
                        color(Color.RED)
                    })
                }

                else -> {
                    it.respond("I don't have any scripts registered.")
                }
            }
        }
    }

    command("stop-script") {
        description = "Stop the specified script"
        expect(WordArg)
        execute {
            val id = it.args.component1().toString()
            if (RegisteredScripts.scriptExists(id)) {
                RegisteredScripts.stopScript(id)
                it.respond(embed {
                    title("Script $id Stopped")
                    color(Color.RED)
                })
            } else {
                it.respond("That script doesn't exist.")
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


    command("view-script") {
        description = "View Script Content"
        expect(WordArg("ID of the script you'd like to view."))
        execute {
            val id = it.args.component1() as String
            if (RegisteredScripts.getScript(id) != null) {
                it.respond(embed {
                    title("Script Content - ID: **$id**")
                })

                it.channel.sendMessage(MessageBuilder().appendCodeBlock(
                        RegisteredScripts.getScript(id)?.script, "Javascript")
                        .build()).queue()
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

private fun execWithThread(
        engine: ScriptEngine?, name: String, author: String, script: String, commandEvent: CommandEvent) {

    val functionContext = createFunctionContext(script)

    val r = Runnable {
        try {
            engine?.eval(functionContext)
            (EngineContainer.engine as Invocable).invokeFunction(functionName, commandEvent)

        } catch (e: ScriptException) {
            commandEvent.respond("${e.message} - **cause** - ${e.cause}")
        }
    }

    val t = Thread(r)

    t.start()
    RegisteredScripts.addScript(name, author, script, t)
}