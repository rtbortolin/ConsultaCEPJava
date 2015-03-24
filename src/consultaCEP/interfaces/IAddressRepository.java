package consultaCEP.interfaces;

import consultaCEP.domain.entities.Address;

public interface IAddressRepository {

	public abstract Address getAddres(String cep);

	public abstract void saveAddress(Address address);

}