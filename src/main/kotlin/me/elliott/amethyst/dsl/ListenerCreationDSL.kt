package me.elliott.amethyst.dsl

import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.entities.User


data class Source(val channel: TextChannel, val user: User, val says: String, val all: Boolean)
data class Destination(val channel: TextChannel, val user: User)

