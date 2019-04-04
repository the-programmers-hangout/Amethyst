package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.elliott.amethyst.util.case

open class YesNoArg(override val name: String = "YesNoArg") : ArgumentType {
    companion object : YesNoArg()

    override val examples = arrayListOf("yes", "y", "no", "n")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        var result = ArgumentResult.Single(false)
        case(arg.toLowerCase()) {
            eq("yes") - { result = ArgumentResult.Single(true) }
            eq("y") - { result = ArgumentResult.Single(true) }
        }
        return result
    }
}
