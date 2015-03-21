package consultaCEP.interfaces;

import consultaCEP.implementation.Address;

public interface ISearchCepService {

	Address getAddress(String cep);
	
	Address getAddressFromWeb(String cep);
	
	Address getAddressFromDb(String cep);
}
