package io.github.seggan.choirflowers.client

import io.github.seggan.choirflowers.client.ChoirFlowersClient.Companion.LOGGER
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3

object ChoirFlowersManager {

    private val C_PENTATONIC = listOf(Note.C, Note.D, Note.E, Note.G, Note.A)

    private val singingChorusFlowers = mutableMapOf<ChunkPos, MutableMap<BlockPos, ChorusFlowerSound>>()

    @JvmStatic
    fun startSinging(pos: BlockPos) {
        val flowersInChunk = singingChorusFlowers.getOrPut(ChunkPos(pos)) { mutableMapOf() }
        if (pos in flowersInChunk) {
            stopSinging1(pos, flowersInChunk[pos]!!)
        }

        val note = C_PENTATONIC.random()
        val sound = ChorusFlowerSound(note, 4, Vec3(pos))
        flowersInChunk[pos] = sound
        LOGGER.info("Chorus flower at $pos started singing note $note")
        Minecraft.getInstance().soundManager.play(sound)
    }

    @JvmStatic
    fun stopSinging(pos: BlockPos) {
        val chunkPos = ChunkPos(pos)
        val flowersInChunk = singingChorusFlowers[chunkPos] ?: return
        val sound = flowersInChunk.remove(pos) ?: return
        stopSinging1(pos, sound)
        if (flowersInChunk.isEmpty()) {
            singingChorusFlowers.remove(chunkPos)
        }
    }

    fun stopSingingChunk(chunkPos: ChunkPos) {
        val flowersInChunk = singingChorusFlowers.remove(chunkPos).orEmpty()
        for ((pos, sound) in flowersInChunk) {
            stopSinging1(pos, sound)
        }
    }

    private fun stopSinging1(pos: BlockPos, sound: ChorusFlowerSound) {
        LOGGER.info("Chorus flower at $pos stopped singing")
        Minecraft.getInstance().soundManager.stop(sound)
    }
}