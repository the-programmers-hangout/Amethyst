package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.*

@CommandSet
fun utilityCommands() = commands {
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
