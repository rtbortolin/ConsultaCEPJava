package consultaCEP.domain.services;

import java.lang.Thread.State;

import consultaCEP.domain.entities.Address;
import consultaCEP.interfaces.IAddressRepository;
import consultaCEP.interfaces.ICorreiosWebAccess;
import consultaCEP.interfaces.ISearchCepService;

public class SearchCepService implements ISearchCepService {

	private ICorreiosWebAccess CorreiosWebAcesss;
	private IAddressRepository AddressRepository;

	public SearchCepService(ICorreiosWebAccess correiosWebAccess,
			IAddressRepository addressRepository) {
		CorreiosWebAcesss = correiosWebAccess;
		AddressRepository = addressRepository;
	}

	@Override
	public Address getAddress(String cep) {

		WebRunnable webRunnable = new WebRunnable(cep);
		DbRunnable dbRunnable = new DbRunnable(cep);

		Thread webThread = new Thread(webRunnable);
		Thread dbThread = new Thread(dbRunnable);

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
						&& dbThread.getState() != State.TERMINATED);
			}

		});

		mainThread.start();

		Thread saveAddressThread = new Thread(new Runnable() {

			@Override
			public void run() {
				do {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (webThread.getState() != State.TERMINATED);
				
				AddressRepository.saveAddress(webRunnable.getAddress());
			}

		});
		saveAddressThread.start();

		try {
			mainThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Address returnAddress = null;
		if (webThread.getState() == State.TERMINATED)
			returnAddress = webRunnable.getAddress();
		else if (dbThread.getState() == State.TERMINATED)
			returnAddress = dbRunnable.getAddress();

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
