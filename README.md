# Information Sharing System

A Java console-based Information Sharing System that enables organizations to securely share information using a MySQL database. The project is built using Java, JDBC, and MySQL, demonstrating database connectivity and CRUD operations.

## Features

- Add new organizations
- View all organizations
- Create information items
- Share information between organizations
- View incoming share requests
- Accept share requests
- Display all database records
- Console-based interactive menu

## Tech Stack

- Java
- JDBC
- MySQL
- SQL
- Git & GitHub

## Project Structure

```
InformationSharingSystem/
│── InfoShareMySQL.java
│── mysql-connector-j.jar
│── README.md
```

## Database

Database Name:
```
infoshare
```

Tables Used:
- organization
- info_item
- share_request

## How to Run

1. Clone the repository
   ```bash
   git clone https://github.com/YOUR_USERNAME/InformationSharingSystem.git
   ```

2. Open the project in your preferred Java IDE (IntelliJ IDEA, Eclipse, or VS Code).

3. Create the MySQL database:
   ```sql
   CREATE DATABASE infoshare;
   ```

4. Create the required tables.

5. Update the database credentials in `InfoShareMySQL.java`:

   ```java
   static final String URL = "jdbc:mysql://localhost:3306/infoshare";
   static final String USER = "root";
   static final String PASS = "your_password";
   ```

6. Compile and run the program.

## Sample Menu

```
1. Add Organization
2. List Organizations
3. Create Info Item
4. Share Info Item
5. View Incoming Requests
6. Accept Request
7. Show Entire Database
0. Exit
```

## Learning Outcomes

- Java Programming
- JDBC Connectivity
- MySQL Database Integration
- CRUD Operations
- Prepared Statements
- Console Application Development

## Future Enhancements

- GUI using Java Swing/JavaFX
- User Authentication
- Role-Based Access Control
- File Sharing Support
- Search and Filter Functionality
- Audit Logs
- REST API Integration

## Author

**Shivaleela A. Ballary**

---
