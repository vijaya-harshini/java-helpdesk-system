
package com.helpdesk.repository;

import com.helpdesk.config.MongoDBConnection;
import com.helpdesk.model.User;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import org.bson.Document;

public class UserRepository {

    private final MongoCollection<Document> collection;

    public UserRepository() {

        collection = MongoDBConnection
                .getDatabase()
                .getCollection("users");

        // Ensure email addresses are unique
        collection.createIndex(
                Indexes.ascending("email"),
                new IndexOptions().unique(true)
        );
    }

    // Save a new user
    public boolean save(User user) {

        // Check whether email already exists
        if (findByEmail(user.getEmail()) != null) {
            return false;
        }

        Document document = new Document()
                .append("name", user.getName())
                .append("email", user.getEmail())
                .append("passwordHash", user.getPasswordHash())
                .append("role", user.getRole());

        collection.insertOne(document);

        user.setId(document.getObjectId("_id"));

        return true;
    }

    // Find user using email
    public User findByEmail(String email) {

        Document document = collection.find(
                Filters.eq("email", email)
        ).first();

        if (document == null) {
            return null;
        }

        User user = new User();

        user.setId(document.getObjectId("_id"));
        user.setName(document.getString("name"));
        user.setEmail(document.getString("email"));
        user.setPasswordHash(document.getString("passwordHash"));
        user.setRole(document.getString("role"));

        return user;
    }
}
