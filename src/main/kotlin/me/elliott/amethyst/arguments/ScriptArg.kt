package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.elliott.amethyst.services.ExecutionResult
import me.elliott.amethyst.services.ScriptEngineService

open class ScriptArg(override val name: String = "ScriptArg", val language: String = "js",
                     private val watch: Boolean = false) : ArgumentType {

    companion object : ScriptArg()

    override val examples = arrayListOf("`Provide code for execution.`")
    override val consumptionType = ConsumptionType.All

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {

        val returned = ScriptEngineService().exec(language, args.joinToString(" "), event)

        return if (returned is ExecutionResult.Success) {
            ArgumentResult.Single(true)
        } else {
            val error = returned as ExecutionResult.Error
            ArgumentResult.Error(error.message)
        }
    }
}

