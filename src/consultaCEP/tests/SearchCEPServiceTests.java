package consultaCEP.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import consultaCEP.interfaces.*;
import consultaCEP.implementation.*;

public class SearchCEPServiceTests {

	private ICorreiosWebAccess CorreiosWebAccessMock;
	private IAddressRepository AddressRepositoryMock;

	private ISearchCepService SearchCepService;

	@Before
	public void setUp() throws Exception {
		CorreiosWebAccessMock = mock(ICorreiosWebAccess.class);
		AddressRepositoryMock = mock(IAddressRepository.class);

		SearchCepService = new SearchCepService(CorreiosWebAccessMock,
				AddressRepositoryMock);
	}

	@Test
	public void get_address_from_web_should_call_correios_web_access_mock() {
		String cep = "13570-003";

		SearchCepService.getAddressFromWeb(cep);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(0)).getAddres(cep);
	}

	@Test
	public void get_address_from_db_should_call_address_repository_mock() {
		String cep = "13570-003";

		SearchCepService.getAddressFromDb(cep);

		verify(CorreiosWebAccessMock, times(0)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);
	}

	@Test
	public void get_address_should_call_both_mock() {
		String cep = "13570-003";

		SearchCepService.getAddress(cep);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);
	}

	@Test
	public void get_address_should_return_addres_from_any_mock() {
		String cep = "13570-003";
		Address webAddress = new Address("webAddress", "", "", "", cep);
		Address dbAddress = new Address("dbAddress", "", "", "", cep);

		when(this.CorreiosWebAccessMock.getResponse(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						return webAddress;
					}
				});

		when(this.AddressRepositoryMock.getAddres(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						return dbAddress;
					}
				});

		Address address = SearchCepService.getAddress(cep);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);

		boolean isCorrectAddress = address.getLogradouro() == webAddress.getLogradouro()
				|| address.getLogradouro() == dbAddress.getLogradouro();

		assertTrue(isCorrectAddress);
	}

	@Test
	public void get_address_should_return_addres_from_faster_mock_1() {
		String cep = "13570-003";
		Address webAddress = new Address("webAddress", "", "", "", cep);
		Address dbAddress = new Address("dbAddress", "", "", "", cep);
		
		when(this.CorreiosWebAccessMock.getResponse(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						Thread.sleep(1000);
						return webAddress;
					}
				});

		when(this.AddressRepositoryMock.getAddres(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						return dbAddress;
					}
				});

		Address address = SearchCepService.getAddress(cep);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);

		assertSame(dbAddress, address);
	}

	@Test
	public void get_address_should_return_addres_from_faster_mock_2() {
		String cep = "13570-003";
		Address webAddress = new Address("webAddress", "", "", "", cep);
		Address dbAddress = new Address("dbAddress", "", "", "", cep);

		when(this.CorreiosWebAccessMock.getResponse(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {

						return webAddress;
					}
				});

		when(this.AddressRepositoryMock.getAddres(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						Thread.sleep(1000);
						return dbAddress;
					}
				});

		Address address = SearchCepService.getAddress(cep);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);

		assertSame(webAddress, address);
	}

	@Test
	public void get_address_should_update_or_save_the_returned_addres_from_web_1() throws InterruptedException {
		String cep = "13570-003";
		Address webAddress = new Address("webAddress", "", "", "", cep);
		Address dbAddress = new Address("dbAddress", "", "", "", cep);
		int threadWait = 1000;
		
		when(this.CorreiosWebAccessMock.getResponse(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						return webAddress;
					}
				});

		when(this.AddressRepositoryMock.getAddres(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						Thread.sleep(threadWait);
						return dbAddress;
					}
				});

		Address address = SearchCepService.getAddress(cep);

		Thread.sleep(threadWait + 500);
		
		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);
		verify(AddressRepositoryMock, times(1)).saveAddress(webAddress);

		assertSame(webAddress, address);
	}

	@Test
	public void get_address_should_update_or_save_the_returned_addres_from_web_2()
			throws InterruptedException {
		String cep = "13570-003";
		Address webAddress = new Address("webAddress", "", "", "", cep);
		Address dbAddress = new Address("dbAddress", "", "", "", cep);

		int threadWait = 1000;

		when(this.CorreiosWebAccessMock.getResponse(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						Thread.sleep(threadWait);
						return webAddress;
					}
				});

		when(this.AddressRepositoryMock.getAddres(cep)).thenAnswer(
				new Answer<Address>() {
					@Override
					public Address answer(InvocationOnMock invocation)
							throws Throwable {
						return dbAddress;
					}
				});

		Address address = SearchCepService.getAddress(cep);

		Thread.sleep(threadWait + 500);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);
		verify(AddressRepositoryMock, times(1)).saveAddress(webAddress);

		assertSame(dbAddress, address);
	}

}
