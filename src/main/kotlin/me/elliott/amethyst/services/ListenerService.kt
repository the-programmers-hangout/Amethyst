package me.elliott.amethyst.services


import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.api.dsl.embed
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.*
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.awt.Color
import java.util.*
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

data class ListenerState(val id: String, val user: User, val guild: Guild,
                         val sources: List<Source>, val destinations: List<Destination>)

@Service
class ListenerService(val jda: JDA, val configuration: Configuration) {

    private val listeners = mutableListOf<ListenerState>()
    private var activeSessions = mutableMapOf<String, ListenerState>()

    private fun hasActiveBuildingSession(userId: String) = activeSessions.any { it.key == userId }
    private fun generateShortUUID(): String = UUID.randomUUID().toString().substring(0, 7)
    fun getListener(id: String): ListenerState = listeners.first { l -> l.id == id }

    fun createListener(user: User, guild: Guild, channel: MessageChannel) {

        if (hasActiveBuildingSession(user.id))
            return

        val listener = ListenerState(generateShortUUID(), user, guild,
                mutableListOf(), mutableListOf())

        listeners.add(listener)
        channel.sendMessage(buildIntroductionEmbed(listener.guild, listener.id)).queue()
    }

    private fun handleResponse(listenerState: ListenerState, event: MessageReceivedEvent) {

    }

    private fun addSource(listenerState: ListenerState) {

    }

    private fun addPattern(listenerState: ListenerState) {

    }

    private fun addDestination(listenerState: ListenerState) {

    }

    private fun buildIntroductionEmbed(guildObject: Guild, listenerId: String) =
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
                setAuthor("You're currently listening to ${getSourceNamesString(listenerState.sources)}")

                setThumbnail(listenerState.guild.iconUrl)
            }
}

fun buildSourceListEmbed(listenerState: ListenerState) =
        embed {
            setColor(Color.PINK)
            setTitle("Would you like to listen to anything else?")
            setAuthor("You're currently listening to ${getSourceNamesString(listenerState.sources)}")

            setThumbnail(listenerState.guild.iconUrl)
        }
}

class Listener(val name: String, val conditions: List<Condition>, val destinations: List<Destination>)

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


fun String.isRegex(): Boolean {
    var isRegex = true

    try {
        Pattern.compile(this)
    } catch (e: PatternSyntaxException) {
        isRegex = false
    }
    return isRegex
}

