package me.elliott.amethyst.data

import me.elliott.amethyst.services.ListenerState

class RegisteredListeners {
    companion object {
        var listeners = mutableListOf<ListenerState>()

        fun tryReturnListener(id: String): ListenerState? = listeners.firstOrNull { l -> l.id == id }
        fun registerListener(listener: ListenerState) {
            listeners.add(listener)
        }
    }
}