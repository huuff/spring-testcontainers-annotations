package xyz.haff.testcontainers.util

import org.testcontainers.containers.GenericContainer

// TODO: Is "Factory" a good name? It acts more like a registry that stores containers that exist (if persistent) or
// creates new ones
class ContainerFactory<T : GenericContainer<*>>(
    private val createContainer: (tag: String) -> T,
) {
    private val persistentContainers: MutableMap<String, T> = mutableMapOf()

    fun get(tag: String, persistent: Boolean): T = if (persistent) {
        persistentContainers.getOrPut(tag) {
            createContainer(tag)
        }
    } else {
        createContainer(tag)
    }
}