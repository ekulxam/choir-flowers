package io.github.seggan.choirflowers.client.note

enum class Note(val semitonesFromA: Int) {
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

    override fun toString() = name.replace("_SHARP", "#")
}