# UserDocStorageService

A Spring Boot RESTful service that allows users to upload and download documents to/from an AWS S3 bucket. This project is backend-only, fully testable using Postman or Swagger, and follows clean code principles with centralized exception handling and structured responses.

---

## ✅ Features

- Upload files to AWS S3 under the user's directory  
- Download files from AWS S3 using username and filename  
- REST-compliant APIs (no UI)  
- Swagger UI for API testing and documentation  
- Global exception handling using `@ControllerAdvice`  
- Structured API responses (`ApiResponse<T>`)  
- Logging using SLF4J  
- Unit tests with JUnit 5 and Mockito  
- Clean, layered architecture  

---

## 📦 Tech Stack

- Java 21  
- Spring Boot 3.5.3  
- AWS Java SDK (S3)  
- Spring Web  
- SpringDoc OpenAPI (Swagger UI)  
- JUnit 5 + Mockito  
- Gradle  
- Lombok  

---

## 🚀 How to Run

### 1. Clone the repository

```bash
git clone https://github.com/your-username/UserDocStorageService.git
cd UserDocStorageService
```

### 2. Update `application.properties`

```properties
cloud.aws.credentials.access-key=YOUR_ACCESS_KEY
cloud.aws.credentials.secret-key=YOUR_SECRET_KEY
cloud.aws.region.static=us-east-1
s3.bucket.name=your-s3-bucket-name
```

### 3. Build and run

```bash
./gradlew clean build
./gradlew bootRun
```

---

## 🔄 API Endpoints

### 📤 Upload File

**POST** `/api/files/upload`

**Request (form-data):**
- `userName`: required (string)
- `file`: required (file)

**Response:**
```json
{
  "status": "success",
  "message": "File uploaded successfully.",
  "data": null
}
```

---

### 📥 Download File

**GET** `/api/files/download?userName=manish&fileName=test.txt`

**Response:**  
Returns the file as a stream with content type `application/octet-stream`.

---

## 📘 Swagger UI

You can test and explore APIs via Swagger at:  
**http://localhost:8080/swagger-ui/index.html**

---
