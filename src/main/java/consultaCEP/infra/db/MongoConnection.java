package main.java.consultaCEP.infra.db;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoConnection {

	private String dbname = DBConfiguration.getDbName();
	private String connectionString = DBConfiguration.getDbConnectionString();

	private MongoClient mongoclient;
	private DB db;
	private DBCollection collection;

	private static Map<String, MongoConnection> connections;

	public static MongoConnection Instance(String collection) {
		if (connections == null)
			connections = new HashMap<String, MongoConnection>();

		MongoConnection connection = connections.get(collection);
		if (connection == null)
			connection = new MongoConnection(collection);

		return connection;
	}

	public DBCollection getCollection() {
		return collection;
	}

	public DB getDB() {
		return db;
	}

	public MongoClient getMongoClient() {
		return mongoclient;
	}

	private MongoConnection(String collectionName) {
		System.out.println("MongoConnection instantiated " + collectionName);
		openConnection();
		boolean collectionExists = db.collectionExists(collectionName);
		if (collectionExists == false) {
			db.createCollection(collectionName, null);
		}
		collection = db.getCollection(collectionName);
	}

	protected boolean openConnection() {
		try {
			mongoclient = new MongoClient(new MongoClientURI(connectionString));
			db = mongoclient.getDB(dbname);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected void closeConnection() {
		if (mongoclient != null)
			mongoclient.close();
	}

	public void dropDataBase() {
		db.dropDatabase();
	}
}