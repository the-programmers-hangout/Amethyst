package me.elliott.amethyst.script

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.mocks.jda.guildMock
import me.elliott.amethyst.mocks.makeCommandEventMock
import me.elliott.amethyst.services.ExecutionResult
import me.elliott.amethyst.services.ScriptEngineService
import me.elliott.amethyst.util.Constants
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.graalvm.polyglot.Context
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal object AddScriptFeature : Spek({

    lateinit var event: CommandEvent

    Feature("Allows you to evaluate custom scripts in your Discord server") {
        val engineContext by memoized {
            Context.newBuilder()
                    .option("js.nashorn-compat", "true")
                    .allowIO(true)
                    .allowAllAccess(true)
                    .allowHostAccess(true)
                    .allowNativeAccess(true)
                    .allowHostClassLoading(true)
                    .build()
        }

        Scenario("checking the scripting languages that are available") {

            Then("more than one should be available ") {
                assertTrue(engineContext.engine.languages.isNotEmpty(), "no languages available")
            }
        }

        Scenario("evaluating a script") {

            event = makeCommandEventMock(guildMock)
            val returned = ScriptEngineService().exec(Constants.JS, "var result = 1+1;", event)

            When("i attempt to register a script") {
                if (returned is ExecutionResult.Success) {
                    RegisteredScripts.addScript(returned.id, "test-script", event.author.asMention,
                            Constants.JS, "var result = 1+1;", returned.context, false)
                }
            }

            Then("the script executiion should be successful") {
                assertTrue(returned is ExecutionResult.Success, "script execution failed")
            }

            Then("it should be registered successfully and there should only be one script available") {
                assertEquals(1, RegisteredScripts.getAllScripts().size,
                        "incorrect number of scripts registered")
            }
        }
    }
})

