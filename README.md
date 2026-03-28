# Bookstore Application

A bookstore web app project. The focus of this project is combining Spring and HTMX to make modern and responsive Server Siide Rendered application.

## Features

- **Book Catalog:** Browse list of books with detailed views for each title.
- **Author Profiles:** Explore information about book authors and their bibliography.
- **Shopping Cart:** Add, remove, and manage books in a shopping cart.
- **Order Management:** Secure checkout process with order history tracking for users.
- **User Authentication:** Registration and login system with role-based access control and email verification.
- **Admin Panel:** Administrative pages for inserting and managing books and authors.

## Tech

### Frontend
- **Framework:** [Thymeleaf](https://www.thymeleaf.org/) (Server-side rendering)
- **Dynamic Interaction:** [HTMX](https://htmx.org/) (via `htmx-spring-boot-thymeleaf`)
- **Styling:** [Tailwind CSS 4.0](https://tailwindcss.com/)

### Backend
- **Framework:** [Spring Boot 4.0.3](https://spring.io/projects/spring-boot)
   - [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) (Web routing and request handling)
   - [Spring Validation](https://docs.spring.io/spring-framework/reference/core/validation/bean-validation.html) (Data integrity and input validation)
   - [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb) (Object Document Mapping)
   - [Spring Mail](https://spring.io/guides/gs/sending-email/) (Mail services)
   - [Spring Security](https://spring.io/projects/spring-security) (Authentication)
- **Language:** [Java 25](https://openjdk.org/projects/jdk/25/)
- **Database:** [MongoDB](https://www.mongodb.com/)
- **Other Libraries:** [Lombok](https://projectlombok.org/)

## Installation & Setup

1. **Clone the repository:**
git clone https://github.com/milan/bookstore.git
cd bookstore


3. **Environment Configuration:**

*.env file to be added*

4. **Database Setup:**
Ensure MongoDB is running locally. The application includes a `DbSeeder.java` component that will
automatically populate the database with initial books and authors on startup if empty.

5. **Run the Development Server:**
Start the Spring Boot application
./mvnw spring-boot:run

Start Tailwind CSS watcher (in a separate terminal)
npm run watch

Open [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

*Testing is still not added.*

## Deployment

*Deployment is still not added.*


