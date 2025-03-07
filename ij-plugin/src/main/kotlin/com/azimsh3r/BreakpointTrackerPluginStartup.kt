package com.azimsh3r

import com.azimsh3r.communication.DataTransferService
import com.azimsh3r.tracking.BreakpointTracker
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class BreakpointTrackerStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val dataTransferService = project.getService(DataTransferService::class.java)
        dataTransferService.setupWebSocketConnection()

        val breakpointTracker = project.getService(BreakpointTracker::class.java)
        breakpointTracker.listenBreakpoints()
    }
}
