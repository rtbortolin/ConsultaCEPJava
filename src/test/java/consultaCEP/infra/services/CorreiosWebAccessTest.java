package test.java.consultaCEP.infra.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.infra.services.CorreiosWebAccess;
import main.java.consultaCEP.interfaces.ICorreiosWebAccess;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.java.AddressesMock;

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
		Address response = CorreiosAccess.getResponse("14800-010");

		Address reference = AddressesMock.get("14800-010");

		assertEquals(reference.getLogradouro(), response.getLogradouro());
		assertEquals(reference.getBairro(), response.getBairro());
		assertEquals(reference.getCidade(), response.getCidade());
		assertEquals(reference.getUf(), response.getUf());
		assertEquals(reference.getCep(), response.getCep());
	}

	@Test
	public void get_response_test_should_return_address_if_valid_cep_2() {
		Address response = CorreiosAccess.getResponse("06010-020");

		Address reference = AddressesMock.get("06010-020");

		assertEquals(reference.getLogradouro(), response.getLogradouro());
		assertEquals(reference.getBairro(), response.getBairro());
		assertEquals(reference.getCidade(), response.getCidade());
		assertEquals(reference.getUf(), response.getUf());
		assertEquals(reference.getCep(), response.getCep());
	}

	@Test
	public void get_response_test_should_return_address_if_valid_cep_3() {
		Address response = CorreiosAccess.getResponse("69005-070");

		Address reference = AddressesMock.get("69005-070");

		assertEquals(reference.getLogradouro(), response.getLogradouro());
		assertEquals(reference.getBairro(), response.getBairro());
		assertEquals(reference.getCidade(), response.getCidade());
		assertEquals(reference.getUf(), response.getUf());
		assertEquals(reference.getCep(), response.getCep());
	}

	@Test
	public void get_response_test_should_return_null_if_not_found_cep() {
		Address response = CorreiosAccess.getResponse("123456-789");

		assertNull(response);
	}
}
