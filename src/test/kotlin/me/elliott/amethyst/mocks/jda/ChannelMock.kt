package me.elliott.amethyst.mocks.jda

import io.mockk.*
import me.elliott.amethyst.util.TestConstants
import net.dv8tion.jda.core.entities.*

fun produceTextChannelMock(_guild: Guild) = mockk<TextChannel>(relaxed = true) {
    every { guild } returns _guild
    every { id } returns TestConstants.CHANNEL_ID
}

fun produceVoiceChannelMock(_guild: Guild) = mockk<VoiceChannel>(relaxed = true) {
    every { guild } returns _guild
    every { id } returns TestConstants.CHANNEL_ID
}
