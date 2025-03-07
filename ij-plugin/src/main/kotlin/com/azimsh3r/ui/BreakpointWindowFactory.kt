package com.azimsh3r.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.jcef.JBCefBrowser

class BreakpointWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val browser = JBCefBrowser(getTemplateUrl())
        val content = toolWindow.contentManager.factory.createContent(browser.component, "", false)
        toolWindow.contentManager.addContent(content)
    }
}

fun getTemplateUrl() : String {
    val port = getEnvOrNull("PORT") ?: "8080"
    val host = getEnvOrNull("HOST") ?: "localhost"
    return "http://$host:$port/resources/index.html"
}

fun getEnvOrNull(name: String): String? {
    try {
        return System.getenv(name)
    } catch (_: Exception) {
        println("Environment Variable $name is not found!")
    }
    return null
}
