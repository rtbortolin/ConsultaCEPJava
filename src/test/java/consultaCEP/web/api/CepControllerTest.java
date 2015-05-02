package test.java.consultaCEP.web.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.interfaces.ISearchCepService;
import main.java.consultaCEP.web.api.CepController;

import org.junit.Before;
import org.junit.Test;

import test.java.AddressesMock;

public class CepControllerTest {

	private ISearchCepService ServiceMock;

	@Before
	public void setUp() throws Exception {
		ServiceMock = mock(ISearchCepService.class);
	}
	
	@Test
	public void getCepShouldReturnValidAddress(){
		// for override mockito static service
		CepController controller = new CepController(null);
		controller = new CepController();
		String cep = "14800-010";
		
		Response result = controller.getCep(cep);
		
		assertNotNull(result.getEntity());
		assertEquals(AddressesMock.get(cep).getCep(),
				((Address) result.getEntity()).getCep());
	}

	@Test
	public void get_cep_should_return_an_valid_address_for_a_valid_cep() {
		CepController controller = new CepController(ServiceMock);
		String cep = "69005-070";

		when(ServiceMock.getAddress(cep)).thenReturn(AddressesMock.get(cep));

		Response result = controller.getCep(cep);

		assertEquals(AddressesMock.get(cep).getCep(),
				((Address) result.getEntity()).getCep());
	}
	
	@Test
	public void getCepShouldReturnNullForAnInvalidCep(){
		CepController controller = new CepController(ServiceMock);
		String cep = "123456-789";

		when(ServiceMock.getAddress(cep)).thenReturn(null);

		Response result = controller.getCep(cep);
		
		assertNull(result.getEntity());
	}
	
	@Test
	public void getCepShouldCallSerchCepServiceGetAddress(){
		CepController controller = new CepController(ServiceMock);
		String cep = "14800-010";

		when(ServiceMock.getAddress(cep)).thenReturn(AddressesMock.get(cep));

		Response result = controller.getCep(cep);

		assertEquals(AddressesMock.get(cep).getCep(),
				((Address) result.getEntity()).getCep());
		
		verify(ServiceMock, times(1)).getAddress(cep);
		verify(ServiceMock, times(0)).getAddressFromDb(cep);
		verify(ServiceMock, times(0)).getAddressFromWeb(cep);
	}
	
	@Test
	public void get_cep_should_return_cors_header(){
		CepController controller = new CepController(ServiceMock);
		String cep = "69005-070";

		when(ServiceMock.getAddress(cep)).thenReturn(AddressesMock.get(cep));

		Response result = controller.getCep(cep);

		assertTrue(result.getMetadata().get("Access-Control-Allow-Origin").toString().equals("[*]"));
	}
	
	@Test
	public void get_cep_should_return_http_cache_header(){
		CepController controller = new CepController(ServiceMock);
		String cep = "69005-070";

		when(ServiceMock.getAddress(cep)).thenReturn(AddressesMock.get(cep));

		Response result = controller.getCep(cep);

		assertTrue(result.getMetadata().get("Cache-Control").toString().equals("[private, no-transform, max-age=86400]"));
	}

	
	@Test
	public void get_cep_should_return_allow_methods_header(){
		CepController controller = new CepController(ServiceMock);
		String cep = "69005-070";

		when(ServiceMock.getAddress(cep)).thenReturn(AddressesMock.get(cep));

		Response result = controller.getCep(cep);

		assertTrue(result.getMetadata().get("Access-Control-Allow-Methods").toString().equals("[GET, POST, OPTIONS]"));
	}
}
