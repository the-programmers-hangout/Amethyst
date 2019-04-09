package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.*
import me.elliott.amethyst.services.ListenerService

@CommandSet
fun utilityCommands(listenerService: ListenerService) = commands {
    command("ping") {
        description = "Check the status of the bot."
        execute {
            it.respond("pong! (${it.jda.ping}ms)")
        }
    }

    command("source") {
        description = "Display the source code via a GitLab link."
        execute {
            it.respond("https://github.com/elliottshort/amethyst")
        }
    }

    command("Author") {
        description = "Display the bot author."
        execute {
            it.respond("Elliott#0001")
        }
    }
}