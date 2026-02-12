# PlayTrack

A simple attendance tracking application that can be used to player's attendances for sports teams. This is a monolithically architected application (for now) that aims perform the following functionalities -
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
- Database Design: Normalized schema, Proper use of primary/foreign keys, Migration from name-based keys to ID-based relations, PostgreSQL integration

Tech Stack:
- Backend: Java, Spring Boot
- Security: Spring Security, JWT Authentication
- Database: PostgreSQL
- ORM: Hibernate / JPA
- API Style: RESTful APIs
- Build Tool: Maven
