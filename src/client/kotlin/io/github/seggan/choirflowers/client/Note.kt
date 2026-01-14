package io.github.seggan.choirflowers.client

import kotlin.math.pow

enum class Note(private val semitoneOffset: Int) {
    C(-9),
    C_SHARP(-8),
    D(-7),
    D_SHARP(-6),
    E(-5),
    F(-4),
    F_SHARP(-3),
    G(-2),
    G_SHARP(-1),
    A(0),
    A_SHARP(1),
    B(2);

    fun frequency(octave: Int): Double {
        val semitonesFromA4 = semitoneOffset + (octave - 4) * 12
        return 440.0 * 2.0.pow(semitonesFromA4 / 12.0)
    }
}