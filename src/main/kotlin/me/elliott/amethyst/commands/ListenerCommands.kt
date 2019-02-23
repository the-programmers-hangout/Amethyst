package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
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
            val result = it.args.component1() as ListenerState
            listenerService.addSourceOrDestination(result, it.args.component2(), true)
            it.respond("Source :: **${it.args.component2()}** added successfully")
        }
    }

    command("addDestination") {
        description = "Add a destination for message that match the listeners criteria."

        expect(ListenerArg, ChannelOrUserArg)

        execute {
            val result = it.args.component1() as ListenerState
            listenerService.addSourceOrDestination(result, it.args.component2(), false)
            it.respond("Destination :: **${it.args.component2()}** added successfully")
        }
    }
}