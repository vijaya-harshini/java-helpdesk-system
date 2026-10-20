# HelpDesk Ticket Management System

A simple Java desktop application for managing helpdesk tickets, built using Java Swing and MongoDB.

## Features

- Create support tickets
- View all tickets in a table
- Search tickets by ID
- Resolve open tickets
- Store ticket data persistently in MongoDB
- Automatically generate ticket IDs

## Technologies Used

- Java 17
- Java Swing
- MongoDB 8.2
- MongoDB Java Driver
- Maven

## Project Structure

```text
HelpDesk/
├── src/
│   └── main/
│       └── java/
│           ├── App.java
│           ├── Ticket.java
│           ├── MongoDBConnection.java
│           ├── TicketRepository.java
│           └── HelpDeskGUI.java
├── pom.xml
└── README.md
```

## Database Configuration

- Database: `helpdesk_db`
- Collection: `tickets`
- Connection: `mongodb://localhost:27017`

Make sure MongoDB Server is running before launching the application.

## How to Run

1. Install Java 17, Maven, and MongoDB.
2. Start MongoDB Server.
3. Open a terminal in the project folder.
4. Compile the project:

   ```bash
   mvn compile
   ```

5. Run `HelpDeskGUI` with the compiled classes and MongoDB driver dependencies on the Java classpath.

## Learning Outcomes

- Practiced Java object-oriented programming
- Built a graphical interface using Swing
- Connected Java to MongoDB
- Implemented database CRUD operations
- Managed project dependencies with Maven
