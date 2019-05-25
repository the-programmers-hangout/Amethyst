package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.elliott.amethyst.arguments.ChannelOrUserArg
import me.elliott.amethyst.arguments.ListenerArg
import me.elliott.amethyst.services.ListenerService
import me.elliott.amethyst.services.ListenerState
import net.dv8tion.jda.core.entities.TextChannel


@CommandSet
fun listenerCommands(listenerService: ListenerService) = commands {

    command("createListener") {
        description = "Create a listener."

        execute {
            val eventChannel = it.channel as TextChannel
            listenerService.createListener(it.author, eventChannel.guild, eventChannel)
        }
    }

    command("addSource") {
        description = "Add a listening source to a listener."

        expect(ListenerArg, ChannelOrUserArg)

        execute {
            val listener = it.args.component1() as ListenerState
            listenerService.addSource(listener, it.args.component2())
            it.respond("Source :: **${it.args.component2()}** added successfully")
        }
    }

    command("addDestination") {
        description = "Add a destination for message that match the listeners criteria."

        expect(ListenerArg, ChannelOrUserArg)

        execute {
            val listener = it.args.component1() as ListenerState
            listenerService.addDestination(listener, it.args.component2())
            it.respond("Destination :: **${it.args.component2()}** added successfully")
        }
    }

    command("addPattern") {
        description = "Add a pattern that the specified listener should match against."

        expect(ListenerArg, WordArg)

        execute {
            val listener = it.args.component1() as ListenerState
            val pattern = it.args.component2() as String

            listenerService.addPattern(listener, pattern)
            it.respond("Pattern :: **${it.args.component2()}** added successfully")
        }
    }
}