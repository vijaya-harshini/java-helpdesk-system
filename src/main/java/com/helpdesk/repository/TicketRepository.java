
package com.helpdesk.repository;

import com.helpdesk.model.Ticket;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class TicketRepository {

    private final MongoClient client;
    private final MongoCollection<Document> collection;

    // MongoDB connection
    public TicketRepository() {

        client = MongoClients.create(
                "mongodb://localhost:27017"
        );

        MongoDatabase database =
                client.getDatabase("helpdesk_db");

        collection = database.getCollection("tickets");
    }

    // Safely convert MongoDB ID
    private Integer getTicketId(Document document) {

        Object value = document.get("id");

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    // Generate next ticket ID
    private int generateNextTicketId() {

        Document lastTicket = collection
                .find()
                .sort(descending("id"))
                .first();

        Integer lastId = lastTicket == null
                ? null
                : getTicketId(lastTicket);

        return lastId == null ? 101 : lastId + 1;
    }

    // Create ticket with user ownership
    public int createTicket(String problem, String userEmail) {

        int nextId = generateNextTicketId();

        Document ticket = new Document("id", nextId)
                .append("problem", problem)
                .append("status", "OPEN")
                .append("userEmail", userEmail);

        collection.insertOne(ticket);

        return nextId;
    }

    // Backward-compatible method
    public int createTicket(String problem) {

        int nextId = generateNextTicketId();

        Document ticket = new Document("id", nextId)
                .append("problem", problem)
                .append("status", "OPEN");

        collection.insertOne(ticket);

        return nextId;
    }

    // Convert MongoDB document into Ticket object
    private Ticket convertToTicket(Document document) {

        Integer id = getTicketId(document);

        if (id == null) {
            return null;
        }

        String problem = document.getString("problem");
        String status = document.getString("status");
        String userEmail = document.getString("userEmail");

        if (problem == null) {
            problem = "No description";
        }

        if (status == null) {
            status = "OPEN";
        }

        return new Ticket(
                id,
                problem,
                status,
                userEmail
        );
    }

    // Get all tickets (Admin)
    public List<Ticket> getAllTickets() {

        List<Ticket> tickets = new ArrayList<>();

        for (Document document :
                collection.find().sort(ascending("id"))) {

            Ticket ticket = convertToTicket(document);

            if (ticket != null) {
                tickets.add(ticket);
            }
        }

        return tickets;
    }

    // Get tickets belonging to a particular user
    public List<Ticket> getTicketsByUserEmail(String userEmail) {

        List<Ticket> tickets = new ArrayList<>();

        for (Document document :
                collection.find(eq("userEmail", userEmail))
                        .sort(descending("id"))) {

            Ticket ticket = convertToTicket(document);

            if (ticket != null) {
                tickets.add(ticket);
            }
        }

        return tickets;
    }

    // Find ticket by ID
    public Ticket findTicket(int id) {

        Document document =
                collection.find(eq("id", id)).first();

        if (document == null) {
            return null;
        }

        return convertToTicket(document);
    }

    // Update ticket status
    public boolean updateTicketStatus(int id, String status) {

        if (!List.of("OPEN", "IN_PROGRESS", "RESOLVED")
                .contains(status)) {
            return false;
        }

        Ticket ticket = findTicket(id);

        if (ticket == null) {
            return false;
        }

        collection.updateOne(
                eq("id", id),
                new Document("$set",
                        new Document("status", status))
        );

        return true;
    }

    // Resolve ticket
    public boolean resolveTicket(int id) {

        return updateTicketStatus(id, "RESOLVED");
    }

    // Total ticket count
    public long getTotalTicketCount() {

        return collection.countDocuments();
    }

    // Count tickets by status
    public long getTicketCountByStatus(String status) {

        return collection.countDocuments(
                eq("status", status)
        );
    }

    // Close MongoDB connection
    public void close() {

        client.close();
    }
}
