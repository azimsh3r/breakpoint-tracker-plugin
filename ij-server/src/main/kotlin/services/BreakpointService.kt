package services

import dto.BreakpointEventDTO
import dto.BreakpointStatus
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import models.Breakpoint
import java.util.concurrent.CopyOnWriteArraySet

class BreakpointService {
    private val breakpoints : MutableList<Breakpoint> = mutableListOf()
    private val sessions = CopyOnWriteArraySet<WebSocketSession>()

    fun parseBreakpointEvent(json: String) = Json.decodeFromString<BreakpointEventDTO>(json)

    suspend fun processBreakpointEvent(breakpointEvent: BreakpointEventDTO) {
        when (breakpointEvent.status) {
            BreakpointStatus.BREAKPOINT_ADDED -> {
                val breakpoint = convertFromDTOToModel(breakpointEvent)
                if (!breakpoints.contains(breakpoint)) {
                    breakpoints.add(breakpoint)
                }
            }

            BreakpointStatus.BREAKPOINT_REMOVED -> {
                val breakpoint = convertFromDTOToModel(breakpointEvent)
                breakpoints.removeIf { it.path == breakpoint.path && it.line == breakpoint.line }
            }

            BreakpointStatus.BREAKPOINT_REACHED -> {
                //TODO: logic for reached breakpoint
            }

            BreakpointStatus.BREAKPOINT_CHANGED -> {
                //TODO: logic for changed breakpoint
            }
        }

        sendUpdatedBreakpoints()
    }

    fun registerSession(session: WebSocketSession) {
        sessions.add(session)
    }

    fun unregisterSession(session: WebSocketSession) {
        sessions.remove(session)
    }

    private suspend fun sendUpdatedBreakpoints() {
        val jsonData = Json.encodeToString(breakpoints)
        sessions.forEach { session ->
            session.send(jsonData)
        }
    }
}

fun convertFromDTOToModel(breakpointEvent: BreakpointEventDTO) : Breakpoint {
    val breakpoint = Breakpoint()
    breakpoint.path = breakpointEvent.path
    breakpoint.line = breakpointEvent.line
    return breakpoint
}
