package me.elliott.amethyst.commands

import me.aberrantfox.kjdautils.api.dsl.CommandSet
import me.aberrantfox.kjdautils.api.dsl.commands
import me.elliott.amethyst.services.ListenerService
import me.elliott.amethyst.util.Utils

@CommandSet
fun utilityCommands(listenerService: ListenerService) = commands {
    command("ping") {
        description = "Check the status of the bot."
        execute {
            it.respond("pong! (${it.jda.ping}ms)")
        }
    }

    command("check-languages") {
        description = "Lists the languages available to write scripts in."
        execute {
            it.respond(Utils.getAvailableLanguages())
        }
    }

    command("source") {
        description = "Display the source code via a GitLab link."
        execute {
            it.respond("https://gitlab.com/tphelliott/amethyst")
        }
    }

    command("Author") {
        description = "Display the bot author."
        execute {
            it.respond("Elliott#0001")
        }
    }
}