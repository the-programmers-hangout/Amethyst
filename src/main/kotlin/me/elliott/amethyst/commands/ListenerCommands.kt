package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.aberrantfox.kjdautils.internal.command.arguments.WordArg
import me.elliott.amethyst.services.ListenerService
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
        description = "Create a listener."

        expect(WordArg)

        execute {
            val eventChannel = it.channel as TextChannel
            listenerService.createListener(it.author, eventChannel.guild, eventChannel)
        }


    }
}