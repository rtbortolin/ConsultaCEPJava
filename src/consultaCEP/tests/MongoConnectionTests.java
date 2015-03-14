package consultaCEP.tests;

import static org.junit.Assert.*;

import org.junit.*;

import consultaCEP.implementation.Address;
import consultaCEP.infra.db.AddressRepository;

public class MongoConnectionTests {

	@Test
	public void address_get_should_return_an_address_with_an_existing_cep() {
		AddressRepository repository = new AddressRepository();
		Address address = repository.getAddres("13570-003");

		assertNotEquals("", address.getBairro());
		assertNotEquals("", address.getCep());
		assertNotEquals("", address.getCidade());
		assertNotEquals("", address.getLogradouro());
		assertNotEquals("", address.getUf());
	}
	
	@Test
	public void address_get_should_return_null_with_an_not_existing_cep() {
		AddressRepository repository = new AddressRepository();
		Address address = repository.getAddres("00000-000");

		assertNull(address);		
	}
}
