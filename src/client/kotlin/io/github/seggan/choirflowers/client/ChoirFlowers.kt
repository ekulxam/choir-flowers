@file:JvmName("ChoirFlowers")

package io.github.seggan.choirflowers.client

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3

private val C_PENTATONIC = listOf(Note.C, Note.D, Note.E, Note.G, Note.A)

private val singingChorusFlowers = mutableMapOf<ChunkPos, MutableMap<BlockPos, ChorusFlowerSound>>()
private val muted = mutableSetOf<BlockPos>()

private val soundManager = Minecraft.getInstance().soundManager

fun startSinging(pos: BlockPos) {
    val pos = pos.immutable()
    val flowersInChunk = singingChorusFlowers.getOrPut(ChunkPos(pos)) { mutableMapOf() }
    if (pos in flowersInChunk) return
    flowersInChunk[pos] = ChorusFlowerSound(C_PENTATONIC.random(), (2..5).random(), Vec3(pos))
}

fun stopSinging(pos: BlockPos) {
    val pos = pos.immutable()
    val chunkPos = ChunkPos(pos)
    val flowersInChunk = singingChorusFlowers[chunkPos] ?: return
    val sound = flowersInChunk.remove(pos) ?: return
    soundManager.stop(sound)
    muted.remove(pos)
    if (flowersInChunk.isEmpty()) {
        singingChorusFlowers.remove(chunkPos)
    }
}

fun stopSingingChunk(chunkPos: ChunkPos) {
    val flowersInChunk = singingChorusFlowers.remove(chunkPos).orEmpty()
    for ((pos, sound) in flowersInChunk) {
        soundManager.stop(sound)
        muted.remove(pos)
    }
}

private const val MAX_DISTANCE = 16

fun updateSound() {
    val playerPos = Minecraft.getInstance().player?.position() ?: return
    var singing = 0
    for (flowers in singingChorusFlowers.values) {
        for ((pos, sound) in flowers) {
            val isTooFar = playerPos.distanceToSqr(Vec3(pos)) > MAX_DISTANCE * MAX_DISTANCE
            if (pos in muted && !isTooFar) {
                muted.remove(pos)
            } else if (pos !in muted && isTooFar) {
                muted.add(pos)
            }
            if (pos in muted) {
                if (soundManager.isActive(sound)) {
                    soundManager.stop(sound)
                }
            } else {
                if (!soundManager.isActive(sound)) {
                    soundManager.play(sound)
                }
                singing++
            }
        }
    }
    //ChoirFlowersClient.LOGGER.info(singing.toString())
}