# Real-Time Chat Application

A full-stack chat application built using **Spring Boot**, **Angular**, and **JWT-based authentication**, containerized with **Docker** and orchestrated using **Docker Compose**. The system enables secure messaging between users with persistent message storage and a production-style deployment architecture.

---

## 🚀 Features

* Secure **JWT-based authentication and authorization**
* User **registration and login**
* **One-to-one chat messaging**
* Persistent chat message storage
* **RESTful API architecture**
* Containerized services using **Docker**
* **Nginx reverse proxy** serving the Angular application
* Monorepo structure for frontend and backend

---

## 🏗 Architecture

```
Browser
   ↓
Nginx (Angular SPA)
   ↓
Spring Boot Backend (JWT Authentication + Chat APIs)
   ↓
PostgreSQL Database
```

### Stack Overview

**Frontend**

* Angular
* TypeScript
* HTML / CSS

**Backend**

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* REST APIs

**Infrastructure**

* Docker
* Docker Compose
* Nginx

**Database**

* PostgreSQL

---

## 📁 Project Structure

```
chatapplication
│
├── compose.yaml
│
├── chatapp
│   ├── src
│   ├── Dockerfile
│   └── pom.xml
│
└── chatappFrontend
    ├── src
    ├── Dockerfile
    └── nginx.conf
```

* `chatapp` → Spring Boot backend service
* `chatappFrontend` → Angular frontend served via Nginx
* `compose.yaml` → Docker Compose orchestration

---

## 🔐 Authentication

The application uses **JWT (JSON Web Tokens)** for stateless authentication.

### Register

```
POST /auth/register
```

### Login

```
POST /auth/login
```

### Send Message

```
POST /chat/send
```

### Fetch Messages

```
GET /chat/messages
```

Protected endpoints require:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 🐳 Running the Application with Docker

### 1. Clone the repository

```
git clone https://github.com/Akash-nitA/chatapplication.git
cd chatapplication
```

### 2. Start the containers

```
docker compose up --build
```

### 3. Access the application

Frontend

```
http://localhost
```

Backend API

```
http://localhost:8082
```

---

## ⚙️ Environment Variables

Backend configuration is managed through a `.env` file.

Example:

```
JWT_SECRET=your_secret_key
DB_USERNAME=postgres
DB_PASSWORD=postgres123
```

---

## 🛠 Engineering Highlights

* Implemented **stateless JWT authentication** with Spring Security.
* Designed clean backend architecture using **service layers, DTOs, and repository pattern**.
* Built a **containerized multi-service architecture** using Docker and Docker Compose.
* Configured **Nginx reverse proxy** to serve the Angular SPA and route backend API requests.
* Eliminated CORS issues by using **same-origin reverse proxy routing**.

---

## 📎 Repository

```
https://github.com/Akash-nitA/chatapplication
```

---

## 👤 Author

**Akash Acharjee**

Email: [akashacharjee212@gmail.com](mailto:akashacharjee212@gmail.com)
LinkedIn: https://www.linkedin.com/in/akash-acharjee-b07909205/
