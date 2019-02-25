package me.elliott.amethyst.util

import me.aberrantfox.kjdautils.api.dsl.embed
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.services.ListenerState
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.User
import java.awt.Color

class EmbedUtils {
    companion object {
        fun buildIntroductionEmbed(guildObject: Guild, listenerId: String) =
                embed {
                    setColor(Color.RED)
                    setTitle("Create Listener")
                    setAuthor("Let's create a listener in ${guildObject.name}")
                    description("\n Below is the ID you'll use when setting up the rest of the " +
                            "parameters for your listener. \n \n Please refer to the help for the **addSource**, " +
                            "**addDestination** and **addPattern** commands to continue.")
                    addField("ID", "**$listenerId**", true)
                    setThumbnail(guildObject.iconUrl)
                }

        fun buildScriptListEmbed(running: Boolean): MessageEmbed {

            val status = if (running) Constants.RUNNING else Constants.STOPPED
            val color = if (running) Color.GREEN else Color.RED

            return embed {
                title("$status Scripts")
                RegisteredScripts.getScripts(running).forEach { script ->
                    addField("ID", script.id, true)
                    addField("Author", script.author, true)
                    addField("Name", script.name, true)
                }
                color(color)
            }
        }

        fun buildMessageDestinationEmbed(listenerId: String, user: User, match: String) =
                embed {
                    setColor(Color.CYAN)
                    setTitle("Match")
                    setAuthor("Listener :: $listenerId matched")
                    description("${user.asMention} :: $match")
                    setThumbnail(user.avatarUrl)
                }


        fun buildSourceListEmbed(listenerState: ListenerState) =
                embed {
                    setColor(Color.PINK)
                    setTitle("Would you like to listen to anything else?")
                    setAuthor("You're currently listening to")

                    setThumbnail(listenerState.guild.iconUrl)
                }
    }
}