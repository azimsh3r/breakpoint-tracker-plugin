![Build](https://github.com/azimsh3r/breakpoint-tracker-ide/workflows/Build/badge.svg)

# Breakpoint Tracking IntelliJ Plugin

This IntelliJ plugin tracks breakpoints set within your project, capturing real-time information about each breakpoint, such as the file and line number. The plugin communicates with a dedicated Ktor server via WebSockets to display this data dynamically in a small IntelliJ window.

## Features:
- Tracks breakpoints in real-time.
- Displays breakpoint information (file, line number) within IntelliJ.
- Uses WebSockets for seamless communication with a Ktor server.
- Real-time updates in a dedicated UI window.

## Setup:
1. Install the plugin in IntelliJ IDEA.
2. Run the Ktor server to handle breakpoint data.
3. Use the plugin’s window in IntelliJ to view tracked breakpoints.

## Requirements:
- IntelliJ IDEA with plugin support.
- Ktor server for backend processing.

## License:
MIT License
