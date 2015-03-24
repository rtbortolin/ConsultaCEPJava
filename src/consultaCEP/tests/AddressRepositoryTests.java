package consultaCEP.tests;

import static org.junit.Assert.*;

import org.junit.*;

import consultaCEP.implementation.Address;
import consultaCEP.infra.db.AddressRepository;
import consultaCEP.interfaces.IAddressRepository;

public class AddressRepositoryTests {

	@Before
	public void setUp() throws Exception {
		new AddressRepository().dropDataBase();
	}

	@Test
	public void address_get_should_return_an_address_with_an_existing_cep() {
		String cep = "13570-003";
		Address reference = new Address("Rua Professor Paulo Monte Serrat",
				"Jardim Ricetti", "S�o Carlos", "SP", cep);

		IAddressRepository repository = new AddressRepository();
		repository.saveAddress(reference);

		repository = new AddressRepository();

		Address address = repository.getAddres("13570-003");

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
		String cep = "13570-003";
		Address reference = new Address("Rua Professor Paulo Monte Serrat",
				"Jardim Ricetti", "S�o Carlos", "SP", cep);

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
		String cep = "13570-003";
		String logradouro = "Rua Professor Paulo Monte Serrat";
		String updatedLogradouro = logradouro + " - updated";
		String bairro = "Jardim Ricetti";
		String cidade = "S�o Carlos";
		String uf = "SP";
		Address reference = new Address(logradouro, bairro, cidade, uf, cep);

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
}