package consultaCEP.infra.db;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import consultaCEP.implementation.Address;

public class AddressRepository extends MongoConnection {

	private DBCollection collection;

	public AddressRepository() {
		openConnection();
		collection = db.getCollection("addresses");
	}

	public Address getAddres(String cep) {
		openConnection();
		DBCursor cursor = collection.find(new BasicDBObject("cep", cep));

		try {
			while (cursor.hasNext()) {
				return fillAddress((BasicDBObject)cursor.next());
			}
			return null;
		} finally {
			cursor.close();
		}
	}

	private Address fillAddress(BasicDBObject dbObject) {
			String logradouro = dbObject.getString("logradouro");
			String cep = dbObject.getString("cep");
			String bairro = dbObject.getString("bairro");
			String cidade = dbObject.getString("cidade");
			String uf = dbObject.getString("uf");
			
			return new Address(logradouro, bairro, cidade, uf, cep);			
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
