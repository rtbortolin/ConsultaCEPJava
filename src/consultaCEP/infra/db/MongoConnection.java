package consultaCEP.infra.db;

import com.mongodb.*;

import consultaCEP.implementation.Address;

import java.net.UnknownHostException;

public class MongoConnection {

	public boolean connect(Address address) throws UnknownHostException {

		String connectionString = "mongodb://usr_consultacep:XXXX@ds048537.mongolab.com:48537/consultacep_prod";
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