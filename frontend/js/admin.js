let departments = [];
let positions = [];
let employees = [];
let editingEmployeeId = null;

document.addEventListener("DOMContentLoaded", () => {
    if (!requireAdmin()) {
        return;
    }

    document.getElementById("adminUsername").textContent = localStorage.getItem("username") || "admin";
    document.getElementById("employeeForm").addEventListener("submit", handleEmployeeSubmit);
    document.getElementById("cancelEditButton").addEventListener("click", resetEmployeeForm);
    document.getElementById("refreshEmployeesButton").addEventListener("click", loadEmployees);

    initAdminDashboard();
});

async function initAdminDashboard() {
    try {
        await loadReferences();
        await loadEmployees();
    } catch (error) {
        setMessage("adminMessage", error.message, "error");
    }
}

async function loadReferences() {
    const [departmentData, positionData] = await Promise.all([
        apiRequest("/api/admin/departments"),
        apiRequest("/api/admin/positions")
    ]);

    departments = departmentData || [];
    positions = positionData || [];
    fillSelect("departmentId", departments, "Pilih department");
    fillSelect("positionId", positions, "Pilih position");
}

function fillSelect(selectId, items, placeholder) {
    const select = document.getElementById(selectId);
    select.innerHTML = `<option value="">${placeholder}</option>`;
    for (const item of items) {
        const option = document.createElement("option");
        option.value = item.id;
        option.textContent = item.name;
        select.appendChild(option);
    }
}

async function loadEmployees() {
    employees = await apiRequest("/api/admin/employees");
    renderEmployeeTable(employees || []);
}

function renderEmployeeTable(data) {
    const tableBody = document.getElementById("employeeTableBody");

    if (!data.length) {
        tableBody.innerHTML = `<tr><td colspan="7" class="empty-cell">Belum ada employee.</td></tr>`;
        return;
    }

    tableBody.innerHTML = data.map(employee => `
        <tr>
            <td>${escapeHtml(employee.employeeCode)}</td>
            <td>${escapeHtml(employee.fullName)}</td>
            <td>${escapeHtml(employee.email)}</td>
            <td>${escapeHtml(employee.departmentName || "-")}</td>
            <td>${escapeHtml(employee.positionName || "-")}</td>
            <td><span class="status-badge">${escapeHtml(employee.employmentStatus)}</span></td>
            <td>
                <div class="table-actions">
                    <button type="button" class="secondary-button" onclick="startEditEmployee(${employee.id})">Edit</button>
                    <button type="button" class="danger-button" onclick="softDeleteEmployee(${employee.id})">Delete</button>
                </div>
            </td>
        </tr>
    `).join("");
}

async function handleEmployeeSubmit(event) {
    event.preventDefault();
    setMessage("adminMessage", "");

    const payload = readEmployeeForm();
    const isEditing = Boolean(editingEmployeeId);
    const path = isEditing ? `/api/admin/employees/${editingEmployeeId}` : "/api/admin/employees";
    const method = isEditing ? "PUT" : "POST";

    try {
        await apiRequest(path, {
            method,
            body: JSON.stringify(payload)
        });

        setMessage("adminMessage", isEditing ? "Employee berhasil diupdate." : "Employee berhasil dibuat.", "success");
        resetEmployeeForm();
        await loadEmployees();
    } catch (error) {
        setMessage("adminMessage", error.message, "error");
    }
}

function readEmployeeForm() {
    return {
        employeeCode: document.getElementById("employeeCode").value.trim(),
        fullName: document.getElementById("fullName").value.trim(),
        email: document.getElementById("email").value.trim(),
        phone: document.getElementById("phone").value.trim(),
        address: document.getElementById("address").value.trim(),
        gender: document.getElementById("gender").value.trim(),
        birthDate: document.getElementById("birthDate").value || null,
        hireDate: document.getElementById("hireDate").value || null,
        employmentStatus: document.getElementById("employmentStatus").value,
        departmentId: Number(document.getElementById("departmentId").value),
        positionId: Number(document.getElementById("positionId").value)
    };
}

function startEditEmployee(id) {
    const employee = employees.find(item => item.id === id);
    if (!employee) {
        setMessage("adminMessage", "Employee tidak ditemukan di tabel saat ini.", "error");
        return;
    }

    editingEmployeeId = id;
    document.getElementById("formTitle").textContent = "Edit Employee";
    document.getElementById("cancelEditButton").classList.remove("hidden");
    document.getElementById("saveEmployeeButton").textContent = "Update Employee";

    document.getElementById("employeeCode").value = employee.employeeCode || "";
    document.getElementById("fullName").value = employee.fullName || "";
    document.getElementById("email").value = employee.email || "";
    document.getElementById("phone").value = employee.phone || "";
    document.getElementById("address").value = employee.address || "";
    document.getElementById("gender").value = employee.gender || "";
    document.getElementById("birthDate").value = employee.birthDate || "";
    document.getElementById("hireDate").value = employee.hireDate || "";
    document.getElementById("employmentStatus").value = employee.employmentStatus || "ACTIVE";
    document.getElementById("departmentId").value = employee.departmentId || "";
    document.getElementById("positionId").value = employee.positionId || "";

    window.scrollTo({ top: 0, behavior: "smooth" });
}

async function softDeleteEmployee(id) {
    const confirmed = window.confirm("Soft delete employee ini? Status akan menjadi INACTIVE.");
    if (!confirmed) {
        return;
    }

    try {
        await apiRequest(`/api/admin/employees/${id}`, { method: "DELETE" });
        setMessage("adminMessage", "Employee berhasil di-soft delete.", "success");
        await loadEmployees();
    } catch (error) {
        setMessage("adminMessage", error.message, "error");
    }
}

function resetEmployeeForm() {
    editingEmployeeId = null;
    document.getElementById("employeeForm").reset();
    document.getElementById("employmentStatus").value = "ACTIVE";
    document.getElementById("formTitle").textContent = "Tambah Employee";
    document.getElementById("cancelEditButton").classList.add("hidden");
    document.getElementById("saveEmployeeButton").textContent = "Simpan Employee";
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
