package com.company;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class MongoDB {

    private static MongoClient mongoClient;

    private final static String TELEGRAM_DATABASE = "telegram";
    private final static String CUSTOMERS_COLLECTION = "customers";

    public static void connectToDatabase() {

        String uri = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";

        mongoClient = MongoClients.create(uri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);

        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = mongoDatabase.runCommand(command);
            System.out.println("Connected successfully to server");
        } catch (MongoException mongoException) {
            mongoException.printStackTrace();
        }
    }

    public static void insertNewUserId(String userId) {

        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);
        MongoCollection<Document> customersCollection = mongoDatabase.getCollection(CUSTOMERS_COLLECTION);

        try {
            if (!userExists(userId)) {
                InsertOneResult result = customersCollection.insertOne(new Document().append("_id", userId));
                System.out.println(result.getInsertedId());
            }

        } catch (MongoException mongoException) {
            mongoException.printStackTrace();
        }

    }

    public static boolean userExists(String userId) {

        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);
        MongoCollection<Document> customersCollection = mongoDatabase.getCollection(CUSTOMERS_COLLECTION);

        Document userDoc = customersCollection.find(eq("_id", userId)).first();

        if (userDoc == null) {
            return false;
        } else {
            return true;
        }

    }

}
