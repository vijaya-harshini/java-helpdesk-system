
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

    public TicketRepository() {
        client = MongoClients.create("mongodb://localhost:27017");

        MongoDatabase database = client.getDatabase("helpdesk_db");
        collection = database.getCollection("tickets");
    }

    public int createTicket(String problem) {
        Document lastTicket = collection
                .find()
                .sort(descending("id"))
                .first();

        int nextId = lastTicket == null
                ? 101
                : lastTicket.getInteger("id") + 1;

        Document ticket = new Document("id", nextId)
                .append("problem", problem)
                .append("status", "OPEN");

        collection.insertOne(ticket);

        return nextId;
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        for (Document document :
                collection.find().sort(ascending("id"))) {

            Ticket ticket = new Ticket(
                    document.getInteger("id"),
                    document.getString("problem"),
                    document.getString("status")
            );

            tickets.add(ticket);
        }

        return tickets;
    }

    public Ticket findTicket(int id) {
        Document document = collection.find(eq("id", id)).first();

        if (document == null) {
            return null;
        }

        return new Ticket(
                document.getInteger("id"),
                document.getString("problem"),
                document.getString("status")
        );
    }

    public boolean resolveTicket(int id) {
        Ticket ticket = findTicket(id);

        if (ticket == null || ticket.status.equals("RESOLVED")) {
            return false;
        }

        collection.updateOne(
                eq("id", id),
                new Document("$set",
                        new Document("status", "RESOLVED"))
        );

        return true;
    }

    public void close() {
        client.close();
    }
}
