package io.github.seggan.choirflowers.client.note

import io.github.seggan.choirflowers.client.ChorusFlowerSound
import io.github.seggan.choirflowers.client.FORMAT
import net.minecraft.world.phys.Vec3
import java.nio.ByteBuffer
import javax.sound.sampled.AudioSystem

data class Pitch(val note: Note, val octave: Int) {

    val midiValue = (octave - 4) * 12 + 60 + note.semitonesFromC

    val audio: ByteBuffer
        get() = audios.getOrPut(this) {
            val audioInputStream = AudioSystem.getAudioInputStream(Note::class.java.getResource("/notes/choir_$midiValue.wav"))
            val format = audioInputStream.format
            if (!format.matches(FORMAT)) {
                error("Unexpected audio format for note $this$octave (MIDI $midiValue): expected $FORMAT, got $format")
            }
            ByteBuffer.wrap(audioInputStream.readAllBytes())
        }

    init {
        require(octave in 2..5) { "Octave must be between 2 and 5, got $octave" }
    }

    fun shift(semitones: Int): Pitch {
        val newNote = note.shift(semitones)
        val octaveShift = (note.semitonesFromC + semitones).floorDiv(12)
        return Pitch(newNote, octave + octaveShift)
    }

    fun makeSoundAt(pos: Vec3) = ChorusFlowerSound(audio, pos)

    override fun toString() = note.toString() + octave

    companion object {
        private val audios = mutableMapOf<Pitch, ByteBuffer>()
    }
}
