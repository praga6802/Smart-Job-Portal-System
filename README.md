# Smart Job Portal System

A **Spring Boot based backend application** that provides a platform for candidates, companies, and administrators to manage job-related activities securely. The system provides **JWT-based authentication**, email verification using OTP, job posting and application management, resume handling, and role-based access to REST APIs.

## 🚀 Features

* User Registration and Login
* **JWT Based Authentication**
* **Role-Based Authorization**
* Secure Password Encryption using **BCrypt**
* **Email Verification using OTP**
* OTP Expiration and Verification Handling
* Candidate Management
* Company Management
* Job Posting Management
* Job Application Management
* Resume Upload and Download
* Active Job Management
* RESTful API Architecture
* Global Exception Handling
* Common API Response Structure using DTOs
* Database Integration using **Spring Data JPA**
* MySQL Database Integration
* API Testing using Postman

## 🛠 Tech Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT (JSON Web Token)**
* **Spring Data JPA**
* **Hibernate**
* **MySQL**
* **Maven**
* **Postman**
* **Git & GitHub**

## 🔐 Authentication & Authorization

The application uses **Spring Security** with **JWT authentication** to secure protected APIs.

### Authentication Flow

1. User registers with their required details.
2. An **OTP is sent to the registered email address** for email verification.
3. User verifies the OTP within the allowed expiration time.
4. User logs in using their email and password.
5. The system authenticates the user credentials.
6. A **JWT token** is generated after successful authentication.
7. The token must be included in the `Authorization` header when accessing protected APIs.

```http
Authorization: Bearer <JWT_TOKEN>
```

### Role-Based Access

Different users can access APIs based on their assigned roles.

* **Candidate** – Search and apply for jobs, manage profile and resume
* **Company** – Create and manage job postings and view applications
* **Admin** – Manage and monitor system-level operations

## 📧 Email Verification

The registration process includes email verification using **OTP**.

* OTP is generated during registration.
* OTP is sent to the user's email.
* OTP has a limited validity period.
* Invalid or expired OTPs are rejected.
* Verification status is maintained to prevent reuse of OTPs.

## 💼 Job Management

Companies can manage job postings through secured REST APIs.

The system supports operations such as:

* Create job postings
* Update job postings
* Manage job status
* Retrieve active jobs
* View job details
* Manage jobs associated with a company

## 📄 Resume Management

Candidates can upload and manage their resumes.

The system supports:

* Resume upload
* Resume storage
* Resume download
* Resume validation
* Resume retrieval through secured APIs

## 📝 Job Application Management

Candidates can apply for available jobs, while companies can manage applications associated with their job postings.

The system provides APIs for:

* Applying for jobs
* Viewing job applications
* Managing application information
* Retrieving applications for company job postings

## 🗄 Database

The application uses **MySQL** as the relational database and **Spring Data JPA / Hibernate** for database operations.

Major entities include:

* Users
* Candidate
* Company
* Job
* Job Application
* Verification Data
* Resume

## 📁 Project Architecture

The application follows a layered Spring Boot architecture:

```text
src/main/java
└── com.example.smartjobportalsystem
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    ├── security
    ├── exception
    └── configuration
```

### Architecture Flow

```text
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL Database
```

Security-related requests are handled through Spring Security and JWT filters before reaching protected controllers.

## 🧪 API Testing

The REST APIs are tested using **Postman**.

The project includes APIs for:

* Authentication
* Candidate management
* Company management
* Job management
* Job applications
* Resume management
* Email verification

## 🔮 Future Improvements

* **Refresh Token Service**
* Frontend / UI implementation
* Job search and advanced filtering
* Improved pagination and sorting
* Additional notification features
* Deployment and cloud integration

## 👨‍💻 Author

**Pragadeeswaran Sekar**

Aspiring **Java Backend Developer** passionate about building secure and scalable applications using **Java, Spring Boot, REST APIs, Spring Security, and MySQL**.
