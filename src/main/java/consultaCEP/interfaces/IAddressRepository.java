package main.java.consultaCEP.interfaces;

import main.java.consultaCEP.domain.entities.Address;

public interface IAddressRepository {

	public abstract Address getAddres(String cep);

	public abstract void saveAddress(Address address);

}