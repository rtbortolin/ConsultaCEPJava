package main.java.consultaCEP.infra.db;

import java.util.Date;

import org.bson.Document;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.interfaces.IAddressRepository;

public class AddressRepository extends BaseRepository implements IAddressRepository {

	public AddressRepository() {
		super("addresses");

		if (!indexExists("cep"))
			connection.getCollection().createIndex(new Document("cep", 1)); // true));
	}

	@Override
	public Address getAddres(String cep) {
		connection.openConnection();
		Document document = connection.getCollection().find(new Document("cep", cep)).first();
		return fillAddress(document);
	}

	@Override
	public void saveAddress(Address address) {

		try {
			connection.openConnection();
			Address dbAddress = getAddres(address.getCep());
			if (dbAddress != null) {
				address.setCreatedIn(dbAddress.getCreatedIn());
				updateAddress(address);
			} else
				connection.getCollection().insertOne(convertAddress(address));
		} finally {
			connection.closeConnection();
		}
	}

	private void updateAddress(Address address) {
		address.setUpdatedIn(new Date());
		connection.getCollection().updateOne(new Document("cep", address.getCep()),
				new Document("$set", convertAddress(address)));
	}

	private Address fillAddress(Document dbObject) {
		if (dbObject == null)
			return null;
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

	private Document convertAddress(Address address) {
		Document dbo = new Document();

		dbo.append("cep", address.getCep());
		dbo.append("logradouro", address.getLogradouro());
		dbo.append("bairro", address.getBairro());
		dbo.append("cidade", address.getCidade());
		dbo.append("uf", address.getUf());
		dbo.append("created-in", address.getCreatedIn());
		dbo.append("updated-in", address.getUpdatedIn());

		return dbo;
	}
}
