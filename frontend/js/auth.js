document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", handleLoginSubmit);
    }
});

async function handleLoginSubmit(event) {
    event.preventDefault();
    setMessage("loginMessage", "");

    const formData = new FormData(event.target);
    const payload = {
        username: String(formData.get("username") || "").trim(),
        password: String(formData.get("password") || "")
    };

    try {
        const response = await apiRequest("/api/auth/login", {
            method: "POST",
            body: JSON.stringify(payload)
        });

        localStorage.setItem("token", response.token);
        localStorage.setItem("role", response.role);
        localStorage.setItem("username", response.username);
        localStorage.setItem("employeeId", response.employeeId || "");

        if (response.role === "ADMIN") {
            window.location.href = "admin-dashboard.html";
            return;
        }

        window.location.href = "user-dashboard.html";
    } catch (error) {
        setMessage("loginMessage", error.message, "error");
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "index.html";
}

function requireAuth() {
    if (!localStorage.getItem("token")) {
        window.location.href = "index.html";
        return false;
    }
    return true;
}

function requireAdmin() {
    if (!requireAuth()) {
        return false;
    }
    if (localStorage.getItem("role") !== "ADMIN") {
        window.location.href = "index.html";
        return false;
    }
    return true;
}
