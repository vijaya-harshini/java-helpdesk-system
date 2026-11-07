
package com.helpdesk;

import com.helpdesk.config.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import java.io.Console;
import java.util.Scanner;

public class AdminSetup {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            MongoDatabase database =
                    MongoDBConnection.getDatabase();

            MongoCollection<Document> users =
                    database.getCollection("users");

            System.out.println("=== Smart HelpDesk Admin Setup ===");

            System.out.print("Enter admin name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter admin email: ");
            String email = scanner.nextLine()
                    .trim().toLowerCase();

            Console console = System.console();
            String password;

            if (console != null) {
                char[] passwordChars =
                        console.readPassword("Enter admin password: ");
                password = new String(passwordChars);
                java.util.Arrays.fill(passwordChars, '\0');
            } else {
                System.out.print("Enter admin password: ");
                password = scanner.nextLine();
            }

            if (name.isEmpty() || email.isEmpty()
                    || password.length() < 8) {
                System.out.println(
                        "Invalid input. Password must have at least 8 characters."
                );
                return;
            }

            Document existingUser = users.find(
                    Filters.eq("email", email)
            ).first();

            if (existingUser != null) {
                System.out.println(
                        "An account with this email already exists."
                );
                return;
            }

            String hashedPassword =
                    BCrypt.hashpw(password, BCrypt.gensalt(12));

            Document admin = new Document()
                    .append("name", name)
                    .append("email", email)
                    .append("passwordHash", hashedPassword)
                    .append("role", "ADMIN");

            users.insertOne(admin);

            System.out.println(
                    "Admin account created successfully!"
            );

        } catch (Exception e) {
            System.out.println(
                    "Admin setup failed: " + e.getMessage()
            );
        } finally {
            MongoDBConnection.closeConnection();
            scanner.close();
        }
    }
}
