package me.elliott.amethyst

import me.aberrantfox.kjdautils.api.startBot
import me.elliott.amethyst.commands.EngineContainer
import me.elliott.amethyst.commands.EngineContainer.setupScriptEngine
import net.dv8tion.jda.core.entities.Game

fun main(args: Array<String>) {
    val token = args.first()
    start(token)
}

private fun start(token: String) = startBot(token) {
    configure {
        prefix = "-"
        globalPath = "me.elliott.amethyst"
    }

    EngineContainer.engine = setupScriptEngine(jda, container)
    jda.presence.setPresence(Game.of(Game.GameType.WATCHING, "The Server"), true)
}

