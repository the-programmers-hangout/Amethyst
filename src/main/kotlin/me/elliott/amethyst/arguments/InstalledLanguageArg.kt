package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import org.graalvm.polyglot.Context

open class InstalledLanguageArg(override val name: String = "InstalledLanguageArg") : ArgumentType {
    companion object : InstalledLanguageArg()

    override val examples = arrayListOf("js", "ruby", "python")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {

        val context = Context.newBuilder().allowAllAccess(true).build()
        val installedLanguages = context.engine.languages

        return if (installedLanguages.contains(arg.trim().toLowerCase())) {
            ArgumentResult.Single(arg)

        } else {
            ArgumentResult.Error("Couldn't find an installed language with the value you " +
                    "provided: $arg - installed languages are: " +
                    "**${installedLanguages.entries.map { l -> l.key }}**")

        }
    }
}

