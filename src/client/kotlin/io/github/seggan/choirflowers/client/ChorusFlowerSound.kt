package io.github.seggan.choirflowers.client

import io.github.seggan.choirflowers.client.note.Pitch
import net.minecraft.client.resources.sounds.AbstractSoundInstance
import net.minecraft.client.sounds.AudioStream
import net.minecraft.client.sounds.SoundBufferLibrary
import net.minecraft.resources.Identifier
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import org.lwjgl.BufferUtils
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import javax.sound.sampled.AudioFormat
import kotlin.math.PI
import kotlin.math.sin

class ChorusFlowerSound(pitch: Pitch, pos: Vec3) : AbstractSoundInstance(
    Identifier.fromNamespaceAndPath(MOD_ID, "chorus_flower_sing"),
    SoundSource.BLOCKS,
    RandomSource.createNewThreadLocalInstance()
) {

    private val audio = pitch.audio
    private var pos = 0

    init {
        x = pos.x
        y = pos.y
        z = pos.z
        volume = 0.1f
    }

    fun setVolume(vol: Float) {
        volume = vol
    }

    override fun getAudioStream(
        loader: SoundBufferLibrary,
        id: Identifier,
        repeatInstantly: Boolean
    ): CompletableFuture<AudioStream> {
        return CompletableFuture.completedFuture(Audio())
    }

    private val randomFrequency = random.nextDouble()
    private val randomPhase = random.nextDouble() * PI
    private val randomAmplitude = random.nextDouble()

    private inner class Audio : AudioStream {

        override fun getFormat(): AudioFormat = FORMAT

        override fun read(size: Int): ByteBuffer {
            val buf = BufferUtils.createByteBuffer(size)
            while (buf.hasRemaining()) {
                val sample = audio[pos % audio.size]
                val preAmp = (randomAmplitude * sin(pos / FORMAT.sampleRate * randomFrequency + randomPhase).toFloat() + 1f) / 2f * 0.5f + 0.5f
                buf.putShort((sample * preAmp).toInt().toShort())
                pos++
            }
            buf.flip()
            return buf
        }

        override fun close() {
        }
    }
}