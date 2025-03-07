package models

import kotlinx.serialization.Serializable

@Serializable
data class Breakpoint(
    var path: String? = null,

    var line: Int = -1,
)
