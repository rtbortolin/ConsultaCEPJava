package main.java.consultaCEP.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.domain.services.CorreiosWebAccess;
import main.java.consultaCEP.interfaces.ICorreiosWebAccess;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Rafael
 *
 */
public class CorreiosWebAccessTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		CorreiosAccess = new CorreiosWebAccess();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private ICorreiosWebAccess CorreiosAccess;

	@Test
	public void get_response_test_should_return_null_if_invalid_cep() {
		Address response = CorreiosAccess.getResponse("");

		assertNull(response);
	}

	@Test
	public void get_response_test_should_return_address_if_valid_cep() {
		Address response = CorreiosAccess.getResponse("13570-003");

		Address reference = new Address("Rua Professor Paulo Monte Serrat",
				"Jardim Ricetti", "São Carlos", "SP", "13570-003");

		assertEquals(reference.getLogradouro(), response.getLogradouro());
		assertEquals(reference.getBairro(), response.getBairro());
		assertEquals(reference.getCidade(), response.getCidade());
		assertEquals(reference.getUf(), response.getUf());
		assertEquals(reference.getCep(), response.getCep());
	}

	@Test
	public void get_response_test_should_return_address_if_valid_cep_2() {
		Address response = CorreiosAccess.getResponse("14403-720");

		Address reference = new Address("Avenida São Vicente - lado �mpar",
				"Jardim No�mia", "Franca", "SP", "14403-720");

		assertEquals(reference.getLogradouro(), response.getLogradouro());
		assertEquals(reference.getBairro(), response.getBairro());
		assertEquals(reference.getCidade(), response.getCidade());
		assertEquals(reference.getUf(), response.getUf());
		assertEquals(reference.getCep(), response.getCep());
	}

	@Test
	public void get_response_test_should_return_address_if_valid_cep_3() {
		Address response = CorreiosAccess.getResponse("21010-790");

		Address reference = new Address("Pra�a Getúlio Vargas",
				"Parada de Lucas", "Rio de Janeiro", "RJ", "21010-790");

		assertEquals(reference.getLogradouro(), response.getLogradouro());
		assertEquals(reference.getBairro(), response.getBairro());
		assertEquals(reference.getCidade(), response.getCidade());
		assertEquals(reference.getUf(), response.getUf());
		assertEquals(reference.getCep(), response.getCep());
	}

}
