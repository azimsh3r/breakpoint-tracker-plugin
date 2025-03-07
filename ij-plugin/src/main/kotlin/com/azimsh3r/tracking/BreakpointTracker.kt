package com.azimsh3r.tracking

import com.azimsh3r.communication.DataTransferService
import com.azimsh3r.models.BreakpointEvent
import com.azimsh3r.models.BreakpointStatus
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import com.intellij.util.messages.Topic
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Service(Service.Level.PROJECT)
class BreakpointTracker(private val project: Project) : Disposable {
    private val messageBusConnections: MutableList<MessageBusConnection> = mutableListOf()

    private var breakpoints: MutableList<XBreakpoint<*>> = mutableListOf()

    private val dataTransferService = project.getService(DataTransferService::class.java)

    fun listenBreakpoints() {
        val actionListener = object : XBreakpointListener<XBreakpoint<*>> {
            override fun breakpointAdded(breakpoint: XBreakpoint<*>) {
                sendBreakpointEvent(
                    breakpoint,
                    BreakpointStatus.BREAKPOINT_ADDED
                )
                breakpoints.add(breakpoint)
            }

            override fun breakpointRemoved(breakpoint: XBreakpoint<*>) {
                sendBreakpointEvent(
                    breakpoint,
                    BreakpointStatus.BREAKPOINT_REMOVED
                )
                breakpoints.remove(breakpoint)
            }

            override fun breakpointChanged(breakpoint: XBreakpoint<*>) {
                sendBreakpointEvent(
                    breakpoint,
                    BreakpointStatus.BREAKPOINT_CHANGED
                )
            }
        }

        actionListener.connect(XBreakpointListener.TOPIC)
    }

    private fun sendBreakpointEvent(breakpoint: XBreakpoint<*>, breakpointStatus: BreakpointStatus) {
        val breakpointEvent = BreakpointEvent(
            time = breakpoint.timeStamp,
            path = breakpoint.sourcePosition?.file?.path,
            line = breakpoint.sourcePosition?.line ?: -1,
            status = breakpointStatus
        )

        CoroutineScope(Dispatchers.IO).launch {
            dataTransferService.sendBreakpointEvent(breakpointEvent)
        }
    }

    private fun <T : Any> T.connect(topic: Topic<T>) {
        val connection = project.messageBus.connect()
        messageBusConnections.add(connection)
        connection.subscribe(topic, this)
    }

    override fun dispose() {
        messageBusConnections.forEach { it.disconnect() }
        messageBusConnections.clear()
    }
}
