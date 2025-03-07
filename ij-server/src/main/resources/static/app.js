const ws = new WebSocket("ws://localhost:8080/updatedBreakpoints");

ws.onmessage = function(event) {
    const breakpoints = JSON.parse(event.data);
    const container = document.getElementById("breakpoints");
    container.innerHTML = "";

    breakpoints.forEach(bp => {
        const div = document.createElement("div");
        div.className = "breakpoint";

        const fileDetails = document.createElement("div");
        fileDetails.className = "breakpoint-details";
        fileDetails.innerHTML = `<strong>File:</strong> ${bp.path}`;

        const lineDetails = document.createElement("div");
        lineDetails.className = "breakpoint-details";
        lineDetails.innerHTML = `<strong>Line:</strong> ${bp.line}`;

        div.appendChild(fileDetails);
        div.appendChild(lineDetails);
        container.appendChild(div);
    });
};

ws.onopen = function() {
    console.log("WebSocket connected");
};

ws.onerror = function(error) {
    console.log("WebSocket Error:", error);
};

ws.onclose = function() {
    console.log("WebSocket connection closed");
};
