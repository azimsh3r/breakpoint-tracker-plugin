package com.azimsh3r.models

import kotlinx.serialization.Serializable


@Serializable
data class BreakpointEvent(
    val time: Long,
    val status: BreakpointStatus,

    /**
     * Path of the file in which the breakpoint is located.
     */
    val path: String? = null,

    /**
     * Line number in which the breakpoint is located.
     * If unknown, it should be -1.
     */
    val line: Int = -1,
)

enum class BreakpointStatus {
    /**
     * A new breakpoint is added.
     */
    BREAKPOINT_ADDED,

    /**
     * The breakpoint is removed.
     */
    BREAKPOINT_REMOVED,

    /**
     * The breakpoint is changed.
     */
    BREAKPOINT_CHANGED,

    /**
     * The breakpoint is reached (execution paused).
     */
    BREAKPOINT_REACHED,
}
