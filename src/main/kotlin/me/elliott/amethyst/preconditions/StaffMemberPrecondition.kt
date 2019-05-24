package me.elliott.amethyst.preconditions

import me.aberrantfox.kjdautils.api.dsl.*
import me.aberrantfox.kjdautils.internal.command.*
import me.elliott.amethyst.services.Configuration
import net.dv8tion.jda.core.entities.TextChannel

@Precondition
fun produceIsStaffMemberPrecondition(configuration: Configuration) = exit@{ event: CommandEvent ->
    if (event.channel !is TextChannel) return@exit Fail("This command must be executed from a TextChannel")

    val guild = (event.channel as TextChannel).guild
    val guildConfig = configuration.getGuildConfig(guild.id) ?: return@exit Pass
    val staffRole = guild.getRolesByName(guildConfig.staffRoleName, true).first()

    if (staffRole !in event.message.member.roles) return@exit Fail("Sorry, you don't have the appropriate role")

    return@exit Pass
}
