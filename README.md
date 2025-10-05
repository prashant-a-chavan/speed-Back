# SpeedBack Application

SpeedBack is a platform designed to simplify the process of scheduling feedback sessions within teams. It provides a clear, real-time overview of team members' availability and allows for quick and conflict-free bookings.

---

## 🌟 Key Features

-   **Real-Time Dashboard**: A live, shared view of all feedback slots that updates instantly for all users using WebSockets.
-   **Conflict-Free Booking**: The system intelligently prevents double-bookings, ensuring a person cannot be a booker and a bookie in the same slot.
-   **Interactive UI**: A modern, professional user interface built with React and TypeScript.
-   **API Documentation**: Comes with a live, interactive Swagger UI for exploring and testing the backend API.
-   **Persistent Data**: Uses a PostgreSQL database with Flyway for version-controlled schema migrations.
-   **Professional Tooling**: Integrated with code formatters (Spotless for Java, Prettier for frontend) and linters (ESLint) to maintain high code quality.

---

## 🛠️ Tech Stack

The project is built with a modern, robust, and scalable technology stack.

| Area         | Technology                                                                                   |
|:-------------|:---------------------------------------------------------------------------------------------|
| **Backend**  | **Spring Boot 3** (Java 17), Spring Data JPA, Spring WebSockets, Flyway, Swagger, PostgreSQL |
| **Frontend** | **React 18** (TypeScript), React Router, Axios, Prettier, ESLint, Yarn/NPM                   |

---

## 🚀 Getting Started

Follow these instructions to get a local copy of the project up and running for development and testing purposes.

### Prerequisites

You will need the following software installed on your machine:
-   **Java 17** or later 
-   **Maven 3.8** or later
-   **Node.js 18** or later
-   **Yarn** or **NPM**
-   **PostgreSQL 14** or later

### 1. Backend Setup

First, set up and run the Spring Boot backend server.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/prashant-a-chavan/speed-Back.git
    
    cd speed-Back
    ```

2.  **Set up the PostgreSQL Database:**
    Connect to PostgreSQL and create a dedicated user and database.
    ```sql
    -- Replace 'your_password', use your database password
    CREATE USER speedback_user WITH PASSWORD 'your_password';
    CREATE DATABASE speedback_db;
    GRANT ALL PRIVILEGES ON DATABASE speedback_db TO speedback_user;
    ```

3.  **Configure the Backend:**
    Navigate to the backend directory and configure your database connection.
    ```bash
    cd speed-Back
    ```
    -   Copy the example properties file: `cp src/main/resources/application.properties.example src/main/resources/application.properties` (if you create an example file)
    -   Open `src/main/resources/application.properties` and update the `spring.datasource` properties with the credentials you created above.
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/speedback_db
    spring.datasource.username=speedback_user
    spring.datasource.password=your_password
    ```

4.  **Run the Backend:**
    The application uses the Maven Wrapper, so you don't need a local Maven installation. Flyway will automatically run the database migrations on the first startup.
    ```bash
    ./mvnw spring-boot:run
    ```
    The backend server will start on `http://localhost:8080`.

### 2. Frontend Setup

Next, set up and run the React frontend.

1.  **Navigate to the frontend directory:**
    Open a **new terminal window** and navigate to the frontend folder.
    ```bash
    cd frontend
    ```

2.  **Install Dependencies:**
    ```bash
    yarn install
    # or: npm install
    ```

3.  **Run the Frontend:**
    ```bash
    yarn start
    # or: npm start
    ```
    The frontend development server will start and open a browser window at `http://localhost:3000`.

---

## ⚙️ Available Scripts & Commands

### Backend (`/speed-Back` directory)

The backend uses a `Makefile` for convenient shortcuts.

-   **`make format`**: Automatically formats all Java source code using Spotless.
-   **`make check`**: Checks if the code is formatted correctly (fails the build if not).
-   **`make build`**: Compiles the project, runs tests, and performs a format check.

### Frontend (`/frontend` directory)

The frontend uses Yarn/NPM scripts defined in `package.json`.

-   **`yarn start`**: Runs the app in development mode.
-   **`yarn build`**: Builds the app for production.
-   **`yarn validate`**: Checks both code formatting (Prettier) and code quality (ESLint).
-   **`yarn fix`**: Automatically fixes all formatting and linting issues.

---

## 📖 API Documentation (Swagger)

Once the backend is running, you can access the live, interactive API documentation provided by Swagger UI.

-   **Swagger UI URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---