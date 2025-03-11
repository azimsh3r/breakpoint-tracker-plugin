package com.azimsh3r.communication

import com.azimsh3r.models.BreakpointEvent
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Service(Service.Level.PROJECT)
class DataTransferService : Disposable {
    private val client = HttpClient {
        install(WebSockets)
    }

    private var session : WebSocketSession? = null

    suspend fun setupWebSocketConnection() {
        this.session = client.webSocketSession (
            host = getEnvOrNull("HOST") ?: "localhost",
            port = getEnvOrNull("PORT")?.toIntOrNull() ?: 8080,
            path = "/ws/breakpoints/update"
        )
    }

    suspend fun sendBreakpointEvent(breakpointEvent: BreakpointEvent) {
        if (session != null) {
            session?.send(Json.encodeToString(breakpointEvent))
        } else {
            println("WebSocket not connected")
        }
    }

    override fun dispose() {
        client.close()
    }
}

fun getEnvOrNull(name: String): String? {
    try {
        return System.getenv(name)
    } catch (_: Exception) {
        println("Environment Variable $name is not found!")
    }
    return null
}
