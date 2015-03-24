package consultaCEP.interfaces;

import consultaCEP.domain.entities.Address;

public interface ICorreiosWebAccess {
	Address getResponse(String cep);
}
