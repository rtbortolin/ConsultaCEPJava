package consultaCEP.interfaces;

import consultaCEP.implementation.*;

public interface ICorreiosAccess {
	Address getResponse(String cep);
}
