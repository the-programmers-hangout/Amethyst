package me.elliott.amethyst.mocks.jda

import io.mockk.every
import io.mockk.mockk
import me.elliott.amethyst.util.TestConstants
import net.dv8tion.jda.core.entities.Guild

val guildMock = mockk<Guild>(relaxed = true) {
    every { id } returns "insert-id"
    every { iconUrl } returns TestConstants.DUMMY_URL
}


