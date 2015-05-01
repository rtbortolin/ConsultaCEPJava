package main.java.consultaCEP.interfaces;

import main.java.consultaCEP.domain.entities.Address;

public interface ISearchCepService {

	Address getAddress(String cep);
	
	Address getAddressFromWeb(String cep);
	
	Address getAddressFromDb(String cep);
}
