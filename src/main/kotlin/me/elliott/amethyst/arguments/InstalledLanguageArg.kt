package me.elliott.amethyst.arguments

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import me.aberrantfox.kjdautils.internal.command.ArgumentResult
import me.aberrantfox.kjdautils.internal.command.ArgumentType
import me.aberrantfox.kjdautils.internal.command.ConsumptionType
import me.elliott.amethyst.data.RegisteredScripts
import me.elliott.amethyst.services.ScriptEngineService
import org.graalvm.polyglot.Context

open class InstalledLanguageArg(override val name: String = "InstalledLanguageArg") : ArgumentType {
    companion object : InstalledLanguageArg()

    override val examples = arrayListOf("js", "ruby", "python")
    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent): ArgumentResult {

        val installedLanguages = Context.newBuilder().allowAllAccess(true).build().engine.languages

        return if (installedLanguages.contains(arg.toLowerCase())) {
            ArgumentResult.Single(arg)
        } else {
            ArgumentResult.Error("Couldn't find an installed language with the value you " +
                    "provided: $arg - installed languages are: " +
                    "**${installedLanguages.entries.map { l -> l.key }}**")

        }
    }
}

