package consultaCEP.interfaces;

import consultaCEP.domain.entities.Address;

public interface ISearchCepService {

	Address getAddress(String cep);
	
	Address getAddressFromWeb(String cep);
	
	Address getAddressFromDb(String cep);
}
