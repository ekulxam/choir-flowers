package io.github.seggan.choirflowers.client

import io.github.seggan.choirflowers.client.debug.DebugEntryCurrentNote
import io.github.seggan.choirflowers.client.note.Note
import io.github.seggan.choirflowers.client.note.Pitch
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.gui.components.debug.DebugScreenEntries
import net.minecraft.world.level.block.Blocks
import org.slf4j.LoggerFactory
import javax.sound.sampled.AudioFormat

class ChoirFlowersClient : ClientModInitializer {

    override fun onInitializeClient() {
        for (note in Note.entries) {
            for (octave in 2..5) {
                Pitch(note, octave).audio
            }
        }
        ClientChunkEvents.CHUNK_LOAD.register { _, chunk ->
            chunk.findBlocks({ it.`is`(Blocks.CHORUS_FLOWER) }) { pos, _ ->
                SingingChorusFlower.startSinging(pos)
            }
        }
        ClientChunkEvents.CHUNK_UNLOAD.register { _, chunk ->
            SingingChorusFlower.unloadChunk(chunk.pos)
        }
        ClientTickEvents.START_WORLD_TICK.register {
            //LOGGER.info(measureTime {
                SingingChorusFlower.tickAll()
            //}.toString())
        }

        DebugScreenEntries.register(DebugEntryCurrentNote.ID, DebugEntryCurrentNote)
    }
}

const val MOD_ID = "choir-flowers"
internal val LOGGER = LoggerFactory.getLogger(MOD_ID)!!
val FORMAT = AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100f, 16, 1, 2, 44100f, false)