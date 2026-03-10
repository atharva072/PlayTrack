# PlayTrack

<img width="1820" height="876" alt="image" src="https://github.com/user-attachments/assets/d5a21c47-3c32-4f2d-909a-7a99c1d5cdb6" />

A simple attendance tracking application that can be used to mark player's attendances for sports teams. This is a monolithically architected application (for now) that aims to perform the following functionalities:

- Manage users with different roles (Player, Coach, Admin, etc.)
- Organize teams and assign players
- Track player attendance and participation
- Provide secure authentication using JWT

Features implemented so far:

- Authentication & Security: User registration and login, JWT-based authentication, Role-based authorization, Custom authentication entry point and access handlers, Stateless session management
- User Management: User entity with roles, Secure password encryption (BCrypt), Integration with Spring Security
- Team Management: Team creation and management, Player-team relationships, Captain assignment, Database-level foreign key constraints
- Player Management: Player entity linked to user profiles, Team association, Structured domain modeling
- Attendance Tracking: Attendance records per player and team, Date-based tracking, Database relationships with players and teams
- Caching: Redis-based caching for username existence checks during registration, Cache warmup on startup to pre-load existing usernames from PostgreSQL into Redis, Write-through cache sync on new user registration, TTL-based cache expiry as a self-healing safety net
- Database Design: Normalized schema, Proper use of primary/foreign keys, Migration from name-based keys to ID-based relations, PostgreSQL integration

Tech Stack:
- Backend: Java, Spring Boot
- Frontend: HTML, CSS, JavaScript
- Security: Spring Security, JWT Authentication
- Database: PostgreSQL
- ORM: Hibernate / JPA
- Cache: Redis (via Docker)
- Build Tool: Maven
- API Style: RESTful APIs
