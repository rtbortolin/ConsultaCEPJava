package main.java.consultaCEP.infra.db;

public abstract class BaseRepository {

	public MongoConnection connection;
	
	protected BaseRepository(String collection){
		connection = MongoConnection.Instance("addresses");
	}
	
}
