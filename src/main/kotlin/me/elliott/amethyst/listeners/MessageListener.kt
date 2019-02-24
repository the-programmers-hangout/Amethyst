package me.elliott.amethyst.listeners

import com.google.common.eventbus.Subscribe
import me.aberrantfox.kjdautils.api.annotation.Service
import me.aberrantfox.kjdautils.extensions.jda.sendPrivateMessage
import me.elliott.amethyst.data.RegisteredListeners
import me.elliott.amethyst.services.ListenerService
import me.elliott.amethyst.util.EmbedUtils
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent

@Service
class MessageListener(listenerService: ListenerService) {

    val service = listenerService

    @Subscribe
    fun handleMessage(event: GuildMessageReceivedEvent) {

        if (event.author.isBot)
            return

        RegisteredListeners.listeners.forEach {
            it.sources.forEach { source ->
                when (source) {
                    is TextChannel -> {
                        if (event.channel.id == source.id) {
                            if (service.patternsMatch(it, event.message.contentRaw))
                                sendToDestinations(it.destinations, it.id, event.author, event.message.contentRaw)
                        }
                    }
                    is User -> {
                        if (event.author.id == source.id) {
                            if (service.patternsMatch(it, event.message.contentRaw))
                                sendToDestinations(it.destinations, it.id, event.author, event.message.contentRaw)
                        }
                    }
                }
            }
        }
    }

    fun sendToDestinations(destinations: List<Any>, listenerId: String, user: User, match: String) {
        destinations.forEach { destination ->
            when (destination) {
                is TextChannel -> {
                    destination.sendMessage(EmbedUtils.buildMessageDestinationEmbed(listenerId, user, match)).queue()
                }
                is User -> {
                    destination.sendPrivateMessage(EmbedUtils.buildMessageDestinationEmbed(listenerId, user, match))
                }
            }
        }
    }
}
