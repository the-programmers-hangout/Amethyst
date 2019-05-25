package me.elliott.amethyst.script

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.mocks.jda.guildMock
import me.elliott.amethyst.mocks.makeCommandEventMock
import me.elliott.amethyst.services.ExecutionResult
import me.elliott.amethyst.services.ScriptEngineService
import me.elliott.amethyst.util.Constants
import me.elliott.amethyst.util.TestConstants
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import org.graalvm.polyglot.Context
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal object ScriptEvaluationFeature : Spek({

    lateinit var event: CommandEvent

    Feature("Allows you to run custom scripts in your Discord server in your " +
            "preferred programming language") {

        event = makeCommandEventMock(guildMock)

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
            Then("more than one should be available") {
                assertTrue(engineContext.engine.languages.isNotEmpty(), "no languages available")
            }
        }

        Scenario("evaluating and registering a script") {
            val returned = ScriptEngineService().exec(Constants.JS, TestConstants.TEST_JS_SCRIPT, event)

            When("i execute and register a script") {
                if (returned is ExecutionResult.Success) {
                    RegisteredScripts.addScript(returned.id, "test-script", event.author.asMention,
                            Constants.JS, TestConstants.TEST_JS_SCRIPT, returned.context, false)
                }
            }

            Then("the script execution should be successful") {
                assertTrue(returned is ExecutionResult.Success, "script execution failed")
            }

            Then("it should be registered successfully and there should only be one script registered") {
                assertEquals(1, RegisteredScripts.getAllScripts().size,
                        "incorrect number of scripts registered")
            }
        }

        Scenario("removing a registered script") {
            Then("there should only be one script available to remove") {
                assertEquals(1, RegisteredScripts.getAllScripts().size,
                        "there isn't a script available to remove")
            }

            When("the script is removed") {
                RegisteredScripts.removeScript(RegisteredScripts.getAllScripts().first().id)
            }

            Then("we should be able to verify the removal was successful") {
                assertEquals(true, RegisteredScripts.getAllScripts().isEmpty(),
                        "script removal failed.")
            }
        }
    }
})


