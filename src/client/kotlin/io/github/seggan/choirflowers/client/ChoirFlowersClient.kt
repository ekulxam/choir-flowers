package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import org.slf4j.LoggerFactory

class ChoirFlowersClient : ClientModInitializer {
    override fun onInitializeClient() {
        Note.A
    }

    companion object {
        const val MOD_ID = "choir-flowers"
        val LOGGER = LoggerFactory.getLogger(MOD_ID)
    }
}