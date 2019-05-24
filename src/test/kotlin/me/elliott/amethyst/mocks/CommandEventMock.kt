package me.elliott.amethyst.mocks

import me.aberrantfox.kjdautils.api.dsl.CommandEvent
import io.mockk.*
import me.elliott.amethyst.mocks.jda.jdaMock
import me.elliott.amethyst.mocks.jda.produceMessageMock

fun makeCommandEventMock(vararg _args: Any) = mockk<CommandEvent>(relaxed = true) {
    every { args } returns _args.toList()
    every { jda } returns jdaMock
    every { message } returns produceMessageMock()
}

