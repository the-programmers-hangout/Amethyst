package me.elliott.amethyst.data

import me.elliott.amethyst.services.ListenerState

class RegisteredListeners {
    companion object {
        private var listeners = mutableListOf<ListenerState>()

        fun getListener(id: String): ListenerState? = listeners.firstOrNull { l -> l.id == id }
        fun registerListener(listener: ListenerState) {
            listeners.add(listener)
        }
    }
}