
package com.helpdesk.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String CONNECTION_STRING =
            "mongodb://localhost:27017";

    private static final String DATABASE_NAME =
            "helpdesk_db";

    private static final MongoClient client =
            MongoClients.create(CONNECTION_STRING);

    private static final MongoDatabase database =
            client.getDatabase(DATABASE_NAME);

    // Get MongoDB database
    public static MongoDatabase getDatabase() {
        return database;
    }

    // Close MongoDB connection
    public static void closeConnection() {
        client.close();
    }
}
