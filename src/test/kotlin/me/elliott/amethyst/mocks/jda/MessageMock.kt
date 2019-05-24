package me.elliott.amethyst.mocks.jda

import io.mockk.*
import net.dv8tion.jda.core.entities.Message

fun produceMessageMock() = mockk<Message>(relaxed = true) {
    every { author } returns produceUserMock()
    every { guild } returns guildMock
    every { id } returns "_MESSAGE_ID"
}
