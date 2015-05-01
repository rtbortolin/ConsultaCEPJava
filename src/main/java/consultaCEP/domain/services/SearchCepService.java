package main.java.consultaCEP.domain.services;

import java.lang.Thread.State;

import main.java.consultaCEP.domain.entities.Address;
import main.java.consultaCEP.infra.Cache;
import main.java.consultaCEP.interfaces.IAddressRepository;
import main.java.consultaCEP.interfaces.ICorreiosWebAccess;
import main.java.consultaCEP.interfaces.ISearchCepService;

public class SearchCepService implements ISearchCepService {

	private ICorreiosWebAccess CorreiosWebAcesss;
	private IAddressRepository AddressRepository;

	public SearchCepService(ICorreiosWebAccess correiosWebAccess,
			IAddressRepository addressRepository) {
		CorreiosWebAcesss = correiosWebAccess;
		AddressRepository = addressRepository;
	}

	@Override
	public Address getAddress(final String cep) {

		Address result = Cache.get(Address.class, cep);
		if (result != null)
			return result;

		final WebRunnable webRunnable = new WebRunnable(cep);
		final DbRunnable dbRunnable = new DbRunnable(cep);

		final Thread webThread = new Thread(webRunnable);
		final Thread dbThread = new Thread(dbRunnable);

		webThread.start();
		dbThread.start();

		Thread mainThread = new Thread(new Runnable() {

			@Override
			public void run() {
				do {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (webThread.getState() != State.TERMINATED
						|| (dbThread.getState() != State.TERMINATED && dbRunnable
								.getAddress() != null));
			}

		});

		mainThread.start();
		try {
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		final Thread saveAddressThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (webThread.getState() != State.TERMINATED) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (webRunnable.getAddress() == null) {
					System.out.println("cep: " + cep + " - nf");
					return;
				}
				AddressRepository.saveAddress(webRunnable.getAddress());
			}

		});
		saveAddressThread.start();

		Address returnAddress = null;
		if (dbRunnable.getAddress() != null) {
			returnAddress = dbRunnable.getAddress();
			System.out.println("cep: " + cep + " - db");
		} else if (webRunnable.getAddress() != null) {
			returnAddress = webRunnable.getAddress();
			System.out.println("cep: " + cep + " - wb");
		}

		Cache.put(cep, returnAddress);
		return returnAddress;
	}

	@Override
	public Address getAddressFromWeb(String cep) {
		return CorreiosWebAcesss.getResponse(cep);
	}

	@Override
	public Address getAddressFromDb(String cep) {
		return AddressRepository.getAddres(cep);
	}

	private class WebRunnable implements Runnable {

		Address Address;
		String Cep;

		public Address getAddress() {
			return Address;
		}

		public WebRunnable(String cep) {
			Cep = cep;
		}

		@Override
		public void run() {
			Address = getAddressFromWeb(Cep);
		}
	}

	private class DbRunnable extends WebRunnable {

		public DbRunnable(String cep) {
			super(cep);
		}

		@Override
		public void run() {
			Address = getAddressFromDb(Cep);
		}
	}
}
