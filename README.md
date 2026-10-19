# CommunityFix - Community Problem Solving Platform

A full-stack web application for gated communities to report, track, and resolve problems collaboratively.

## Tech Stack

- **Backend**: Java 17, Spring Boot 3, Spring Security (JWT), Spring WebSocket, H2 Database
- **Frontend**: React 18, Tailwind CSS, Zustand, Axios, SockJS, Recharts
- **Video**: Jitsi Meet (embedded via iframe)

## Prerequisites

- Java 17+
- Node.js 18+
- Maven

## Project Structure

```
communityfix/
├── backend/
│   ├── src/main/java/com/communityfix/
│   │   ├── config/          # Security, WebSocket, CORS, Exception Handler
│   │   ├── controller/      # REST API Controllers
│   │   ├── model/           # JPA Entities & Enums
│   │   ├── repository/      # Spring Data JPA Repositories
│   │   ├── service/         # Business Logic Services
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── security/        # JWT & UserDetails
│   │   └── CommunityFixApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── pages/           # Page components
│   │   ├── components/      # Reusable components
│   │   ├── store/           # Zustand stores
│   │   ├── api/             # API layer (Axios)
│   │   ├── hooks/           # Custom React hooks
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── tailwind.config.js
│   ├── vite.config.js
│   └── package.json
```

## Quick Start

### Backend

Set `JWT_SECRET` to a unique secret of at least 32 characters before starting the backend.

PowerShell:

```powershell
$env:JWT_SECRET = "<your-unique-secret-of-at-least-32-characters>"
```

macOS/Linux:

```bash
export JWT_SECRET="<your-unique-secret-of-at-least-32-characters>"
```

```bash
cd backend
mvn spring-boot:run
```

- API runs on http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

### Frontend

```bash
cd frontend
npm install
npm run dev
```

- App runs on http://localhost:5173

## Demo Credentials

| Role        | Email                     | Password    |
|-------------|--------------------------|-------------|
| Super Admin | admin@communityfix.com   | password123 |
| Admin       | priya@communityfix.com   | password123 |
| Resident    | arjun@gmail.com          | password123 |

## Features

- Problem reporting with categories and urgency levels
- Community voting and volunteer system
- Virtual meetings with Jitsi integration
- Real-time notifications via WebSocket
- Resident directory and reputation leaderboard
- Admin dashboard with analytics
- Role-based access control

## API Endpoints

- `POST /api/auth/login` - Login
- `POST /api/auth/register` - Register
- `GET /api/problems` - List problems (with filters)
- `POST /api/problems` - Create problem
- `GET /api/meetings` - List meetings
- `POST /api/meetings` - Schedule meeting
- `GET /api/users/leaderboard` - Reputation leaderboard
- `GET /api/admin/stats` - Admin dashboard stats
