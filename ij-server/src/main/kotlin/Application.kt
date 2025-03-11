import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import services.BreakpointService
import java.time.Duration
import kotlin.time.toKotlinDuration

fun main() {
    val breakpointService = BreakpointService()

    embeddedServer(
        Netty,
        port = getEnvOrNull("PORT")?.toIntOrNull() ?: 8080,
        host = getEnvOrNull("HOST") ?: "localhost"
    ) {
        install(WebSockets) {
            pingPeriod = Duration.ofHours(1).toKotlinDuration()
        }

        routing {
            staticResources("/resources", "static")

            webSocket("/ws/breakpoints/update") {
                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val event = breakpointService.parseBreakpointEvent(text)
                            breakpointService.processBreakpointEvent(event)
                        }
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }

            webSocket("/ws/breakpoints/stream") {
                breakpointService.registerSession(this)
                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val event = breakpointService.parseBreakpointEvent(text)
                            breakpointService.processBreakpointEvent(event)
                        }
                    }
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                } finally {
                    breakpointService.unregisterSession(this)
                }
            }
        }
    }.start(wait = true)
}

fun getEnvOrNull(name: String): String? {
    try {
        return System.getenv(name)
    } catch (_: Exception) {
        println("Environment Variable $name is not found!")
    }
    return null
}
