
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String CONNECTION_STRING =
            "mongodb://localhost:27017";

    private static final String DATABASE_NAME = "helpdesk_db";

    public static void main(String[] args) {

        try (MongoClient client =
                     MongoClients.create(CONNECTION_STRING)) {

            MongoDatabase database =
                    client.getDatabase(DATABASE_NAME);

            // Contact MongoDB to verify the connection
            database.listCollectionNames().first();

            System.out.println("MongoDB connected successfully!");
            System.out.println("Database: " + DATABASE_NAME);

        } catch (Exception e) {
            System.out.println("MongoDB connection failed!");
            System.out.println("Error: " + e.getMessage());
        }
    }
}
