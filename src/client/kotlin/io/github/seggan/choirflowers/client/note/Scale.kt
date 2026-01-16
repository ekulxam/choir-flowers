package io.github.seggan.choirflowers.client.note

data class Scale(private val name: String, val notes: List<Note>) {

    constructor(name: String, vararg notes: Note) : this(name, notes.toList())

    fun getNote(degree: Int) = notes[(degree - 1) % notes.size]

    override fun toString() = name

    companion object {

        val C_MAJOR = Scale("C major", Note.C, Note.D, Note.E, Note.F, Note.G, Note.A, Note.B)
        val G_MAJOR = Scale("G major", Note.G, Note.A, Note.B, Note.C, Note.D, Note.E, Note.F_SHARP)
        val D_MAJOR = Scale("D major", Note.D, Note.E, Note.F_SHARP, Note.G, Note.A, Note.B, Note.C_SHARP)
        val A_MAJOR = Scale("A major", Note.A, Note.B, Note.C_SHARP, Note.D, Note.E, Note.F_SHARP, Note.G_SHARP)
        val E_MAJOR = Scale("E major", Note.E, Note.F_SHARP, Note.G_SHARP, Note.A, Note.B, Note.C_SHARP, Note.D_SHARP)
        val B_MAJOR = Scale("B major", Note.B, Note.C_SHARP, Note.D_SHARP, Note.E, Note.F_SHARP, Note.G_SHARP, Note.A_SHARP)
        val F_SHARP_MAJOR = Scale("F# major", Note.F_SHARP, Note.G_SHARP, Note.A_SHARP, Note.B, Note.C_SHARP, Note.D_SHARP, Note.F)
        val D_FLAT_MAJOR = Scale("Db major", Note.D_FLAT, Note.E_FLAT, Note.F, Note.G_FLAT, Note.A_FLAT, Note.B_FLAT, Note.C)
        val A_FLAT_MAJOR = Scale("Ab major", Note.A_FLAT, Note.B_FLAT, Note.C, Note.D_FLAT, Note.E_FLAT, Note.F, Note.G)
        val E_FLAT_MAJOR = Scale("Eb major", Note.E_FLAT, Note.F, Note.G, Note.A_FLAT, Note.B_FLAT, Note.C, Note.D)
        val B_FLAT_MAJOR = Scale("Bb major", Note.B_FLAT, Note.C, Note.D, Note.E_FLAT, Note.F, Note.G, Note.A)
        val F_MAJOR = Scale("F major", Note.F, Note.G, Note.A, Note.B_FLAT, Note.C, Note.D, Note.E)

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
