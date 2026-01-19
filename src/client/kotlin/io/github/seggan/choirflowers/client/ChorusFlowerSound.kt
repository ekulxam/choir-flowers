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

class ChorusFlowerSound(private val audio: ByteBuffer, pos: Vec3) : AbstractSoundInstance(
    Identifier.fromNamespaceAndPath(MOD_ID, "chorus_flower_sing"),
    SoundSource.BLOCKS,
    RandomSource.createNewThreadLocalInstance()
) {

    private var pos = 0

    init {
        x = pos.x
        y = pos.y
        z = pos.z
        volume = 0.2f
        looping = true
    }

    override fun getAudioStream(
        loader: SoundBufferLibrary,
        id: Identifier,
        repeatInstantly: Boolean
    ): CompletableFuture<AudioStream> {
        return CompletableFuture.completedFuture(Audio())
    }

    private inner class Audio : AudioStream {

        override fun getFormat(): AudioFormat = FORMAT

        override fun read(size: Int): ByteBuffer {
            val buf = BufferUtils.createByteBuffer(size)
            while (buf.hasRemaining()) {
                val toRead = (audio.limit() - pos).coerceAtMost(buf.remaining())
                buf.put(audio.array(), pos, toRead)
                pos += toRead
                if (pos >= audio.limit()) {
                    pos = 0
                }
            }
            buf.flip()
            return buf
        }

        override fun close() {
        }
    }
}