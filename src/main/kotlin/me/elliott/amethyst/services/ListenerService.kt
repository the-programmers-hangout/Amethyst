package me.elliott.amethyst.services

import me.aberrantfox.kjdautils.api.annotation.Service
import me.elliott.amethyst.data.RegisteredListeners
import me.elliott.amethyst.util.EmbedUtils
import me.elliott.amethyst.util.Utils
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageChannel
import net.dv8tion.jda.core.entities.User
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

data class ListenerState(val id: String, val user: User, val guild: Guild,
                         var sources: MutableList<Any>, var destinations: MutableList<Any>, var patterns: MutableList<Any>)

@Service
class ListenerService(val jda: JDA) {

    fun createListener(user: User, guild: Guild, channel: MessageChannel) {

        val listener = ListenerState(Utils.generateShortUUID(), user, guild,
                mutableListOf(), mutableListOf(), mutableListOf())

        RegisteredListeners.registerListener(listener)
        channel.sendMessage(EmbedUtils.buildIntroductionEmbed(listener.guild, listener.id)).queue()
    }

    fun addDestination(listenerState: ListenerState, destination: Any) = listenerState.destinations.add(destination)
    fun addSource(listenerState: ListenerState, source: Any) = listenerState.sources.add(source)

    fun addPattern(listenerState: ListenerState, pattern: String) {
        if (pattern.isRegex())
            listenerState.patterns.add(Regex(pattern, RegexOption.IGNORE_CASE))
        else
            listenerState.patterns.add(pattern)
    }


    fun patternsMatch(listenerState: ListenerState, message: String): Boolean {
        var patternsMatch = false
        var numberOfMatches = 0

        listenerState.patterns.forEach { pattern ->
            when (pattern) {
                is Regex -> {
                    if (pattern.containsMatchIn(message))
                        numberOfMatches += 1
                }
                is String -> {
                    if (message.toLowerCase().contains(pattern.toLowerCase()))
                        numberOfMatches += 1
                }
            }
        }

        if (numberOfMatches == listenerState.patterns.size)
            patternsMatch = true

        println("Number of matches :: $numberOfMatches")
        return patternsMatch
    }

    private fun String.isRegex(): Boolean {
        var isRegex = true

        try {
            Pattern.compile(this)
        } catch (e: PatternSyntaxException) {
            isRegex = false
        }
        return isRegex
    }
}

