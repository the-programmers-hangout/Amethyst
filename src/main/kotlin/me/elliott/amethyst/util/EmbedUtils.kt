package me.elliott.amethyst.util

import me.aberrantfox.kjdautils.api.dsl.embed
import me.elliott.amethyst.services.ListenerState
import net.dv8tion.jda.core.entities.Guild
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


        fun buildSourceAddedEmbed(listenerState: ListenerState, isUser: Boolean) =
                embed {
                    setColor(Color.PINK)
                    setTitle("Would you like to listen to anything else?")
                    setAuthor("You're currently listening to ")

                    setThumbnail(listenerState.guild.iconUrl)
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