# Campaign Management System

A Spring Boot backend project for managing campaigns, employees, tasks, payments, and task submissions.

## Project Idea

Admins can create campaigns, assign employees to them, define required amounts, and create simple tasks.

Employees can view their assigned campaigns, complete tasks, and upload between 1 and 5 images as proof of completion.

Clients have read-only access to available campaign information.

## Roles

- **SUPERADMIN**
    - Full access
    - Can create, update, view, and delete

- **ADMIN**
    - Can manage almost everything
    - Cannot delete

- **EMPLOYEE**
    - Can view assigned campaigns
    - Can complete tasks
    - Can upload task submission images
    - Cannot edit or delete administrative data

- **CLIENT**
    - Read-only access

## Main Features

- User authentication with JWT
- Role-based authorization
- Campaign management
- Employee assignment to campaigns
- Campaign payment tracking
- Task management
- Task submissions
- Upload 1–5 images per submission
- Validation and global exception handling
- Pagination and search
- Swagger/OpenAPI documentation
- Database migrations with Flyway

## Tech Stack

- Java 25
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Flyway
- Maven
- Swagger / OpenAPI

## Architecture

The project follows a **Modular Monolith** architecture with a **feature-based package structure**.

Example:

```text
auth/
user/
campaign/
task/
submission/
payment/
common/