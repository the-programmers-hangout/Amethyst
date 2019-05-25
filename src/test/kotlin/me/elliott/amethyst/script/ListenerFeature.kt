package me.elliott.amethyst.script

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.elliott.amethyst.data.RegisteredListeners
import me.elliott.amethyst.mocks.jda.guildMock
import me.elliott.amethyst.mocks.jda.jdaMock
import me.elliott.amethyst.mocks.jda.produceTextChannelMock
import me.elliott.amethyst.mocks.jda.produceUserMock
import me.elliott.amethyst.mocks.makeCommandEventMock
import me.elliott.amethyst.services.ListenerService
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertTrue

internal object ListenerFeature : Spek({

    lateinit var event: CommandEvent

    Feature("Setup a listener to monitor specific users, patterns or channels.") {

        event = makeCommandEventMock(guildMock)
        val listenerService = ListenerService(jdaMock)

        Scenario("listener lifecycle") {
            When("a listener is created") {
                listenerService.createListener(produceUserMock(), guildMock,
                        produceTextChannelMock(guildMock))
            }
            
            Then("there should be a single listener registered") {
                assertTrue(RegisteredListeners.listeners.isNotEmpty(), "no listeners registered")
            }
        }
    }
})


