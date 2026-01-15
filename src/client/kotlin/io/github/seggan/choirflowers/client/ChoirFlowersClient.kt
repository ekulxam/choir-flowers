package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.world.level.block.Blocks
import org.slf4j.LoggerFactory

class ChoirFlowersClient : ClientModInitializer {

    override fun onInitializeClient() {
        Note.A // Preload notes
        ClientChunkEvents.CHUNK_LOAD.register { _, chunk ->
            chunk.findBlocks({ it.`is`(Blocks.CHORUS_FLOWER) }) { pos, _ ->
                startSinging(pos)
            }
        }
        ClientChunkEvents.CHUNK_UNLOAD.register { _, chunk ->
            stopSingingChunk(chunk.pos)
        }
        ClientTickEvents.START_WORLD_TICK.register {
            //LOGGER.info(measureTime { updateSound() }.toString())
            updateSound()
        }
    }

    companion object {
        const val MOD_ID = "choir-flowers"

        @JvmField
        val LOGGER = LoggerFactory.getLogger(MOD_ID)!!
    }
}