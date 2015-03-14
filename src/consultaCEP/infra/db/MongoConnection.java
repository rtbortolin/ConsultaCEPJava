package consultaCEP.infra.db;

import com.mongodb.*;

import consultaCEP.implementation.Address;

import java.net.UnknownHostException;

public abstract class MongoConnection {

	private String dbname = "consultacep_prod";
	private String user = "usr_consultacep";
	private String password = "F3n1x";
	private String connectionString = "mongodb://" + user + ":" + password
			+ "@ds048537.mongolab.com:48537/consultacep_prod";

	protected MongoClient mongoclient;
	protected DB db;

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

	public boolean connect(Address address) throws UnknownHostException {

		MongoClientURI uri = new MongoClientURI(connectionString);
		MongoClient mongo = new MongoClient(uri);

		DB db = mongo.getDB("consultacep_prod");

		DBCollection addresses = db.getCollection("addresses");

		WriteResult result = addresses.insert(convertAddress(address));
		System.out.println(result);

		DBObject myDoc = addresses.findOne();
		System.out.println(myDoc);
		return true;
	}

	private DBObject convertAddress(Address address) {
		BasicDBObject dbo = new BasicDBObject();

		dbo.put("cep", address.getCep());
		dbo.put("logradouro", address.getLogradouro());
		dbo.put("bairro", address.getBairro());
		dbo.put("cidade", address.getCidade());
		dbo.put("uf", address.getUf());

		return dbo;

	}
}