package org.pizza.bot;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

public class MongoDB {

    private static MongoClient mongoClient;

    private final static String TELEGRAM_DATABASE = "telegramDB";
    private final static String CUSTOMERS_COLLECTION = "customers";
    public final static String ORDER_STATE = "ORDER_STATE";
    public final static String PIZZA_TYPE = "PIZZA_TYPE";
    public final static String PIZZA_SIZE = "PIZZA_SIZE";
    public final static String DRINK = "DRINK";
    public final static String ADDRESS = "ADDRESS";

    public static void connectToDatabase() {

        String uri = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";

        mongoClient = MongoClients.create(uri);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);

        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = mongoDatabase.runCommand(command);
        } catch (MongoException mongoException) {
            mongoException.printStackTrace();
        }
    }

    public static void insertNewUserId(String userId) {

        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);
        MongoCollection<Document> customersCollection = mongoDatabase.getCollection(CUSTOMERS_COLLECTION);

        try {
            if (!userExists(userId)) {
                InsertOneResult result = customersCollection.insertOne(new Document().append("_id", userId).append(ORDER_STATE, OrderState.PIZZA_SELECTION.toString()).append(PIZZA_TYPE, "").append(PIZZA_SIZE, "").append(DRINK, "").append(ADDRESS, ""));
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

    public static boolean updateField(String fieldName, String newValue, String userChatId) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);
        MongoCollection<Document> customersCollection = mongoDatabase.getCollection(CUSTOMERS_COLLECTION);

        UpdateResult result = customersCollection.updateOne(eq("_id", userChatId), new Document("$set", new Document(fieldName, newValue)));
        return result.wasAcknowledged() && result.getModifiedCount() == 1;

    }

    public static String getFieldValue(String fieldName, String userChatId) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);
        MongoCollection<Document> customersCollection = mongoDatabase.getCollection(CUSTOMERS_COLLECTION);

        Document result = customersCollection.find(eq("_id", userChatId)).projection(fields(include(fieldName), excludeId())).first();

        return result.getString(fieldName);
    }

    public static Document getCustomerAttributes(String userChatId) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(TELEGRAM_DATABASE);
        MongoCollection<Document> customersCollection = mongoDatabase.getCollection(CUSTOMERS_COLLECTION);
        Document result = customersCollection.find(eq("_id", userChatId)).first();

        return result;

    }
}
