package io.github.seggan.choirflowers.client.note

class Note private constructor(private val name: String, val semitonesFromC: Int) {

    fun shift(semitones: Int): Note {
        val newSemitones = (semitonesFromC + semitones) % 12
        return entries.first { it.semitonesFromC == newSemitones }
    }

    override fun toString() = name

    companion object {

        val C = Note("C", 0)
        val C_SHARP = Note("C#", 1)
        val D_FLAT = C_SHARP
        val D = Note("D", 2)
        val D_SHARP = Note("D#", 3)
        val E_FLAT = D_SHARP
        val E = Note("E", 4)
        val F = Note("F", 5)
        val F_SHARP = Note("F#", 6)
        val G_FLAT = F_SHARP
        val G = Note("G", 7)
        val G_SHARP = Note("G#", 8)
        val A_FLAT = G_SHARP
        val A = Note("A", 9)
        val A_SHARP = Note("A#", 10)
        val B_FLAT = A_SHARP
        val B = Note("B", 11)

        val entries = listOf(C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B)
    }
}