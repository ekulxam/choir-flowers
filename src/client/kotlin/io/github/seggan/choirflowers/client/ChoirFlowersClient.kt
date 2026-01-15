package io.github.seggan.choirflowers.client

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.world.level.block.Blocks
import org.slf4j.LoggerFactory
import javax.sound.sampled.AudioFormat

class ChoirFlowersClient : ClientModInitializer {

    override fun onInitializeClient() {
        Note.A // Preload notes
        ClientChunkEvents.CHUNK_LOAD.register { _, chunk ->
            chunk.findBlocks({ it.`is`(Blocks.CHORUS_FLOWER) }) { pos, _ ->
                SingingChorusFlower.startSinging(pos)
            }
        }
        ClientChunkEvents.CHUNK_UNLOAD.register { _, chunk ->
            SingingChorusFlower.unloadChunk(chunk.pos)
        }
        ClientTickEvents.START_WORLD_TICK.register {
            SingingChorusFlower.tickAll()
        }
    }
}

const val MOD_ID = "choir-flowers"
internal val LOGGER = LoggerFactory.getLogger(MOD_ID)!!
val FORMAT = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 16, 1, 2, 44100f, false)