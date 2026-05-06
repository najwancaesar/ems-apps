document.addEventListener("DOMContentLoaded", () => {
    if (!requireAuth()) {
        return;
    }

    loadCurrentUser();
});

async function loadCurrentUser() {
    try {
        const currentUser = await apiRequest("/api/auth/me");

        document.getElementById("profileUsername").textContent = currentUser.username || "-";
        document.getElementById("profileEmail").textContent = currentUser.email || "-";
        document.getElementById("profileRole").textContent = currentUser.role || "-";
        document.getElementById("profileEmployeeId").textContent = currentUser.employeeId || "-";
        document.getElementById("profileEmployeeName").textContent = currentUser.employeeName || "-";
    } catch (error) {
        setMessage("userMessage", error.message, "error");
    }
}
