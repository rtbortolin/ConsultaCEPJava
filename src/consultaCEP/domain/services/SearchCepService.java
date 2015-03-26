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

		Thread saveAddressThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (webThread.getState() != State.TERMINATED) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				AddressRepository.saveAddress(webRunnable.getAddress());
			}

		});
		saveAddressThread.start();

		Address returnAddress = null;
		if (dbRunnable.getAddress() != null){
			returnAddress = dbRunnable.getAddress();
			System.out.println("cep: " + cep + " - db");
		}
		else if (webRunnable.getAddress() != null){
			returnAddress = webRunnable.getAddress();
			System.out.println("cep: " + cep + " - wb");
		}

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
