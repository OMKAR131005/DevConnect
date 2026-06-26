# DevConnect 🚀

> A social media platform built for developers — connect with seniors, share knowledge, and see where you stand globally based on your real coding platform stats.

---

## What is DevConnect?

DevConnect is a full-stack developer community platform. The idea is simple — developers should have a place where they can build a profile that actually shows their skills, follow people they want to learn from, share what they know through posts, chat in real time, and compete on a leaderboard that is based on their actual coding activity — not just what they write on their resume.

This project was built as a placement project but the goal was always to make it production ready — not just something that looks good in a demo.

---

## How It Works — The Big Picture

```
┌─────────────────┐         ┌──────────────────────────┐
│  React Frontend  │ ──────▶ │   Main Backend (8090)     │
│  Vite + Tailwind │◀─────── │   Spring Boot + MySQL     │
└─────────────────┘         │   Redis + Cloudinary      │
                             └──────────┬───────────────┘
                                        │
                                   RabbitMQ
                             (fires events on like,
                              comment, follow)
                                        │
                             ┌──────────▼───────────────┐
                             │  Notification Service     │
                             │  Spring Boot (8091)       │
                             │  Separate MySQL DB        │
                             └──────────────────────────┘
```

The main backend handles everything — auth, profiles, posts, follow system, chat, rankings. When something happens like a like or a follow, it fires an event to RabbitMQ. The notification microservice listens to that queue, saves the notification in its own database, and the frontend polls port 8091 directly to show notifications.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.2 |
| Database | MySQL |
| Cache | Redis |
| Auth | JWT + httpOnly Cookie + TOTP 2FA |
| File Storage | Cloudinary |
| Real-time Chat | STOMP WebSocket |
| Message Broker | RabbitMQ |
| Notification Service | Separate Spring Boot Microservice |
| Frontend | React + Vite + Tailwind CSS |

---

## Database Design

The database went through a lot of changes during development. New features kept coming in and almost every feature needed a new entity or a change in an existing one.

Here is the final structure:

```
users                → stores auth data (email, password, 2FA secret)
profiles             → stores display data (bio, skills, education, social links)
posts                → stores posts with visibility control
comments             → linked to posts, cascade delete
post_likes           → separate table to track who liked what (toggle support)
follows              → tracks follow relationships with status (PENDING / ACCEPTED)
messages             → stores chat messages between two users
notifications        → lives in a separate DB (notificationDB) in the microservice
profile_views        → one row per unique viewer, updates on revisit
```

**Why two tables for user and profile?**
Auth data and display data are different things. `users` table is touched on every request for auth. `profiles` table is only loaded when someone views a profile. Keeping them separate makes the design cleaner.

**N+1 Problem**
This hit hard when fetching paginated lists. When you load a list of posts and each post fetches its author separately — that is N+1 queries. For every 10 posts, it was making 11 database calls. Fixed this using JPQL constructor expressions so the join happens at the query level and not in Java code.

---

## Authentication

Auth was the most learning-heavy part of this project.

Previously Bearer token in the Authorization header was the approach. It works but the token lives in localStorage which is accessible via JavaScript — meaning any XSS attack can steal it.

This time httpOnly cookies were used. The browser stores the JWT in a cookie that JavaScript cannot read at all. Even if there is an XSS vulnerability somewhere, the token is safe. The browser automatically sends the cookie on every request — no manual token management needed on the frontend.

**Flow:**
```
Register → Login → JWT set in httpOnly cookie
→ Every request cookie goes automatically
→ JwtFilter reads cookie → validates → sets SecurityContext
```

**2FA Flow:**
```
Login → if 2FA enabled → temp JWT issued (5 min, isPending2FA=true)
→ User enters TOTP code from Google Authenticator
→ Verified → full JWT issued
```

TOTP is implemented using `com.warrenstrange:googleauth` — same algorithm as Google Authenticator. QR code is generated using ZXing and shown to the user during setup.

---

## Why RabbitMQ and Not Kafka?

Kafka was the first choice. Setup was done, dependencies added, but errors kept coming one after another. As an engineer the instinct is to keep debugging — but after a point it was clear that the time being spent was not going anywhere productive.

That is when RabbitMQ came up as an alternative. Lighter, easier to set up locally, and more than enough for this use case. The decision was simple — use the tool that lets you build, not the tool that sounds impressive.

RabbitMQ setup:
- One TopicExchange — `notification.exchange`
- One Queue — `notification.queue`
- Three routing keys — `notification.like`, `notification.comment`, `notification.follow`

Main backend produces events. Notification service consumes them.

---

## Rankings

The leaderboard is not based on self-reported data. It pulls live stats from:
- **LeetCode API** — problems solved
- **Codeforces API** — rating and contest history
- **GitHub API** — contributions and repos

These are combined into a multi-factor score and stored in a **Redis Sorted Set**. Redis sorted sets are perfect for leaderboards — O(log N) insert and O(log N) rank fetch. No need to sort a database table on every request.

---

## Features

- JWT auth with httpOnly cookies and Google Authenticator 2FA
- Developer profiles with skills, education, social links, profile picture
- Profile view analytics — weekly, monthly, total
- Posts with like, comment, visibility control (Public / Followers / Only Me)
- Instagram style follow system — instant for public, request for private
- One-to-one real-time chat via STOMP WebSocket
- Notification microservice via RabbitMQ
- Global rankings via LeetCode, Codeforces, GitHub APIs
- Redis caching for performance
- Cloudinary for image uploads

---

## Local Setup

### What You Need
- Java 17
- Maven
- MySQL
- Redis
- RabbitMQ
- Node.js

### Start Redis and RabbitMQ (WSL)

```bash
wsl --user root
service redis-server start
service rabbitmq-server start
```

### Main Backend

```bash
cd bakend

# Update src/main/resources/application.properties
# Set your MySQL credentials, Cloudinary keys

mvn spring-boot:run
# Runs on port 8090
```

### Notification Service

```bash
cd notification-service

# Update src/main/resources/application.properties

mvn spring-boot:run
# Runs on port 8091
```

### Frontend

```bash
cd frontend

# Create .env file
echo "VITE_BACKEND_URL=http://localhost:8090" > .env

npm install
npm run dev
# Runs on port 5173
```

---

## API Collection

A full Postman collection is included in this repo covering all REST endpoints across auth, profiles, posts, follow, messages, notifications, and rankings.

Import the collection directly into Postman and test everything without writing a single request manually.

> The WebSocket endpoints use STOMP protocol — connect to `ws://localhost:8090/ws` with the JWT cookie set.

---

## Security

- JWT in httpOnly cookie — not accessible via JavaScript
- TOTP 2FA via Google Authenticator
- CORS restricted to frontend origin only
- Stateless backend — no server side sessions
- BCrypt password hashing
- Temp JWT for 2FA pending state — blocks all API access until 2FA is complete

---

## Author

**Omkar Gawande**  
B.Tech Information Technology — SGGS Institute of Engineering & Technology, Nanded  
[GitHub](https://github.com/OMKAR131005)

---

> This project is not perfect. The database schema changed almost every day in the beginning, Kafka was tried and dropped, the auth system was rewritten from Bearer tokens to cookies midway. But that is exactly what made it worth building.
