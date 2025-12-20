# 📝 Blog Platform API  

🧪 API Testing (Postman)

All APIs are tested and documented using Postman.

📌 Postman Collection Link:
👉 (https://ramkumarkr.postman.co/workspace/Ramkumar-K-R's-Workspace~cd265aee-4258-4f63-8868-fd8dfb8607e4/collection/44813629-bb4142b6-01cc-4f68-92b6-5d2d66757213?action=share&creator=44813629)

A modern, stateless blog backend built with **Spring Boot 3**, **JWT-based authentication**, **Spring Security**, and **Dockerized PostgreSQL**.  
This service provides secure user login, post management, categories, tags, and more.

---

## 🚀 Features

### 🔐 Authentication
- User login with **JWT tokens**
- Stateless sessions with **Spring Security**
- Custom `JwtAuthenticationFilter` for token validation
- Password hashing using `PasswordEncoder`

### 🗂 Blog Features
- Public API for viewing posts, categories, tags  
- Protected endpoints for creating, editing, and deleting posts (requires JWT)

### 🛢 Database
- PostgreSQL 15 running inside Docker  
- Automatically managed schema with JPA + Hibernate  
- Clean migration model

### 🧱 Architecture
- Controller → Service → Repository pattern  
- DTO-based request/response  
- Custom `UserDetails` & `UserDetailsService`  
- Token generation, validation, and parsing

---


