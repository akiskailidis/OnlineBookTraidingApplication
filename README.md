# Online Social Bookstore Application

A Spring Boot web application where users can create accounts, manage book offers, request books, and receive recommendations based on preferences. Built using Agile Scrum methodology.

## Team Members

- Kailidis Kyrillos (AM: 4680)  
- Petrogiannis Georgios (AM: 4779)  

## Project Overview

This project simulates a social platform for exchanging books between users. It includes user profiles, personalized recommendations, search functionality, and notification handling.

## Features

- User registration, login, logout
- Profile creation with name, address, age, phone, favorite categories, and authors
- Add/edit/remove book offers (title, author, category, summary)
- Search book offers by title/author (exact or approximate match)
- Make book requests to other users
- Accept or reject book requests
- View and manage incoming requests
- Browse personalized recommendations
- Contact other users after matching
- Secure role-based access (Spring Security)

## Architecture

The system is developed using the following layers:

- **Controllers**: Handle HTTP requests
- **Services**: Business logic and interaction coordination
- **Mappers**: Data access and persistence logic
- **Models**: Entity classes
- **Config**: Web and security configuration
- **Tests**: Unit tests with JUnit

## Technologies

- Java 17  
- Spring Boot  
- Spring MVC  
- Spring Security  
- Thymeleaf  
- H2 (in-memory database)  
- Maven  
- JUnit 5  

## Scrum Process

| Sprint | Dates             | User Stories                                                                 |
|--------|------------------|------------------------------------------------------------------------------|
| 1      | 22/04/2023        | Account creation, login, logout                                              |
| 2–4    | 15/04–13/05/2023  | Profile management, book offer creation, search, request, and recommendation |

### Roles

- **Product Owner**: Kailidis Kyrillos  
- **Scrum Master**: Petrogiannis Georgios  
- **Developers**: Both team members  

## Folder Structure

social-bookstore/
├── src/
│ ├── main/java/
│ │ ├── controllers/
│ │ ├── services/
│ │ ├── mappers/
│ │ ├── model/
│ │ └── config/
│ ├── main/resources/
│ │ ├── templates/ (Thymeleaf views)
│ │ └── static/ (CSS/JS)
│ └── test/
│ ├── UserServiceImplTest.java
│ └── UserProfileServiceImplTest.java
├── pom.xml
└── README.md


Testing
Unit tested services using JUnit 5

Tested edge cases: invalid credentials, empty profile data, request handling

Security
Spring Security for login/logout and role management

Password encryption with BCrypt

Admin/User separation

License
This project is for educational use only.
