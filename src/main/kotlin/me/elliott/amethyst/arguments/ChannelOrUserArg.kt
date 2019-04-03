package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.aberrantfox.kjdautils.internal.command.tryRetrieveSnowflake
import net.dv8tion.jda.core.JDA

open class ChannelOrUserArg(override val name: String = "ChannelOrUserArg") : ArgumentType {
    companion object : ChannelOrUserArg()

    override val examples = arrayListOf("308762287819126784")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        return getChannelOrUser(event.jda, arg)
    }
}

fun getChannelOrUser(jda: JDA, arg: String): ArgumentResult {
    var retrieved = tryRetrieveSnowflake(jda) { it.getTextChannelById(arg.trimToID()) }


    if (retrieved == null)
        retrieved = tryRetrieveSnowflake(jda) { it.retrieveUserById(arg.trimToID()).complete() }

    return if (retrieved != null)
        ArgumentResult.Single(retrieved)
    else
        ArgumentResult.Error("Couldn't find a channel or user with the id you provided: $arg")
}

