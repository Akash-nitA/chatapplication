# Real-Time Chat Application

A full-stack one-to-one chat application built with Spring Boot + Angular, secured with JWT, and deployed with Docker Compose behind Nginx.

## Features

- JWT-based authentication and authorization
- User registration and login
- User list for chat selection
- One-to-one conversation history
- Send messages to selected users
- Realtime updates using WebSocket push events
- Polling fallback when WebSocket is disconnected
- Nginx reverse proxy for SPA, REST APIs, and WebSocket upgrade

## Architecture

```text
Browser (Angular SPA)
   |
   v
Nginx (port 80)
   |-- /auth/*, /chat/*  -> Spring Boot (port 8082)
   '-- /ws/*             -> Spring Boot WebSocket
                              |
                              v
                          PostgreSQL
```

## Tech Stack

- Frontend: Angular, TypeScript, HTML, CSS
- Backend: Java 17, Spring Boot, Spring Security, Spring Data JPA
- Realtime: Spring WebSocket + browser WebSocket client
- Infra: Docker, Docker Compose, Nginx
- Database: PostgreSQL

## Project Structure

```text
chatapplication
├── compose.yaml
├── chatapp
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
└── chatappFrontend
    ├── src
    ├── Dockerfile
    └── nginx.conf
```

## API Overview

Public endpoints:

```text
POST /auth/register
POST /auth/login
```

Protected endpoints (require Bearer token):

```text
GET  /chat/users
GET  /chat/messages
GET  /chat/messages?with=<username>
POST /chat/send
```

Authorization header:

```text
Authorization: Bearer <JWT_TOKEN>
```

WebSocket subscription:

```text
ws://localhost/ws/chat?token=<JWT_TOKEN>
```

Server sends `NEW_MESSAGE` events to sender and receiver sessions.

## Environment Setup

Create `chatapp/.env` with:

```text
DB_USERNAME=postgres
DB_PASSWORD=postgres123
JWT_SECRET=<base64-encoded-secret>
```

Generate a JWT secret (example):

```bash
openssl rand -base64 32
```

Notes:

- Backend currently expects PostgreSQL reachable at `host.docker.internal:5432/postgres`.
- Compose file does not include a dedicated PostgreSQL container yet.

## Run with Docker Compose

```bash
git clone https://github.com/Akash-nitA/chatapplication.git
cd chatapplication
docker compose build --no-cache backend frontend
docker compose up -d
```

Access:

- Frontend: `http://localhost`
- Backend (direct): `http://localhost:8082`

## Troubleshooting

- `403` on `/chat/*`: token missing/invalid, or expired.
- `No mapping for GET /chat/users`: stale backend image; rebuild backend with `--no-cache`.
- Repeated `GET /chat/messages?with=...`: polling fallback is active because WebSocket is not connected.
- Verify WebSocket connection in DevTools Network: `/ws/chat?...` should return `101 Switching Protocols`.

## Repository

```text
https://github.com/Akash-nitA/chatapplication
```

## Author

Akash Acharjee  
Email: [akashacharjee212@gmail.com](mailto:akashacharjee212@gmail.com)  
LinkedIn: https://www.linkedin.com/in/akash-acharjee-b07909205/
