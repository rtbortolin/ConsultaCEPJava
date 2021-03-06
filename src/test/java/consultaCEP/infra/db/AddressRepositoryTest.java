package test.java.consultaCEP.infra.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.infra.db.AddressRepository;
import main.java.consultaCEP.infra.db.MongoConnection;
import main.java.consultaCEP.interfaces.IAddressRepository;
import test.java.AddressesMock;

public class AddressRepositoryTest {

	@Before
	public void setUp() throws Exception {
		AddressRepository repo = new AddressRepository();
		MongoConnection connection = repo.connection;
		connection.dropDataBase();
	}

	@Test
	public void address_get_should_return_an_address_with_an_existing_cep() {
		String cep = "14800-010";
		Address reference = AddressesMock.get(cep);

		IAddressRepository repository = new AddressRepository();
		repository.saveAddress(reference);

		repository = new AddressRepository();

		Address address = repository.getAddres("14800-010");

		assertNotNull(address);
		assertNotEquals("", address.getBairro());
		assertNotEquals("", address.getCep());
		assertNotEquals("", address.getCidade());
		assertNotEquals("", address.getLogradouro());
		assertNotEquals("", address.getUf());
	}

	@Test
	public void address_get_should_return_null_with_an_not_existing_cep() {
		IAddressRepository repository = new AddressRepository();
		Address address = repository.getAddres("00000-000");

		assertNull(address);
	}

	@Test
	public void save_address_should_persist_a_new_address() {
		String cep = "14800-010";
		Address reference = AddressesMock.get(cep);

		IAddressRepository repository = new AddressRepository();
		repository.saveAddress(reference);

		Address dbAddress = repository.getAddres(cep);

		assertEquals(reference.getLogradouro(), dbAddress.getLogradouro());
		assertEquals(reference.getBairro(), dbAddress.getBairro());
		assertEquals(reference.getCidade(), dbAddress.getCidade());
		assertEquals(reference.getUf(), dbAddress.getUf());
		assertEquals(reference.getCep(), dbAddress.getCep());
	}

	@Test
	public void save_address_should_update_an_existing_address() {
		String cep = "69005-070";
		Address reference = AddressesMock.get(cep);

		String logradouro = reference.getLogradouro();
		String updatedLogradouro = logradouro + " - updated";
		String bairro = reference.getBairro();
		String cidade = reference.getCidade();
		String uf = reference.getUf();

		reference = new Address(logradouro, bairro, cidade, uf, cep);

		IAddressRepository repository = new AddressRepository();
		repository.saveAddress(reference);

		reference = new Address(updatedLogradouro, bairro, cidade, uf, cep);
		repository.saveAddress(reference);

		Address dbAddress = repository.getAddres(cep);

		assertEquals(updatedLogradouro, dbAddress.getLogradouro());
		assertEquals(bairro, dbAddress.getBairro());
		assertEquals(cidade, dbAddress.getCidade());
		assertEquals(uf, dbAddress.getUf());
		assertEquals(cep, dbAddress.getCep());
	}

	@Test
	public void save_address_should_change_update_in_field() {
		String cep = "69005-070";
		Address reference = AddressesMock.get(cep);

		String logradouro = reference.getLogradouro();
		String updatedLogradouro = logradouro + " - updated";
		String bairro = reference.getBairro();
		String cidade = reference.getCidade();
		String uf = reference.getUf();

		reference = new Address(logradouro, bairro, cidade, uf, cep);

		Date updatedInDate = reference.getUpdatedIn();

		IAddressRepository repository = new AddressRepository();
		repository.saveAddress(reference);

		reference = new Address(updatedLogradouro, bairro, cidade, uf, cep);
		repository.saveAddress(reference);

		Address dbAddress = repository.getAddres(cep);

		assertNotEquals(updatedInDate, dbAddress.getUpdatedIn());
	}
}
