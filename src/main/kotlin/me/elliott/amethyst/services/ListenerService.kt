package me.elliott.amethyst.services


import me.aberrantfox.kjdautils.api.annotation.Service
import me.elliott.amethyst.data.RegisteredListeners
import me.elliott.amethyst.util.EmbedUtils
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import java.util.*
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

data class ListenerState(val id: String, val user: User, val guild: Guild,
                         var sources: MutableList<Any>, var destinations: MutableList<Any>)

@Service
class ListenerService(val jda: JDA, val configuration: Configuration) {

    private fun generateShortUUID(): String = UUID.randomUUID().toString().substring(0, 7)


    fun createListener(user: User, guild: Guild, channel: MessageChannel) {

        val listener = ListenerState(generateShortUUID(), user, guild,
                mutableListOf(), mutableListOf())

        RegisteredListeners.registerListener(listener)
        channel.sendMessage(EmbedUtils.buildIntroductionEmbed(listener.guild, listener.id)).queue()
    }

    fun addSourceOrDestination(listenerState: ListenerState, sourceOrDestination: Any, isSource: Boolean) {
        if (isSource) {
            listenerState.sources.add(sourceOrDestination)
        } else {
            listenerState.destinations.add(sourceOrDestination)
        }
    }


    private fun addPattern(listenerState: ListenerState, pattern: String) {

    }


    sealed class Source {
        class Everywhere(val listenEverywhere: Boolean) : Source()
        class User(val user: net.dv8tion.jda.core.entities.User) : Source()
        class Channel(val channel: TextChannel) : Source()
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
}

