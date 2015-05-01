package main.java.consultaCEP.interfaces;

import main.java.consultaCEP.domain.entities.Address;

public interface ICorreiosWebAccess {
	Address getResponse(String cep);
}
