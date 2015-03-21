package consultaCEP.interfaces;

import consultaCEP.implementation.*;

public interface ICorreiosWebAccess {
	Address getResponse(String cep);
}
