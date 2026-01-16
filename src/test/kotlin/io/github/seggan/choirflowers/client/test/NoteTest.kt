package io.github.seggan.choirflowers.io.github.seggan.choirflowers.client.test

import io.github.seggan.choirflowers.client.note.Note
import io.github.seggan.choirflowers.client.note.Pitch
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NoteTest {

    @Test
    fun `test pitch shifting`() {
        val pitch = Pitch(Note.A, 4)
        val shiftedPitch = pitch.shift(4)
        assertEquals(Pitch(Note.C_SHARP, 5), shiftedPitch)
    }
}