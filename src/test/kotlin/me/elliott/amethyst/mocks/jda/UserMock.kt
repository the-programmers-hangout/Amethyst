package me.elliott.amethyst.mocks.jda

import io.mockk.*
import net.dv8tion.jda.core.entities.User

fun produceUserMock() = mockk<User>(relaxed = true) {
    every { id } returns "_USER_ID"
}
