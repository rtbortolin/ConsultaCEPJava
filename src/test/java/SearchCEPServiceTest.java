package test.java;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.isNotNull;
import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.domain.services.SearchCepService;
import main.java.consultaCEP.infra.Cache;
import main.java.consultaCEP.interfaces.IAddressRepository;
import main.java.consultaCEP.interfaces.ICache;
import main.java.consultaCEP.interfaces.ICorreiosWebAccess;
import main.java.consultaCEP.interfaces.ISearchCepService;

import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class SearchCEPServiceTest {

	private ICorreiosWebAccess CorreiosWebAccessMock;
	private IAddressRepository AddressRepositoryMock;
	private Cache cache;

	private ISearchCepService SearchCepService;

	@Before
	public void setUp() throws Exception {
		CorreiosWebAccessMock = mock(ICorreiosWebAccess.class);
		AddressRepositoryMock = mock(IAddressRepository.class);
		cache = mock(Cache.class);
		when(cache.get((Class<?>)isNotNull(), (String)isNotNull())).thenReturn(null);
		Cache.setInstance(cache);

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
		String cep = "00000-000";

		SearchCepService.getAddress(cep);

		verify(CorreiosWebAccessMock, times(1)).getResponse(cep);
		verify(AddressRepositoryMock, times(1)).getAddres(cep);
	}

	@Test
	public void get_address_should_return_addres_from_any_mock() {
		String cep = "13570-003";
		final Address webAddress = new Address("webAddress", "", "", "", cep);
		final Address dbAddress = new Address("dbAddress", "", "", "", cep);

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

		boolean isCorrectAddress = address.getLogradouro() == webAddress
				.getLogradouro()
				|| address.getLogradouro() == dbAddress.getLogradouro();

		assertTrue(isCorrectAddress);
	}

	@Test
	public void get_address_should_return_addres_from_faster_mock_1() {
		String cep = "13570-003";
		final Address webAddress = new Address("webAddress", "", "", "", cep);
		final Address dbAddress = new Address("dbAddress", "", "", "", cep);

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
		final Address webAddress = new Address("webAddress", "", "", "", cep);
		final Address dbAddress = new Address("dbAddress", "", "", "", cep);

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
	public void get_address_should_update_or_save_the_returned_addres_from_web_1()
			throws InterruptedException {
		String cep = "13570-003";
		final Address webAddress = new Address("webAddress", "", "", "", cep);
		final Address dbAddress = new Address("dbAddress", "", "", "", cep);
		final int threadWait = 500;

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
		final Address webAddress = new Address("webAddress", "", "", "", cep);
		final Address dbAddress = new Address("dbAddress", "", "", "", cep);

		final int threadWait = 500;

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

	@Test
	public void get_address_should_not_save_the_returned_addres_from_web_when_null()
			throws InterruptedException {
		String cep = "123456-789";
		final Address webAddress = null;
		final Address dbAddress = null;

		final int threadWait = 500;

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
		verify(AddressRepositoryMock, times(0)).saveAddress(webAddress);

		assertSame(dbAddress, address);
	}

}
