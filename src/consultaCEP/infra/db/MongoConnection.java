package consultaCEP.infra.db;

import com.mongodb.*;

import java.net.UnknownHostException;

public abstract class MongoConnection {

	private String dbname = "consultacep_dev";
	private String user = "usr_consultacep";
	private String password = "F3n1x";
	private String connectionString = "mongodb://" + user + ":" + password
			+ "@ds053139.mongolab.com:53139/consultacep_dev";

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
	
	public void dropDataBase(){
		db.dropDatabase();
	}

}