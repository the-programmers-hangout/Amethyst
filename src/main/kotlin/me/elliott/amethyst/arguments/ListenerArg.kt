package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.elliott.amethyst.services.ListenerService

open class ListenerArg(override val name : String = "ListenerArg") : ArgumentType {
    companion object : ListenerArg()

    override val examples = arrayListOf("9acd4rfv")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val retrieved =  .getTextChannelById(arg.trimToID()) }

        return if (retrieved != null) {
            ArgumentResult.Single(retrieved)
        } else {
            ArgumentResult.Error("Couldn't retrieve text channel: $arg")
        }
    }
}

}
