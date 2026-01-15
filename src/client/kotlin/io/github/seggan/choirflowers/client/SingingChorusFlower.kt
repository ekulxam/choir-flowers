package io.github.seggan.choirflowers.client

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3
import java.io.Closeable

class SingingChorusFlower(private val pos: BlockPos) : Closeable {

    private val random = RandomSource.createNewThreadLocalInstance()

    private val sound = ChorusFlowerSound(C_PENTATONIC.random(), (3..5).random(), Vec3(pos))

    private var muted = false

    private fun tick() {
        val playerPos = minecraft.player?.position() ?: return
        val isTooFar = playerPos.distanceToSqr(Vec3(pos)) > MAX_DISTANCE * MAX_DISTANCE
        if (muted && !isTooFar) {
            muted = false
        } else if (!muted && isTooFar) {
            muted = true
        }
        if (muted) {
            if (soundManager.isActive(sound)) {
                soundManager.stop(sound)
            }
        } else {
            if (!soundManager.isActive(sound)) {
                soundManager.play(sound)
            }
        }
    }

    override fun close() {
        soundManager.stop(sound)
    }

    companion object {
        private val minecraft = Minecraft.getInstance()
        private val soundManager = minecraft.soundManager

        private const val MAX_DISTANCE = 16

        private val singingChorusFlowers = mutableMapOf<ChunkPos, MutableMap<BlockPos, SingingChorusFlower>>()

        @JvmStatic
        fun startSinging(pos: BlockPos) {
            val pos = pos.immutable()
            val flowersInChunk = singingChorusFlowers.getOrPut(ChunkPos(pos), ::mutableMapOf)
            if (pos in flowersInChunk) return
            flowersInChunk[pos] = SingingChorusFlower(pos)
        }

        @JvmStatic
        fun stopSinging(pos: BlockPos) {
            val pos = pos.immutable()
            val chunkPos = ChunkPos(pos)
            val flowersInChunk = singingChorusFlowers[chunkPos] ?: return
            flowersInChunk.remove(pos)?.close()
            if (flowersInChunk.isEmpty()) {
                singingChorusFlowers.remove(chunkPos)
            }
        }

        @JvmStatic
        fun unloadChunk(chunkPos: ChunkPos) {
            val flowersInChunk = singingChorusFlowers.remove(chunkPos).orEmpty()
            for (flower in flowersInChunk.values) {
                flower.close()
            }
        }

        @JvmStatic
        fun tickAll() {
            for (flowersInChunk in singingChorusFlowers.values) {
                for (flower in flowersInChunk.values) {
                    flower.tick()
                }
            }
        }
    }
}

private val C_PENTATONIC = listOf(Note.C, Note.D, Note.E, Note.G, Note.A)