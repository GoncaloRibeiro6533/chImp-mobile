package pt.isel.chimp.domain

enum class Role( val value: String) {
    READ_ONLY("READ_ONLY"),
    READ_WRITE("READ_WRITE"),
}