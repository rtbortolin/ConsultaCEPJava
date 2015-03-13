package consultaCEP.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import consultaCEP.implementation.Address;
import consultaCEP.infra.db.MongoConnection;

public class MongoConnectionTests {

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
		mongoConnection = new MongoConnection();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private MongoConnection mongoConnection;

	@Test
	public void mongo_connect_shoul_return_true_if_sucess() {
		
		Address reference = new Address("Rua Professor Paulo Monte Serrat",
				"Jardim Ricetti", "São Carlos", "SP", "13570-003");
		
		try {
			boolean result = mongoConnection.connect(reference);
			Assert.assertTrue(result);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}