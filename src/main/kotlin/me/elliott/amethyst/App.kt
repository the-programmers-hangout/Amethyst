package me.elliott.amethyst

import me.aberrantfox.kjdautils.api.startBot
import me.elliott.amethyst.data.RegisteredScripts
import net.dv8tion.jda.core.entities.Game
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import me.elliott.amethyst.util.Constants


fun main(args: Array<String>) {
    val token = args.first()
    start(token)
}

private fun start(token: String) = startBot(token) {

    configure {
        prefix = "-"
        globalPath = "me.elliott.amethyst"
    }

    jda.presence.setPresence(Game.of(Game.GameType.WATCHING, "Your Scripts"), true)

    Timer("scriptWatcher", true).scheduleAtFixedRate(1000,
            1000) {
        if (RegisteredScripts.hasRunningScripts()) {
            RegisteredScripts.getAllScripts().forEach { script ->
                try {
                    if (script.watch)
                        script.context.close(false)
                } catch (e: Exception) {
                }
                try {
                    script.context.polyglotBindings
                } catch (e: Exception) {
                    script.status = Constants.STOPPED
                }
            }
        }
    }
}





