package me.elliott.amethyst.services

import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent
import java.awt.Color

data class ListenerState(val jda: JDA, val guild: Guild, val sources: List<Source>, val destinations: List<Destination>)

fun createListenerState(jda: JDA, guild: Guild, sources: List<Source>, destinations: List<Destination>) {

}

class ListenerService(val jda: JDA, val configuration: Configuration) {

    private val listenerStates = mutableListOf<ListenerState>()

    @Subscribe
    fun onPrivateMessageReceived(event: PrivateMessageReceivedEvent) {

    }

    fun createListenerState(name: String, conditions: List<Condition>, destination: Destination) {
        
    }

    fun buildIntroductionEmbed(guildObject: Guild) =
            embed {
                setColor(Color.PINK)
                setTitle("Create Listener")
                setAuthor("Let's create a listener in ${guildObject.name} - Where would you like me to listen to?")
                description("Please provide a channel or user id, user mention, or simply say 'Everywhere'")
                setThumbnail(guildObject.iconUrl)
            }

    fun buildYesNoSourceChoiceEmbed(listenerState: ListenerState) =
            embed {
                setColor(Color.PINK)
                setTitle("Would you like to listen to anything else?")
                setAuthor("You're currently listening to ${getSourceNamesString(listenerState.sources)}")
                description("Please provide a channel or user id, user mention, or simply say 'Everywhere'")
                setThumbnail(listenerState.guild.iconUrl)
            }
}

class Listener(val name: String, val conditions: List<Condition>, val destination: Destination)

data class Condition(val source: Source, val matches: Boolean)

sealed class Source {
    class Everywhere(val listenEverywhere: Boolean) : Source()
    class User(val user: net.dv8tion.jda.core.entities.User) : Source()
    class Channel(val channel: TextChannel) : Source()
    class Error(val error: String) : Source()
}

sealed class Destination {
    data class User(val user: net.dv8tion.jda.core.entities.User) : Destination()
    data class Channel(val channel: TextChannel) : Destination()
    data class Error(val error: String) : Destination()
}

fun getSourceNamesString(sources: List<Source>): String {
    var sourceString = ""
    sources.forEach {
        when (it) {
            is Source.Everywhere -> {
                sourceString = "Listening to all messages, by all users, in all channels. "
            }
            is Source.User -> {
                sourceString += "**User** :: ${it.user.asMention} "
            }
            is Source.Channel -> {
                sourceString += "**Channel** :: ${it.channel.asMention} "
            }
        }
    }
    return sourceString
}



