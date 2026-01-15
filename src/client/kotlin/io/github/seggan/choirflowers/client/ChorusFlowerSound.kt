package io.github.seggan.choirflowers.client

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

class ChorusFlowerSound(note: Note, octave: Int, pos: Vec3) : AbstractSoundInstance(
    Identifier.fromNamespaceAndPath(MOD_ID, "chorus_flower_sing"),
    SoundSource.BLOCKS,
    RandomSource.createNewThreadLocalInstance()
) {

    private val format: AudioFormat
    private val audio: ShortArray
    private var pos = 0

    init {
        val (fmt, buf) = note.getAudioForOctave(octave)
        format = fmt
        audio = buf
        x = pos.x
        y = pos.y
        z = pos.z
    }

    fun setVolume(volume: Float) {
        this.volume = volume
    }

    override fun getVolume(): Float {
        LOGGER.info("getVolume: $volume")
        return super.getVolume()
    }

    override fun getAudioStream(loader: SoundBufferLibrary, id: Identifier, repeatInstantly: Boolean): CompletableFuture<AudioStream> {
        return CompletableFuture.completedFuture(Audio())
    }

    private inner class Audio : AudioStream {

        override fun getFormat(): AudioFormat = this@ChorusFlowerSound.format

        override fun read(size: Int): ByteBuffer {
            val buf = BufferUtils.createByteBuffer(size)
            while (buf.hasRemaining()) {
                buf.putShort(audio[pos])
                pos = (pos + 1) % audio.size
            }
            buf.flip()
            return buf
        }

        override fun close() {
        }
    }
}