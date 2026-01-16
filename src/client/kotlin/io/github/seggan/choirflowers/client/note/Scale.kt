package io.github.seggan.choirflowers.client.note

data class Scale(val notes: List<Note>) {

    constructor(vararg notes: Note) : this(notes.toList())

    fun getNote(degree: Int) = notes[(degree - 1) % notes.size]

    companion object {

        val C_MAJOR = Scale(Note.C, Note.D, Note.E, Note.F, Note.G, Note.A, Note.B)
        val G_MAJOR = Scale(Note.G, Note.A, Note.B, Note.C, Note.D, Note.E, Note.F_SHARP)
        val D_MAJOR = Scale(Note.D, Note.E, Note.F_SHARP, Note.G, Note.A, Note.B, Note.C_SHARP)
        val A_MAJOR = Scale(Note.A, Note.B, Note.C_SHARP, Note.D, Note.E, Note.F_SHARP, Note.G_SHARP)
        val E_MAJOR = Scale(Note.E, Note.F_SHARP, Note.G_SHARP, Note.A, Note.B, Note.C_SHARP, Note.D_SHARP)
        val B_MAJOR = Scale(Note.B, Note.C_SHARP, Note.D_SHARP, Note.E, Note.F_SHARP, Note.G_SHARP, Note.A_SHARP)
        val F_SHARP_MAJOR = Scale(Note.F_SHARP, Note.G_SHARP, Note.A_SHARP, Note.B, Note.C_SHARP, Note.D_SHARP, Note.F)
        val D_FLAT_MAJOR = Scale(Note.D_FLAT, Note.E_FLAT, Note.F, Note.G_FLAT, Note.A_FLAT, Note.B_FLAT, Note.C)
        val A_FLAT_MAJOR = Scale(Note.A_FLAT, Note.B_FLAT, Note.C, Note.D_FLAT, Note.E_FLAT, Note.F, Note.G)
        val E_FLAT_MAJOR = Scale(Note.E_FLAT, Note.F, Note.G, Note.A_FLAT, Note.B_FLAT, Note.C, Note.D)
        val B_FLAT_MAJOR = Scale(Note.B_FLAT, Note.C, Note.D, Note.E_FLAT, Note.F, Note.G, Note.A)
        val F_MAJOR = Scale(Note.F, Note.G, Note.A, Note.B_FLAT, Note.C, Note.D, Note.E)

        val MAJOR_SCALES = setOf(
            C_MAJOR,
            G_MAJOR,
            D_MAJOR,
            A_MAJOR,
            E_MAJOR,
            B_MAJOR,
            F_SHARP_MAJOR,
            D_FLAT_MAJOR,
            A_FLAT_MAJOR,
            E_FLAT_MAJOR,
            B_FLAT_MAJOR,
            F_MAJOR
        )

        fun majorScaleFromTonic(tonic: Note): Scale {
            return when (tonic) {
                Note.C -> C_MAJOR
                Note.G -> G_MAJOR
                Note.D -> D_MAJOR
                Note.A -> A_MAJOR
                Note.E -> E_MAJOR
                Note.B -> B_MAJOR
                Note.F_SHARP -> F_SHARP_MAJOR
                Note.D_FLAT -> D_FLAT_MAJOR
                Note.A_FLAT -> A_FLAT_MAJOR
                Note.E_FLAT -> E_FLAT_MAJOR
                Note.B_FLAT -> B_FLAT_MAJOR
                Note.F -> F_MAJOR
                else -> throw AssertionError("Unreachable")
            }
        }
    }
}
