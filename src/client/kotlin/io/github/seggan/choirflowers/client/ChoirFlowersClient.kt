package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.minecraft.world.level.block.Blocks
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
        ClientChunkEvents.CHUNK_LOAD.register { _, chunk ->
            chunk.findBlocks({ it.`is`(Blocks.CHORUS_FLOWER) }) { pos, _ ->
                ChoirFlowersManager.startSinging(pos)
            }
        }
        ClientChunkEvents.CHUNK_UNLOAD.register { _, chunk ->
            ChoirFlowersManager.stopSingingChunk(chunk.pos)
        }
    }

    companion object {
        const val MOD_ID = "choir-flowers"

        @JvmField
        val LOGGER = LoggerFactory.getLogger(MOD_ID)!!
    }
}