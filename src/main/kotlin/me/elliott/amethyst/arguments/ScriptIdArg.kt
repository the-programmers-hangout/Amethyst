package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.elliott.amethyst.data.RegisteredScripts

open class ScriptIdArg(override val name: String = "ScriptIdArg") : ArgumentType {
    companion object : ScriptIdArg()

    override val examples = arrayListOf("2afd4rf")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {
        val retrieved = RegisteredScripts.tryReturnScript(arg)

        return if (retrieved != null) {
            ArgumentResult.Single(retrieved)
        } else {
            ArgumentResult.Error("Couldn't find a script with the ID you provided: $arg")
        }
    }
}

