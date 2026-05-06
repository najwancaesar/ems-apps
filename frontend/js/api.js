const API_BASE_URL = "http://localhost:8080";

async function apiRequest(path, options = {}) {
    const token = localStorage.getItem("token");
    const headers = new Headers(options.headers || {});

    headers.set("Accept", "application/json");
    if (options.body && !headers.has("Content-Type")) {
        headers.set("Content-Type", "application/json");
    }
    if (token) {
        headers.set("Authorization", `Bearer ${token}`);
    }

    const response = await fetch(`${API_BASE_URL}${path}`, {
        ...options,
        headers
    });

    const responseText = await response.text();
    const data = responseText ? parseJson(responseText) : null;

    if (response.status === 401) {
        localStorage.clear();
        if (path !== "/api/auth/login" && !window.location.pathname.endsWith("index.html")) {
            window.location.href = "index.html";
        }
        throw new Error(readErrorMessage(data, "Sesi berakhir. Silakan login kembali."));
    }

    if (!response.ok) {
        throw new Error(readErrorMessage(data, `Request gagal dengan status ${response.status}`));
    }

    return data;
}

function parseJson(text) {
    try {
        return JSON.parse(text);
    } catch (error) {
        return null;
    }
}

function readErrorMessage(data, fallbackMessage) {
    if (!data) {
        return fallbackMessage;
    }
    if (data.errors) {
        return Object.values(data.errors).join(", ");
    }
    return data.message || data.error || fallbackMessage;
}

function setMessage(elementId, message, type = "") {
    const element = document.getElementById(elementId);
    if (!element) {
        return;
    }
    element.textContent = message || "";
    element.className = `message ${type}`.trim();
}
