package io.github.seggan.choirflowers.client

import io.github.seggan.choirflowers.client.note.Note
import io.github.seggan.choirflowers.client.note.Pitch
import io.github.seggan.choirflowers.client.note.Scale
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3
import java.io.Closeable
import kotlin.streams.asSequence

class SingingChorusFlower(pos: BlockPos) : Closeable {

    private val playingPos = pos.center

    val scale: Scale
    val pitch: Pitch

    init {
        val nearbyFlowers = ChunkPos.rangeClosed(ChunkPos(pos), 1).asSequence()
            .flatMap { singingChorusFlowers[it].orEmpty().values.asSequence() }
            .filter { canBeHeardAt(it.playingPos) }
            .groupBy { it.pitch.note }
            .mapValues { (_, notes) -> notes.map { it.playingPos.distanceTo(playingPos) }.average() }
        if (nearbyFlowers.isEmpty()) {
            scale = Scale.MAJOR_SCALES.random()
            pitch = Pitch(scale.notes.random(), (3..5).random())
        } else {
            val weights = WeightedSet<Scale>()
            for (scale in Scale.MAJOR_SCALES) {
                val commonNotes = scale.notes.mapNotNull { nearbyFlowers[it] }
                val weight = commonNotes.sumOf { (MAX_DISTANCE - it).coerceAtLeast(0.0) } * commonNotes.size
                weights.add(WeightedSet.Element(scale, (weight * weight).toFloat()))
            }
            scale = weights.getRandom()

            val noteWeights = WeightedSet<Note>()
            noteWeights.add(scale.getNote(1), 10f)
            noteWeights.add(scale.getNote(2), 0.5f)
            noteWeights.add(scale.getNote(3), 10f)
            noteWeights.add(scale.getNote(4), 3f)
            noteWeights.add(scale.getNote(5), 10f)
            noteWeights.add(scale.getNote(6), 3f)
            noteWeights.add(scale.getNote(7), 0.5f)
            val note = noteWeights.getRandom()

            pitch = Pitch(note, (3..5).random())
        }
    }

    private val sound = pitch.makeSoundAt(playingPos)

    private var muted = true

    private fun tick() {
        val playerPos = minecraft.player?.position() ?: return
        val isTooFar = !canBeHeardAt(playerPos)
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

    fun canBeHeardAt(pos: Vec3) = pos.distanceToSqr(playingPos) <= MAX_DISTANCE * MAX_DISTANCE

    override fun close() {
        if (!muted) {
            soundManager.stop(sound)
        }
    }

    companion object {
        private const val MAX_DISTANCE = 16

        private val minecraft = Minecraft.getInstance()
        private val soundManager = minecraft.soundManager

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

        fun unloadChunk(chunkPos: ChunkPos) {
            val flowersInChunk = singingChorusFlowers.remove(chunkPos).orEmpty()
            for (flower in flowersInChunk.values) {
                flower.close()
            }
        }

        fun tickAll() {
            for (flowersInChunk in singingChorusFlowers.values) {
                for (flower in flowersInChunk.values) {
                    flower.tick()
                }
            }
        }

        fun getInstance(pos: BlockPos): SingingChorusFlower? {
            val flowersInChunk = singingChorusFlowers[ChunkPos(pos)] ?: return null
            return flowersInChunk[pos]
        }
    }
}