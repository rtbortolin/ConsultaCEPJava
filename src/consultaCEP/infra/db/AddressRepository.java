package consultaCEP.infra.db;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import consultaCEP.domain.entities.Address;
import consultaCEP.interfaces.IAddressRepository;

public class AddressRepository extends MongoConnection implements IAddressRepository {

	public AddressRepository() {
		super("addresses");
		collection.createIndex(new BasicDBObject("cep", 1), new BasicDBObject("unique", true));
	}

	@Override
	public Address getAddres(String cep) {
		openConnection();
		DBCursor cursor = collection.find(new BasicDBObject("cep", cep));
		try {
			while (cursor.hasNext()) {
				return fillAddress((BasicDBObject) cursor.next());
			}
			return null;
		} finally {
			cursor.close();
			closeConnection();
		}
	}

	@Override
	public void saveAddress(Address address) {

		try {
			openConnection();
			Address dbAddress = getAddres(address.getCep());
			if (dbAddress != null){
				address.setCreatedIn(dbAddress.getCreatedIn());
				updateAddress(address);
			}
			else
				collection.insert(convertAddress(address));

		} finally {
			closeConnection();
		}
	}

	private void updateAddress(Address address) {
		address.setUpdatedIn(new Date());
		collection.update(new BasicDBObject("cep", address.getCep()),
				convertAddress(address));
	}

	private Address fillAddress(BasicDBObject dbObject) {
		String logradouro = dbObject.getString("logradouro");
		String cep = dbObject.getString("cep");
		String bairro = dbObject.getString("bairro");
		String cidade = dbObject.getString("cidade");
		String uf = dbObject.getString("uf");		
		Date createdIn = dbObject.getDate("created-in");
		Date updatedIn = dbObject.getDate("updated-in");

		Address address = new Address(logradouro, bairro, cidade, uf, cep);
		address.setCreatedIn(createdIn);
		address.setUpdatedIn(updatedIn);
		return address;
	}

	private DBObject convertAddress(Address address) {
		BasicDBObject dbo = new BasicDBObject();

		dbo.put("cep", address.getCep());
		dbo.put("logradouro", address.getLogradouro());
		dbo.put("bairro", address.getBairro());
		dbo.put("cidade", address.getCidade());
		dbo.put("uf", address.getUf());
		dbo.put("created-in", address.getCreatedIn());
		dbo.put("updated-in",address.getUpdatedIn());

		return dbo;
	}
}
