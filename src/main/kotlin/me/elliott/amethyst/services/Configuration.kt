package me.elliott.amethyst.services

import me.aberrantfox.kjdautils.api.annotation.Data


data class GuildConfiguration(var guildId: String = "insert-id",
                              var staffRoleName: String = "Staff")

@Data("config/config.json")
data class Configuration(val prefix: String = "--",
                         var guildConfigurations: MutableList<GuildConfiguration> = mutableListOf(GuildConfiguration())) {
    fun hasGuildConfig(guildId: String) = getGuildConfig(guildId) != null
    fun getGuildConfig(guildId: String) = guildConfigurations.firstOrNull { it.guildId == guildId }
}
