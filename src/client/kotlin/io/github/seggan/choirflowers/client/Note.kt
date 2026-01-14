package io.github.seggan.choirflowers.client

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.sounds.SoundEvent

enum class Note {
    C,
    C_SHARP,
    D,
    D_SHARP,
    E,
    F,
    F_SHARP,
    G,
    G_SHARP,
    A,
    A_SHARP,
    B;

    val noteName = name.lowercase().replace("_sharp", "#")

    private val octaveInstances = Int2ObjectOpenHashMap<SoundEvent>()

    init {
        for (octave in 2..5) {
            val id = Identifier
        }
    }
}