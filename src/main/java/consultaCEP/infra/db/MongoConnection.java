package main.java.consultaCEP.infra.db;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

	private String dbname = DBConfiguration.getDbName();
	private String connectionString = DBConfiguration.getDbConnectionString();

	private MongoClient mongoclient;
	private MongoDatabase db;
	private MongoCollection<Document> collection;

	private static Map<String, MongoConnection> connections;

	public static MongoConnection Instance(String collection) {
		if (connections == null)
			connections = new HashMap<String, MongoConnection>();

		MongoConnection connection = connections.get(collection);
		if (connection == null) {
			connection = new MongoConnection(collection);
		}

		return connection;
	}

	public MongoCollection<Document> getCollection() {
		return collection;
	}

	public MongoDatabase getDB() {
		return db;
	}

	public MongoClient getMongoClient() {
		return mongoclient;
	}

	private MongoConnection(String collectionName) {
		System.out.println("MongoConnection instantiated " + collectionName);
		openConnection();
		collection = db.getCollection(collectionName);
		if (collection == null) {
			db.createCollection(collectionName);
		}
		collection = db.getCollection(collectionName);
	}

	protected boolean openConnection() {
		MongoClientURI uri = new MongoClientURI(connectionString);
		mongoclient = new MongoClient(uri);
		db = mongoclient.getDatabase(dbname);
		return true;
	}

	protected void closeConnection() {
		if (mongoclient != null)
			mongoclient.close();
	}

	public void dropDataBase() {
		db.drop();
	}
}