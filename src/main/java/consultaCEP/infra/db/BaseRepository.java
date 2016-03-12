package main.java.consultaCEP.infra.db;

import org.bson.Document;

import com.mongodb.client.ListIndexesIterable;

public abstract class BaseRepository {

	public MongoConnection connection;

	protected BaseRepository(String collection) {
		connection = MongoConnection.Instance("addresses");
	}

	protected boolean indexExists(String index) {
		ListIndexesIterable<Document> indexes = connection.getCollection().listIndexes();
		for (Document document : indexes) {
			Object keyObj = document.get("key");
			if (keyObj == null)
				continue;
			if (((Document) keyObj).containsKey("cep")) {
				return true;
			}
		}
		return false;
	}
}
