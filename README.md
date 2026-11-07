# Smart HelpDesk 🎫

A desktop-based HelpDesk Ticket Management System developed using **JavaFX, Java 17, Maven, and MongoDB**. The application allows users to report technical issues, track their support tickets, and enables administrators to manage tickets and monitor their status through a dashboard.

## 🚀 Features

### 👤 User Module
- User registration and login.
- User dashboard.
- Create support tickets.
- View personally submitted tickets.
- Track ticket status.
- Refresh ticket information.
- Secure password storage using BCrypt.
- Logout functionality.

### 🛠️ Admin Module
- Admin authentication.
- Admin dashboard with ticket statistics.
- View all submitted tickets.
- View total tickets.
- Monitor open tickets.
- Monitor in-progress tickets.
- Monitor resolved tickets.
- Update ticket status.
- Logout functionality.

## 🧰 Tech Stack

| Category | Technology |
|---|---|
| Programming Language | Java 17 |
| Desktop UI | JavaFX |
| UI Design | FXML |
| Styling | CSS / JavaFX Inline CSS |
| Database | MongoDB |
| Database Connectivity | MongoDB Java Sync Driver |
| Password Security | jBCrypt |
| Build Tool | Maven |
| IDE | Visual Studio Code |

## 📂 Project Structure

```text
HelpDesk/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── helpdesk/
│       │           ├── config/
│       │           │   └── MongoDBConnection.java
│       │           │
│       │           ├── controller/
│       │           │   ├── LoginController.java
│       │           │   ├── RegisterController.java
│       │           │   ├── AdminDashboardController.java
│       │           │   └── UserDashboardController.java
│       │           │
│       │           ├── model/
│       │           │   ├── User.java
│       │           │   └── Ticket.java
│       │           │
│       │           ├── repository/
│       │           │   ├── UserRepository.java
│       │           │   └── TicketRepository.java
│       │           │
│       │           ├── service/
│       │           │   └── AuthService.java
│       │           │
│       │           └── Main.java
│       │
│       └── resources/
│           └── fxml/
│               ├── login.fxml
│               ├── register.fxml
│               ├── admin-dashboard.fxml
│               └── user-dashboard.fxml
│
├── pom.xml
├── .gitignore
└── README.md
```

## ⚙️ Installation and Setup

### Prerequisites

Make sure you have installed:

- Java JDK 17
- Apache Maven
- MongoDB Community Server
- Visual Studio Code (or any Java IDE)

### 1. Clone the Repository

```bash
git clone https://github.com/vijaya-harshini/java-helpdesk-system.git
```

### 2. Navigate to the Project Directory

```bash
cd SmartHelpDesk
```

### 3. Configure MongoDB

Start your MongoDB server.

The application uses the following default configuration:

```text
Connection: mongodb://localhost:27017
Database: helpdesk_db
Collection: tickets
```

Ensure MongoDB is running before starting the application.

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn clean javafx:run
```

## 🔐 Admin Account Setup

The application includes an admin setup utility.

Run the following command to create the admin account:

```bash
mvn compile exec:java "-Dexec.mainClass=com.helpdesk.AdminSetup"
```

Use the admin credentials configured in the setup utility to log in.

**Note:** Regular user registration creates USER accounts. Admin accounts are created separately.

## 🎟️ Ticket Status

Tickets support three statuses:

| Status | Description |
|---|---|
| OPEN | Newly submitted ticket |
| IN_PROGRESS | Ticket currently being handled |
| RESOLVED | Issue has been resolved |

## 🔒 Security

- Passwords are hashed using BCrypt.
- User and admin roles are handled separately.
- Users can view their own tickets.
- Admins can view and manage all tickets.

## 🎯 Project Objective

The objective of Smart HelpDesk is to provide a simple and organized desktop application for managing support requests, tracking issues, and improving the support ticket management process.

## 👩‍💻 Author

**Vijaya Harshini D**

B.Tech – Artificial Intelligence and Data Science

## 📄 License

This project is developed for educational and academic purposes.
