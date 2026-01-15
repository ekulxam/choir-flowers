package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import org.slf4j.LoggerFactory

class ChoirFlowersClient : ClientModInitializer {
    override fun onInitializeClient() {
        Note.A // Preload notes
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommandManager.literal("a4")
                    .executes { o ->
                        val client = o.source.client
                        client.soundManager.play(ChorusFlowerSound(Note.A, 4, client.player!!.position()))
                        0
                    }
            )
        }
    }

    companion object {
        const val MOD_ID = "choir-flowers"
        val LOGGER = LoggerFactory.getLogger(MOD_ID)!!
    }
}