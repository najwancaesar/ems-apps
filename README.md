# EMS - Employee Management System

## Perusahaan

PT. LOCAL CENTRAL DIGITAL

## Deskripsi

Sistem manajemen karyawan sederhana berbasis website untuk kebutuhan pembelajaran fullstack.

## Tech Stack

- Frontend: HTML, CSS, Vanilla JavaScript
- Backend: Java Spring Boot Maven
- Database: MySQL Docker
- Security: Spring Security + JWT
- API Docs: Postman
- Container: Docker Compose

## Struktur Folder

```text
backend/
frontend/
docs/postman/
docker-compose.yml
```

## Cara Menjalankan Local Development

Jalankan MySQL:

```powershell
docker compose up -d mysql
```

Jalankan backend:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Jalankan frontend:

- Buka `frontend/index.html` dengan VS Code Live Server.
- Pastikan Live Server berjalan dari `http://localhost:5500` atau `http://127.0.0.1:5500`.

## Cara Menjalankan Full Docker

```powershell
docker compose up --build
```

## URL Penting

- Frontend Docker: http://localhost:3000
- Backend health: http://localhost:8080/api/health
- Login API: http://localhost:8080/api/auth/login

## Default Account

Admin:

```text
username: admin
password: admin123
```

User:

```text
username: emp001
password: user123
```

## Endpoint Utama

- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/admin/departments`
- `POST /api/admin/departments`
- `GET /api/admin/positions`
- `POST /api/admin/positions`
- `GET /api/admin/employees`
- `POST /api/admin/employees`
- `PUT /api/admin/employees/{id}`
- `DELETE /api/admin/employees/{id}`

## Cara Import Postman Collection

1. Import `docs/postman/EMS.postman_collection.json`.
2. Import `docs/postman/EMS.local_environment.json`.
3. Pilih environment `EMS Local`.
4. Jalankan request `POST Login - Admin`.
5. Token akan otomatis masuk ke variable `admin_token`. Jika tidak, copy token dari response ke `admin_token`.

## Troubleshooting

- `ECONNREFUSED localhost:8080` berarti backend belum running.
- `401 Unauthorized` berarti belum login atau token salah.
- `403 Forbidden` berarti role tidak punya akses.
- `Port 8080 already in use` berarti ada proses lain memakai port tersebut.
- MySQL connection error berarti container mysql belum running atau datasource salah.
- Jika sebelumnya pernah memakai password MySQL lama dan ingin memakai konfigurasi Docker final, reset database dengan hati-hati:

```powershell
docker compose down -v
```

## Checklist Smoke Test

- `GET /api/health`
- Login admin
- Akses admin employees
- Create department
- Create position
- Create employee
- Login user
- Buka frontend
